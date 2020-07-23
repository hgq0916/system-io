package com.mashibing.mashibing.system.io.testRPC.server;

import com.mashibing.mashibing.system.io.testRPC.request.RequestBean;
import com.mashibing.mashibing.system.io.testRPC.request.RequestContent;
import com.mashibing.mashibing.system.io.testRPC.response.ResponseBean;
import com.mashibing.mashibing.system.io.testRPC.response.ResponseBody;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.lang.reflect.Method;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.server.ClientRequestHandler
 * @Description: 客户端请求处理
 * @date 2020/7/23 16:13
 */
public class ClientRequestHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf buf = (ByteBuf) msg;
    byte[] data = new byte[buf.readableBytes()];
    buf.readBytes(data);

    //对客户端的请求解码
    RequestBean requestBean = RequestBean.deserialize(data);

    System.out.println("收到请求："+requestBean);
    //处理请求
    ResponseBean responseBean = handleRequest(requestBean);
    byte[] responseData = responseBean.serialize();

    NioSocketChannel channel = (NioSocketChannel) ctx.channel();
    ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer();
    byteBuf.writeBytes(responseData);
    channel.writeAndFlush(byteBuf);
  }

  private ResponseBean handleRequest(RequestBean requestBean){

    try{
      RequestContent requestContent = requestBean.getRequestContent();
      String className = requestContent.getServerName();
      Class<?> aClass = Class.forName(className);
      //todo 从spring容器中获取实现类
      Object service = BeanFactory.getBean(aClass);

      String methodName = requestContent.getMethodName();
      Class<?>[] parameterTypes = requestContent.getParameterTypes();
      Method method = aClass.getMethod(methodName, parameterTypes);

      Object[] args = requestContent.getArgs();

      Object result = method.invoke(service, args);

      ResponseBody responseBody = new ResponseBody(result);
      ResponseBean responseBean = new ResponseBean(requestBean.getRequestHeader().getRequestId(),responseBody);

      return responseBean;
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

}
