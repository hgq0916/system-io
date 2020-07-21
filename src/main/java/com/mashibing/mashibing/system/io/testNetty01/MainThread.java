package com.mashibing.mashibing.system.io.testNetty01;

public class MainThread {

  public static void main(String[] args) {
    SelectorThreadGroup selectorThreadGroup = new SelectorThreadGroup(3);
    selectorThreadGroup.bind(9090);
  }

}
