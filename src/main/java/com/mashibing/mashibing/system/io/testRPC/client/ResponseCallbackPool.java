package com.mashibing.mashibing.system.io.testRPC.client;

import com.mashibing.mashibing.system.io.testRPC.response.ResponseBean;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.ResponseCallbackPool
 * @Description: 响应池
 * @date 2020/7/23 14:08
 */
public class ResponseCallbackPool {

  //方式一
 /* private static final ConcurrentHashMap<Long,ResponseCallback> responseCallbackMap = new ConcurrentHashMap<Long,ResponseCallback>();

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
  }*/

 //方式二
 private static final ConcurrentHashMap<Long, CompletableFuture> responseCallbackMap = new ConcurrentHashMap<>();

  public static synchronized  boolean addCallback(Long requestId,CompletableFuture completableFuture){
    CompletableFuture completableFuture1 = responseCallbackMap
        .putIfAbsent(requestId, completableFuture);
    if(completableFuture1 != null) return false;
    return true;
  }

  public static synchronized void exectueCallback(Long requestId, ResponseBean responseBean){
    CompletableFuture completableFuture = responseCallbackMap.get(requestId);
    if(completableFuture == null) throw new IllegalArgumentException("requestId cannot be found:"+requestId);
    completableFuture.complete(responseBean);
    responseCallbackMap.remove(requestId);
  }

}
