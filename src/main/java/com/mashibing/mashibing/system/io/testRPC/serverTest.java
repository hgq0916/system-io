package com.mashibing.mashibing.system.io.testRPC;

import com.mashibing.mashibing.system.io.testRPC.server.Server;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.server.serverTest
 * @Description: 服务端测试
 * @date 2020/7/23 16:14
 */
public class serverTest {

  public static void main(String[] args) throws Exception {
    new Server().start();
  }

}
