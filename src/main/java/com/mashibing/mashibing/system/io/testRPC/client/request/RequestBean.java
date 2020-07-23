package com.mashibing.mashibing.system.io.testRPC.client.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.UUID;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.request.RequestBean
 * @Description: 客户端请求
 * @date 2020/7/23 12:03
 */
public class RequestBean {

  private static final int DEFAULT_REQUEST_SIGN = 0x14141414;

  private RequestHeader requestHeader;

  private RequestContent requestContent;

  public RequestHeader getRequestHeader() {
    return requestHeader;
  }

  public RequestBean(RequestContent requestContent){
    this.requestContent = requestContent;
  }

  public RequestContent getRequestContent() {
    return requestContent;
  }

  public void setRequestContent(
      RequestContent requestContent) {
    this.requestContent = requestContent;
  }

  public byte[] serialize() throws IOException {
    ByteArrayOutputStream bos = null;
    ObjectOutputStream oos = null;
    try{
      bos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(bos);
      oos.writeObject(requestContent);
      oos.flush();
      byte[] contentBytes = bos.toByteArray();

      requestHeader = new RequestHeader();
      requestHeader.setSign(DEFAULT_REQUEST_SIGN);
      requestHeader.setRequestId(UUID.randomUUID().getLeastSignificantBits());
      requestHeader.setDataLen(contentBytes.length);

      //序列化请求体
      byte[] headBytes = requestHeader.serialize();

      byte[] requestBytes = new byte[headBytes.length+contentBytes.length];

      System.arraycopy(headBytes,0,requestBytes,0,headBytes.length);
      System.arraycopy(contentBytes,0,requestBytes,headBytes.length,contentBytes.length);

      return requestBytes;
    }catch (IOException e){
      throw e;
    }finally {
      try{
        if(oos != null){
          oos.close();
        }
        if(bos != null){
          bos.close();
        }
      }catch (IOException e){
        e.printStackTrace();
      }
    }
  }

}
