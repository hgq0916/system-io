package com.mashibing.mashibing.system.io.testRPC.client;

import com.mashibing.mashibing.system.io.testRPC.IHelloService;
import com.mashibing.mashibing.system.io.testRPC.request.RequestBean;
import com.mashibing.mashibing.system.io.testRPC.request.RequestContent;
import com.mashibing.mashibing.system.io.testRPC.response.ResponseBean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

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
    return (T)Proxy.newProxyInstance(classLoader,clazzs,new ProxyInvocationHandler(clazz));

  }

}
