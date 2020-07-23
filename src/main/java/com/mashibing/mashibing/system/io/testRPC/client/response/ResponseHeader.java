package com.mashibing.mashibing.system.io.testRPC.client.response;

import com.mashibing.mashibing.system.io.testRPC.utils.MathUtils;
import java.util.Arrays;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.response.ResponseHeader
 * @Description: 响应头
 * @date 2020/7/23 14:40
 */
public class ResponseHeader {

  public static final int RESPONSE_HEAD_LEN = 20;

  private int sign;//标志位

  private long requestId;//请求id

  private long dataLen;//数据长度

  public int getSign() {
    return sign;
  }

  public long getRequestId() {
    return requestId;
  }

  public long getDataLen() {
    return dataLen;
  }

  public void setSign(int sign) {
    this.sign = sign;
  }

  public void setRequestId(long requestId) {
    this.requestId = requestId;
  }

  public void setDataLen(long dataLen) {
    this.dataLen = dataLen;
  }

  private ResponseHeader(){}

  public static ResponseHeader deserialize(byte[] responseStream){
    if(responseStream == null || responseStream.length<ResponseHeader.RESPONSE_HEAD_LEN) throw new IllegalStateException("responseStream invalid");
    byte[] signBytes = Arrays.copyOfRange(responseStream, 0, 4);
    byte[] requestIdBytes = Arrays.copyOfRange(responseStream, 4, 12);
    byte[] dataLenBytes = Arrays.copyOfRange(responseStream, 12,RESPONSE_HEAD_LEN);

    ResponseHeader responseHeader = new ResponseHeader();
    responseHeader.sign = MathUtils.byteToInt(signBytes);
    responseHeader.requestId = MathUtils.byteToLong(requestIdBytes);
    responseHeader.dataLen = MathUtils.byteToLong(dataLenBytes);

    return responseHeader;
  }

}
