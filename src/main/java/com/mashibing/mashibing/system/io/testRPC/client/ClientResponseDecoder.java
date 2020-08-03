package com.mashibing.mashibing.system.io.testRPC.client;

import com.mashibing.mashibing.system.io.testRPC.response.ResponseBean;
import com.mashibing.mashibing.system.io.testRPC.response.ResponseHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * 客户端响应解码器
 */
public class ClientResponseDecoder extends ByteToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf,
      List<Object> list) throws Exception {

    if(byteBuf.readableBytes()>= ResponseHeader.RESPONSE_HEAD_LEN){
      byte[] headerBytes = new byte[ResponseHeader.RESPONSE_HEAD_LEN];
      byteBuf.getBytes(byteBuf.readerIndex(),headerBytes);
      ResponseHeader responseHeader = ResponseHeader.deserialize(headerBytes);

      if(responseHeader.getSign() == ResponseHeader.DEFAULT_REQUEST_SIGN){

        int dataLen = (int)responseHeader.getDataLen();
        if(byteBuf.readableBytes()>=(ResponseHeader.RESPONSE_HEAD_LEN+dataLen)){
          byte[] requestBytes = new byte[ResponseHeader.RESPONSE_HEAD_LEN+dataLen];
          byteBuf.readBytes(requestBytes);
          ResponseBean responseBean = ResponseBean.deserialize(requestBytes);
          list.add(responseBean);
        }

      }else {
        //其他报文处理
      }

    }
  }
}
