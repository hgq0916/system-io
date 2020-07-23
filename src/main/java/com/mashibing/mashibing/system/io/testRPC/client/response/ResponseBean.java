package com.mashibing.mashibing.system.io.testRPC.client.response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.response.ResponseBean
 * @Description: 响应体
 * @date 2020/7/23 13:47
 */
public class ResponseBean {

  private ResponseHeader responseHeader;

  private ResponseBody responseBody;

  public ResponseHeader getResponseHeader() {
    return responseHeader;
  }

  public ResponseBody getResponseBody() {
    return responseBody;
  }

  private ResponseBean(){}

  public static ResponseBean deserialize(byte[] responseStream)
      throws Exception {

    ResponseHeader responseHeader = ResponseHeader.deserialize(responseStream);
    long dataLen = responseHeader.getDataLen();
    if(dataLen< (responseStream.length-ResponseHeader.RESPONSE_HEAD_LEN)) throw new IllegalStateException("invalid responseStream");

    byte[] bodyBytes = Arrays
        .copyOfRange(responseStream, ResponseHeader.RESPONSE_HEAD_LEN, (int) dataLen);

    ByteArrayInputStream bis = new ByteArrayInputStream(bodyBytes);
    ObjectInputStream ois = new ObjectInputStream(bis);
    Object object = ois.readObject();

    ResponseBody responseBody = new ResponseBody(object);

    ResponseBean responseBean = new ResponseBean();
    responseBean.responseHeader = responseHeader;
    responseBean.responseBody = responseBody;

    return responseBean;
  }

}
