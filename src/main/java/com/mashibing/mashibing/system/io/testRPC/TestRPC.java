package com.mashibing.mashibing.system.io.testRPC;

import com.mashibing.mashibing.system.io.testRPC.IHelloService;
import com.mashibing.mashibing.system.io.testRPC.bean.User;
import com.mashibing.mashibing.system.io.testRPC.client.ProxyFactory;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.TestRPC
 * @Description: 主方法
 * @date 2020/7/23 10:16
 */
public class TestRPC {

  public static void main(String[] args) {
    //测试rpc调用
    ProxyFactory proxyFactory = new ProxyFactory();
    IHelloService helloService = proxyFactory.getBean(IHelloService.class);
    String sayHello = helloService.sayHello("张三");
    System.out.println("执行结果："+sayHello);
    User user = helloService.getUserById("2347329424");
    System.out.println("执行结果："+user);
  }

}
