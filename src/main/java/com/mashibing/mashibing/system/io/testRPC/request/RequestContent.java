package com.mashibing.mashibing.system.io.testRPC.request;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.request.RequestContent
 * @Description: 请求内容
 * @date 2020/7/23 11:50
 */
public class RequestContent implements Serializable {

  private String serverName;//服务名称

  private String methodName;//方法名

  private Class<?>[] parameterTypes;//方法参数类型列表

  private Object[] args;//方法参数

  public String getServerName() {
    return serverName;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public Class<?>[] getParameterTypes() {
    return parameterTypes;
  }

  public void setParameterTypes(Class<?>[] parameterTypes) {
    this.parameterTypes = parameterTypes;
  }

  public Object[] getArgs() {
    return args;
  }

  public void setArgs(Object[] args) {
    this.args = args;
  }

  @Override
  public String toString() {
    return "RequestContent{" +
        "serverName='" + serverName + '\'' +
        ", methodName='" + methodName + '\'' +
        ", parameterTypes=" + Arrays.toString(parameterTypes) +
        ", args=" + Arrays.toString(args) +
        '}';
  }

}
