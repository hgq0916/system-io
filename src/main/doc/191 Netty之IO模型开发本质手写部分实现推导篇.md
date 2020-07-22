### netty

##### ByteBuf

Netty中的bytebuf相当于Java的byteBuffer

##### NioEventLoopGroup

1.线程池

##### NioSocketChannel

开启一个服务端，

```shell
[root@node1 ~]# nc -l 192.168.25.66 9090
```

1.创建客户端,处理read事件

2.bootstrap创建客户端

##### NioServerSocketChannel

1.创建服务端,处理accept事件和read事件

2.使用InitHandler把用户的自定义handler传递给channel,避免用户的handler需要使用@ChannelHandler.Sharable

2.bootstrap创建服务端

