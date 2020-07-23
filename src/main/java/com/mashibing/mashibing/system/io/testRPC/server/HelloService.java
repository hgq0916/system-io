package com.mashibing.mashibing.system.io.testRPC.server;

import com.mashibing.mashibing.system.io.testRPC.IHelloService;
import com.mashibing.mashibing.system.io.testRPC.bean.User;

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

  @Override
  public User getUserById(String id) {
    //todo 从数据库获取user信息
    return new User(id,"李四",30);
  }

}
