package com.mashibing.mashibing.system.io.testRPC.response;

import com.mashibing.mashibing.system.io.testRPC.utils.MathUtils;
import java.util.Arrays;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.response.ResponseHeader
 * @Description: 响应头
 * @date 2020/7/23 14:40
 */
public class ResponseHeader {

  public static final int RESPONSE_HEAD_LEN = 20;
  public static final int DEFAULT_REQUEST_SIGN = 0x14141414;

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

  public ResponseHeader(){}

  public ResponseHeader(long requestId,long dataLen){
    this.sign = DEFAULT_REQUEST_SIGN;
    this.requestId = requestId;
    this.dataLen = dataLen;
  }

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

  public byte[] serialize() {
    byte[] headBytes = new byte[RESPONSE_HEAD_LEN];

    byte[] signBytes = MathUtils.intToByte(sign);
    byte[] requestIdBytes = MathUtils.longToByte(requestId);
    byte[] dataLenBytes = MathUtils.longToByte(dataLen);

    int index = 0;
    System.arraycopy(signBytes,0,headBytes,index,signBytes.length);
    index += signBytes.length;
    System.arraycopy(requestIdBytes,0,headBytes,index,requestIdBytes.length);
    index += requestIdBytes.length;
    System.arraycopy(dataLenBytes,0,headBytes,index,dataLenBytes.length);

    return headBytes;
  }

  @Override
  public String toString() {
    return "ResponseHeader{" +
        "sign=" + sign +
        ", requestId=" + requestId +
        ", dataLen=" + dataLen +
        '}';
  }
}
