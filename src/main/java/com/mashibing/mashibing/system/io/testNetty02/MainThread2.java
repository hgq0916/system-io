package com.mashibing.mashibing.system.io.testNetty02;

public class MainThread2 {

  public static void main(String[] args) {
    SelectorThreadGroup2 workerThreadGroup = new SelectorThreadGroup2(5);
    SelectorThreadGroup2 selectorThreadGroup = new SelectorThreadGroup2(1,workerThreadGroup);
    selectorThreadGroup.bind(9090);
    selectorThreadGroup.bind(9091);
  }

}
