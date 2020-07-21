package com.mashibing.mashibing.system.io.testNetty02;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

public class SelectorThread2 implements Runnable {

  private Selector selector;

  private SelectorThreadGroup2 selectorThreadGroup;

  private LinkedBlockingDeque<Channel> lbd = new LinkedBlockingDeque<>();

  public LinkedBlockingDeque<Channel> getLbd() {
    return lbd;
  }

  public Selector getSelector() {
    return selector;
  }

  public SelectorThread2(SelectorThreadGroup2 selectorThreadGroup){
    try {
      this.selectorThreadGroup = selectorThreadGroup;
      selector = Selector.open();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    System.out.println("当前线程："+Thread.currentThread().getName()+"启动");
    while (true){
      try {
        System.out.println("当前线程："+Thread.currentThread().getName()+"size:"+selector.keys().size());
        /**
         * selector.select会阻塞，在别的线程唤醒selector后，有可能再次进入阻塞状态而别的线程的代码没有机会执行，
         * 线程间使用队列来通信，当唤醒当前线程后，让当前线程自己来完成后续的工作任务
         */
        int selectCount = selector.select();
        /*try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }*/
        if(selectCount>0){
          Set<SelectionKey> selectionKeys = selector.selectedKeys();
          Iterator<SelectionKey> iterator = selectionKeys.iterator();
          while (iterator.hasNext()){
            SelectionKey selectionKey = iterator.next();
            iterator.remove();
            //判断事件类型
            if(selectionKey.isAcceptable()){
              //accept事件
              acceptHandler(selectionKey);
            }else if(selectionKey.isReadable()){
              //读事件
              readHandler(selectionKey);
            }
          }
        }

        //完成任务
        if(!lbd.isEmpty()){
          Channel channel = lbd.take();
          register(channel);
        }
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }

    }
  }

  private void readHandler(SelectionKey selectionKey) throws IOException {
    SocketChannel channel = (SocketChannel) selectionKey.channel();
    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
    buffer.clear();//清空缓冲区，用来存放读取的数据
    while(channel.read(buffer)>0){
      buffer.flip();//反转buffer
      while(buffer.hasRemaining()){
        channel.write(buffer);
      }
    }
  }

  private void acceptHandler(SelectionKey selectionKey) throws IOException {
    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
    SocketChannel socketChannel = serverSocketChannel.accept();//接收客户端
    socketChannel.configureBlocking(false);//配置客户端为非阻塞
    //把客户端添加到指定线程的队列
    SelectorThread2 selectorThread = selectorThreadGroup.nextV2(socketChannel);
    selectorThread.getLbd().add(socketChannel);
    selectorThread.getSelector().wakeup();
  }

  public void register(Channel channel){
    try {
      if(channel instanceof ServerSocketChannel){
        ((ServerSocketChannel) channel).register(selector,SelectionKey.OP_ACCEPT);
        System.out.println(Thread.currentThread().getName()+" 注册了监听事件："+channel);
      }else if(channel instanceof SocketChannel){
        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
        ((SocketChannel) channel).register(selector,SelectionKey.OP_READ,buffer);
        System.out.println(Thread.currentThread().getName()+" 注册了读取事件："+channel);
      }
    } catch (ClosedChannelException e) {
      e.printStackTrace();
    }
  }


}
