package com.mashibing.mashibing.system.io.testRPC.client.response;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.response.ResponseBody
 * @Description: 响应体
 * @date 2020/7/23 14:40
 */
public class ResponseBody {

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
}
