package com.mashibing.mashibing.system.io.testRPC.client;

import com.mashibing.mashibing.system.io.testRPC.client.response.ResponseBean;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.ClientResponseHandler
 * @Description: 客户端响应
 * @date 2020/7/23 15:22
 */
public class ClientResponseHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    ByteBuf buf = (ByteBuf) msg;
    byte[] data = new byte[buf.readableBytes()];
    buf.readBytes(data);

    //todo 考虑粘包问题
    ResponseBean responseBean = ResponseBean.deserialize(data);

    long requestId = responseBean.getResponseHeader().getRequestId();
    ResponseCallbackPool.exectueCallback(requestId,responseBean);

  }

}
