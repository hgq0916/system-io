package com.mashibing.mashibing.system.io.testRPC.response;

import com.mashibing.mashibing.system.io.testRPC.request.RequestHeader;
import java.io.Serializable;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.response.ResponseBody
 * @Description: 响应体
 * @date 2020/7/23 14:40
 */
public class ResponseBody implements Serializable {

  public ResponseBody(Object content) {
    this.content = content;
  }

  private Object content;

  public Object getContent() {
    return content;
  }

  public void setContent(Object content) {
    this.content = content;
  }

  @Override
  public String toString() {
    return "ResponseBody{" +
        "content=" + content +
        '}';
  }

}
