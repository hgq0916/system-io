package com.mashibing.mashibing.system.io.testNetty01;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 选择器组
 */
public class SelectorThreadGroup {

  SelectorThread[] selectorThreads;

  AtomicInteger incr = new AtomicInteger(0);

  public SelectorThreadGroup(int n){
    selectorThreads = new SelectorThread[n];
    for(int i=0;i<selectorThreads.length;i++){
      selectorThreads[i] = new SelectorThread(this);
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
      SelectorThread selectorThread = next();
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
  public SelectorThread next() {
    int i = incr.incrementAndGet();
    int index = i % selectorThreads.length;
    return selectorThreads[index];
  }


}
