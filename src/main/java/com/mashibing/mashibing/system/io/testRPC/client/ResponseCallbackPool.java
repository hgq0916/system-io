package com.mashibing.mashibing.system.io.testRPC.client;

import com.mashibing.mashibing.system.io.testRPC.response.ResponseBean;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.ResponseCallbackPool
 * @Description: 响应池
 * @date 2020/7/23 14:08
 */
public class ResponseCallbackPool {

  private static final ConcurrentHashMap<Long,ResponseCallback> responseCallbackMap = new ConcurrentHashMap<Long,ResponseCallback>();

  public static synchronized  boolean addCallback(Long requestId,ResponseCallback responseCallback){
    ResponseCallback responseCallback1 = responseCallbackMap
        .putIfAbsent(requestId, responseCallback);
    if(responseCallback1 != null) return false;
    return true;
  }

  public static synchronized void exectueCallback(Long requestId, ResponseBean responseBean){
    ResponseCallback responseCallback = responseCallbackMap.get(requestId);
    if(responseCallback == null) throw new IllegalArgumentException("requestId cannot be found:"+requestId);
    responseCallback.callback(responseBean);
    responseCallbackMap.remove(requestId);
  }

}
