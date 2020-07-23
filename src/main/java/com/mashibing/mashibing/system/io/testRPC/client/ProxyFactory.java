package com.mashibing.mashibing.system.io.testRPC.client;

import com.mashibing.mashibing.system.io.testRPC.IHelloService;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.ProxyFactory
 * @Description: 代理工厂类
 * @date 2020/7/23 11:42
 */
public class ProxyFactory {

  public <T> T getBean(Class<T> clazz) {

    ClassLoader classLoader = clazz.getClassLoader();//获取类加载器

    Class<?>[] clazzs = new Class<?>[]{clazz};
    return (T)Proxy.newProxyInstance(classLoader,clazzs,new ProxyInvocationHandler());

  }

}
