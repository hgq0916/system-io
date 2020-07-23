package com.mashibing.mashibing.system.io.testRPC;

import com.mashibing.mashibing.system.io.testRPC.bean.User;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.IHelloService
 * @Description: hello服务
 * @date 2020/7/23 10:13
 */
public interface IHelloService {

  String sayHello(String name);

  User getUserById(String id);

}
