package com.mashibing.mashibing.system.io.testRPC.request;

import com.mashibing.mashibing.system.io.testRPC.response.ResponseBean;
import com.mashibing.mashibing.system.io.testRPC.response.ResponseBody;
import com.mashibing.mashibing.system.io.testRPC.response.ResponseHeader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.request.RequestBean
 * @Description: 客户端请求
 * @date 2020/7/23 12:03
 */
public class RequestBean {

  private RequestHeader requestHeader;

  private RequestContent requestContent;

  public RequestBean(){}

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

  public static RequestBean deserialize(byte[] stream) throws Exception {
    RequestHeader requestHeader = RequestHeader.deserialize(stream);
    long dataLen = requestHeader.getDataLen();
    if(dataLen< (stream.length-RequestHeader.REQUEST_HEAD_LEN)) throw new IllegalStateException("invalid requestStream");

    byte[] bodyBytes = Arrays
        .copyOfRange(stream, RequestHeader.REQUEST_HEAD_LEN, RequestHeader.REQUEST_HEAD_LEN+ (int) dataLen);

    ByteArrayInputStream bis = new ByteArrayInputStream(bodyBytes);
    ObjectInputStream ois = new ObjectInputStream(bis);
    Object object = ois.readObject();

    RequestContent requestContent = (RequestContent) object;

    RequestBean requestBean = new RequestBean();
    requestBean.requestHeader = requestHeader;
    requestBean.requestContent = requestContent;

    return requestBean;
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

      requestHeader = new RequestHeader(UUID.randomUUID().getLeastSignificantBits(),contentBytes.length);

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

  @Override
  public String toString() {
    return "RequestBean{" +
        "requestHeader=" + requestHeader +
        ", requestContent=" + requestContent +
        '}';
  }
}
