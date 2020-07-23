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
    return (T)Proxy.newProxyInstance(classLoader,clazzs,new InvocationHandler(){

      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String serverName = clazz.getName();//服务名称
        String methodName = method.getName();//方法名
        Class<?>[] parameterTypes = method.getParameterTypes();//参数类型

        //请求体
        RequestContent requestContent = new RequestContent();
        requestContent.setServerName(serverName);
        requestContent.setMethodName(methodName);
        requestContent.setParameterTypes(parameterTypes);
        requestContent.setArgs(args);

        //请求内容
        RequestBean requestBean = new RequestBean(requestContent);
        byte[] serialize = requestBean.serialize();

        //获取一个客户端连接
        NioSocketChannel client = ClientFactory
            .getClientConnection(new InetSocketAddress("192.168.25.1", 9090));

        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicReference<ResponseBean> responseRef = new AtomicReference<>();
        ResponseCallbackPool
            .addCallback(requestBean.getRequestHeader().getRequestId(), (responseBean) -> {
              responseRef.set(responseBean);
              countDownLatch.countDown();
            });

        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer();
        byteBuf.writeBytes(serialize);
        ChannelFuture channelFuture = client.writeAndFlush(byteBuf);
        channelFuture.sync();//等待发送完成

        countDownLatch.await();//阻塞
        //获取响应
        ResponseBean responseBean = responseRef.get();

        if (responseBean == null) return null;
        Object content = responseBean.getResponseBody().getContent();
        return content;
      }
    });

  }

}
