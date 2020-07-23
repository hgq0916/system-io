package com.mashibing.mashibing.system.io.testRPC.response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.response.ResponseBean
 * @Description: 响应体
 * @date 2020/7/23 13:47
 */
public class ResponseBean {

  private ResponseHeader responseHeader;

  private ResponseBody responseBody;

  private long requestId;

  public ResponseHeader getResponseHeader() {
    return responseHeader;
  }

  public ResponseBody getResponseBody() {
    return responseBody;
  }

  private ResponseBean(){}

  public ResponseBean(long requestId, ResponseBody responseBody){
    this.responseBody = responseBody;
    this.requestId = requestId;
  }

  public byte[] serialize() throws IOException {
    ByteArrayOutputStream bos = null;
    ObjectOutputStream oos = null;
    try{
      bos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(bos);
      oos.writeObject(responseBody);
      oos.flush();
      byte[] contentBytes = bos.toByteArray();

      responseHeader = new ResponseHeader(requestId,contentBytes.length);

      //序列化响应体
      byte[] headBytes = responseHeader.serialize();

      byte[] responseBytes = new byte[headBytes.length+contentBytes.length];

      System.arraycopy(headBytes,0,responseBytes,0,headBytes.length);
      System.arraycopy(contentBytes,0,responseBytes,headBytes.length,contentBytes.length);

      return responseBytes;
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

  public static ResponseBean deserialize(byte[] responseStream)
      throws Exception {

    ResponseHeader responseHeader = ResponseHeader.deserialize(responseStream);
    long dataLen = responseHeader.getDataLen();
    if(dataLen< (responseStream.length-ResponseHeader.RESPONSE_HEAD_LEN)) throw new IllegalStateException("invalid responseStream");

    byte[] bodyBytes = Arrays
        .copyOfRange(responseStream, ResponseHeader.RESPONSE_HEAD_LEN, ResponseHeader.RESPONSE_HEAD_LEN + (int) dataLen);

    ByteArrayInputStream bis = new ByteArrayInputStream(bodyBytes);
    ObjectInputStream ois = new ObjectInputStream(bis);
    Object object = ois.readObject();

    ResponseBody responseBody = (ResponseBody) object;

    ResponseBean responseBean = new ResponseBean();
    responseBean.responseHeader = responseHeader;
    responseBean.responseBody = responseBody;

    return responseBean;
  }

  @Override
  public String toString() {
    return "ResponseBean{" +
        "responseHeader=" + responseHeader +
        ", responseBody=" + responseBody +
        '}';
  }
}
