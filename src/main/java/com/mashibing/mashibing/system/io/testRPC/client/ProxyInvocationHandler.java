package com.mashibing.mashibing.system.io.testRPC.client;

import com.mashibing.mashibing.system.io.testRPC.request.RequestBean;
import com.mashibing.mashibing.system.io.testRPC.request.RequestContent;
import com.mashibing.mashibing.system.io.testRPC.response.ResponseBean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.ProxyInvocationHandler
 * @Description: 代理对象调用方法实现
 * @date 2020/7/23 11:48
 */
public class ProxyInvocationHandler implements InvocationHandler {

  private Class<?> clazz;

  public ProxyInvocationHandler(Class<?> clazz){
    this.clazz = clazz;
  }

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

    //方式一
        /*CountDownLatch countDownLatch = new CountDownLatch(1);
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
        return content;*/

    //方式二：
    CompletableFuture completableFuture = new CompletableFuture();
    ResponseCallbackPool
        .addCallback(requestBean.getRequestHeader().getRequestId(),completableFuture );

    ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer();
    byteBuf.writeBytes(serialize);
    ChannelFuture channelFuture = client.writeAndFlush(byteBuf);
    channelFuture.sync();//等待发送完成

    ResponseBean responseBean = (ResponseBean) completableFuture.get();//get方法会阻塞
    if (responseBean == null)
      return null;
    Object content = responseBean.getResponseBody().getContent();
    return content;
  }

}
