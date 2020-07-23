package com.mashibing.mashibing.system.io.testRPC.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.server.Server
 * @Description: 服务端
 * @date 2020/7/23 16:09
 */
public class Server {

  public void start() throws Exception {
    NioEventLoopGroup group = new NioEventLoopGroup(2);
    ServerBootstrap sb = new ServerBootstrap();
    ChannelFuture bind = sb.group(group, group)//混杂模式
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<NioSocketChannel>() {
          @Override
          protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
            ChannelPipeline pipeline = nioSocketChannel.pipeline();
            pipeline.addLast(new ClientRequestHandler());
          }
        })
        .bind("192.168.68.1", 9090);
    ChannelFuture channelFuture = bind.sync();
    System.out.println("服务端启动...");
    channelFuture.channel().closeFuture().sync();
  }

}
