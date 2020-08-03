package com.mashibing.mashibing.system.io.testRPC.bean;

import java.io.Serializable;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.bean.User
 * @Description: 用户
 * @date 2020/7/23 19:51
 */
public class User implements Serializable {

  private String id;
  private String name;
  private int age;

  public User(){}

  public User(String id, String name, int age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return "User{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", age=" + age +
        '}';
  }
}