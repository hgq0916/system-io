package com.mashibing.mashibing.system.io.testRPC.client;

import com.mashibing.mashibing.system.io.testRPC.client.request.RequestBean;
import com.mashibing.mashibing.system.io.testRPC.client.request.RequestContent;
import com.mashibing.mashibing.system.io.testRPC.client.response.ResponseBean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.ProxyInvocationHandler
 * @Description: 代理对象调用方法实现
 * @date 2020/7/23 11:48
 */
public class ProxyInvocationHandler implements InvocationHandler {

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    String serverName = proxy.getClass().getName();//服务名称
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
        .getClientConnection(new InetSocketAddress("192.168.68.129", 9090));
    ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer();
    byteBuf.writeBytes(serialize);
    ChannelFuture channelFuture = client.writeAndFlush(byteBuf);
    channelFuture.sync();//等待发送完成

    CountDownLatch countDownLatch = new CountDownLatch(1);
    AtomicReference<ResponseBean> responseRef = new AtomicReference<>();
    ResponseCallbackPool
        .addCallback(requestBean.getRequestHeader().getRequestId(), (responseBean) -> {
          countDownLatch.countDown();
          responseRef.set(responseBean);
        });
    countDownLatch.await();//阻塞
    //获取响应
    ResponseBean responseBean = responseRef.get();

    if (responseBean == null)
      return null;
    Object content = responseBean.getResponseBody().getContent();
    return content;
  }

}
