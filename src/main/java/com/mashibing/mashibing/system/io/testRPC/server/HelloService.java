package com.mashibing.mashibing.system.io.testRPC.server;

import com.mashibing.mashibing.system.io.testRPC.IHelloService;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.server.HelloService
 * @Description: 实现类
 * @date 2020/7/23 16:02
 */
public class HelloService implements IHelloService {

  @Override
  public String sayHello(String name) {
    return "hello,"+name;
  }

}
