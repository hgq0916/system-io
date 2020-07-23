package com.mashibing.mashibing.system.io.testRPC.client;

import com.mashibing.mashibing.system.io.testRPC.client.response.ResponseBean;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.RequestCallback
 * @Description: 响应回调
 * @date 2020/7/23 13:45
 */
public interface ResponseCallback {

  public void callback(ResponseBean responseBean);

}
