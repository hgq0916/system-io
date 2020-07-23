package com.mashibing.mashibing.system.io.testRPC.client.request;

import com.mashibing.mashibing.system.io.testRPC.utils.MathUtils;
import java.io.Serializable;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.request.RequestHeader
 * @Description: 请求头
 * @date 2020/7/23 11:59
 */
public class RequestHeader {

  public static final int REQUEST_HEAD_LEN = 20;

  private int sign;//标志位

  private long requestId;//请求id

  private long dataLen;//数据长度

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

}
