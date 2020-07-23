package com.mashibing.mashibing.system.io.testRPC.server;

import com.mashibing.mashibing.system.io.testRPC.request.RequestBean;
import com.mashibing.mashibing.system.io.testRPC.request.RequestHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * 客户端数据解码器
 */
public class ClientRequestDecoder extends ByteToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
      List<Object> list) throws Exception {

    if(byteBuf.readableBytes()>= RequestHeader.REQUEST_HEAD_LEN){
      //对客户端的请求解码
      byte[] headerBytes = new byte[RequestHeader.REQUEST_HEAD_LEN];
      byteBuf.getBytes(byteBuf.readerIndex(),headerBytes);//不会移动读取的位置
      RequestHeader requestHeader = RequestHeader.deserialize(headerBytes);

      int sign = requestHeader.getSign();
      if(sign == RequestHeader.DEFAULT_REQUEST_SIGN){
        int dataLen = (int)requestHeader.getDataLen();
        if(byteBuf.readableBytes()>=(RequestHeader.REQUEST_HEAD_LEN+dataLen)){

          byte[] requestBytes = new byte[RequestHeader.REQUEST_HEAD_LEN+dataLen];
          byteBuf.readBytes(requestBytes);
          RequestBean requestBean = RequestBean.deserialize(requestBytes);
          list.add(requestBean);
        }
      }

    }else {
      //其他报文处理
    }

  }
}
