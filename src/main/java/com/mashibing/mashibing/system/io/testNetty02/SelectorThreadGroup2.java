package com.mashibing.mashibing.system.io.testNetty02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 选择器组
 */
public class SelectorThreadGroup2 {

  SelectorThread2[] selectorThreads;

  AtomicInteger incr = new AtomicInteger(0);

  private SelectorThreadGroup2 worker = this;

  public SelectorThreadGroup2(int n,SelectorThreadGroup2 worker){
    this(n);
    this.worker = worker;
  }

  public SelectorThreadGroup2(int n){
    this.worker = worker;
    selectorThreads = new SelectorThread2[n];
    for(int i=0;i<selectorThreads.length;i++){
      selectorThreads[i] = new SelectorThread2(this);
      new Thread(selectorThreads[i]).start();
    }
  }

  public void bind(int port){
    try {
      ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
      serverSocketChannel.configureBlocking(false);
      serverSocketChannel.bind(new InetSocketAddress(port));
      //把server注册到一个selector上
      //Selector selector = next(serverSocketChannel);
      //      //把server注册到selector上
//      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//      selector.wakeup();
      SelectorThread2 selectorThread = nextV2(serverSocketChannel);
      selectorThread.getLbd().add(serverSocketChannel);
      selectorThread.getSelector().wakeup();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 选择一个selector返回
   * @return
   */
  public SelectorThread2 next() {
    int i = incr.incrementAndGet();
    int index = i % selectorThreads.length;
    return selectorThreads[index];
  }

  public SelectorThread2 nextV2(Channel channel) {
    if(channel instanceof ServerSocketChannel){
      return next();
    }else if(channel instanceof SocketChannel){
      return worker.next();
    }
    return next();
  }


  public SelectorThreadGroup2 getWorker() {
    return worker;
  }
}
