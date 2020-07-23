package com.mashibing.mashibing.system.io.testRPC.server;

import com.mashibing.mashibing.system.io.testRPC.IHelloService;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.server.BeanFactory
 * @Description: 对象工厂
 * @date 2020/7/23 16:03
 */
public class BeanFactory {

  private static final ConcurrentHashMap<Class<?>,Object> beanMap = new ConcurrentHashMap<>();

  static {
    beanMap.putIfAbsent(IHelloService.class,new HelloService());
  }

  public static synchronized  <T> T getBean(Class<T> clazz){
    Object o = beanMap.get(clazz);

    return (T) o;
  }

}
