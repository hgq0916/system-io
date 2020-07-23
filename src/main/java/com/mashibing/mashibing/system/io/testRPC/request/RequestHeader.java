package com.mashibing.mashibing.system.io.testRPC.request;

import com.mashibing.mashibing.system.io.testRPC.response.ResponseHeader;
import com.mashibing.mashibing.system.io.testRPC.utils.MathUtils;
import java.util.Arrays;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.request.RequestHeader
 * @Description: 请求头
 * @date 2020/7/23 11:59
 */
public class RequestHeader {

  public static final int REQUEST_HEAD_LEN = 20;
  private static final int DEFAULT_REQUEST_SIGN = 0x14141414;

  private int sign;//标志位

  private long requestId;//请求id

  private long dataLen;//数据长度

  public RequestHeader(){}

  public RequestHeader(long requestId, int dataLen) {
    this.requestId = requestId;
    this.dataLen = dataLen;
    this.sign = DEFAULT_REQUEST_SIGN;
  }

  public static RequestHeader deserialize(byte[] stream) {
    if(stream == null || stream.length< RequestHeader.REQUEST_HEAD_LEN) throw new IllegalStateException("requestStream invalid");
    byte[] signBytes = Arrays.copyOfRange(stream, 0, 4);
    byte[] requestIdBytes = Arrays.copyOfRange(stream, 4, 12);
    byte[] dataLenBytes = Arrays.copyOfRange(stream, 12,REQUEST_HEAD_LEN);

    RequestHeader requestHeader = new RequestHeader();
    requestHeader.sign = MathUtils.byteToInt(signBytes);
    requestHeader.requestId = MathUtils.byteToLong(requestIdBytes);
    requestHeader.dataLen = MathUtils.byteToLong(dataLenBytes);

    return requestHeader;
  }

  public int getSign() {
    return sign;
  }

  public void setSign(int sign) {
    this.sign = sign;
  }

  public long getRequestId() {
    return requestId;
  }

  public void setRequestId(long requestId) {
    this.requestId = requestId;
  }

  public long getDataLen() {
    return dataLen;
  }

  public void setDataLen(long dataLen) {
    this.dataLen = dataLen;
  }

  public byte[] serialize(){

    byte[] headBytes = new byte[REQUEST_HEAD_LEN];

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
    return "RequestHeader{" +
        "sign=" + sign +
        ", requestId=" + requestId +
        ", dataLen=" + dataLen +
        '}';
  }
}
