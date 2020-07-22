package com.mashibing.mashibing.system.io.testNetty03;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import org.junit.Test;

/**
 * netty api测试
 */
public class MyNetty {

  /**
   * ByteBuf
   */
  @Test
  public void ByteBufTest(){
    //ByteBuf byteBuf = ByteBufAllocator.DEFAULT.directBuffer(8,20);//int initialCapacity, int maxCapacity
    //ByteBuf byteBuf = ByteBufAllocator.DEFAULT.heapBuffer(8,20);//int initialCapacity, int maxCapacity
    //ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.directBuffer(8,20);//int initialCapacity, int maxCapacity
    ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.heapBuffer(8,20);//int initialCapacity, int maxCapacity
    printByteBuf(byteBuf);
    byteBuf.writeBytes(new byte[]{1,2,3,4});
    printByteBuf(byteBuf);
    byteBuf.writeBytes(new byte[]{1,2,3,4});
    printByteBuf(byteBuf);
    byteBuf.writeBytes(new byte[]{1,2,3,4});
    printByteBuf(byteBuf);
    byteBuf.writeBytes(new byte[]{1,2,3,4});
    printByteBuf(byteBuf);
    byteBuf.writeBytes(new byte[]{1,2,3,4});
    printByteBuf(byteBuf);
    /*byteBuf.writeBytes(new byte[]{1,2,3,4});
    printByteBuf(byteBuf);*/
  }

  private void printByteBuf(ByteBuf byteBuf) {
    System.out.println("byteBuf.readerIndex():"+byteBuf.readerIndex());
    System.out.println("byteBuf.readableBytes():"+byteBuf.readableBytes());
    System.out.println("byteBuf.capacity():"+byteBuf.capacity());
    System.out.println("byteBuf.maxCapacity():"+byteBuf.maxCapacity());
    System.out.println("byteBuf.writerIndex():"+byteBuf.writerIndex());
    System.out.println("byteBuf.writableBytes():"+byteBuf.writableBytes());
    System.out.println("byteBuf.isDirect():"+byteBuf.isDirect());
    System.out.println("byteBuf.isReadable():"+byteBuf.isReadable());
    System.out.println("byteBuf.isWritable():"+byteBuf.isWritable());
    System.out.println("============================");
  }

  /**
   * NioEventLoopGroup
   */
  @Test
  public void NioEventLoopGroupThreadTest(){
//    NioEventLoopGroup group = new NioEventLoopGroup(1);
    NioEventLoopGroup group = new NioEventLoopGroup(2);
    group.execute(()->{//执行线程的代码
      while (true){
        System.out.println("hello world001");
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });

    group.execute(()->{//执行线程的代码
      while (true){
        System.out.println("hello world002");
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    try {
      System.in.read();//阻塞，防止主线程结束
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * NioSocketChannel 客户端
   */
  @Test
  public void ClientTest() throws Exception {
    NioEventLoopGroup group = new NioEventLoopGroup(1);//相当于selector
    NioSocketChannel client = new NioSocketChannel();
    //注册到group
    ChannelFuture channelFuture = group.register(client);
    //给客户端添加响应处理
    ChannelPipeline pipeline = client.pipeline();
    pipeline.addLast(new MyInHandler());
    channelFuture.sync();
    ChannelFuture connect = client.connect(new InetSocketAddress("192.168.25.66", 9090));
    connect.sync();
    System.out.println("client connected");
    ByteBuf byteBuf = Unpooled.copiedBuffer("hello server".getBytes());
    client.writeAndFlush(byteBuf);
    ChannelFuture closeFuture =client.closeFuture();
    closeFuture.sync();
  }

  /**
   * 服务端 NioServerSocketChannel
   */
  @Test
  public void serverTest() throws Exception {
    NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(3);
    NioServerSocketChannel serverSocketChannel = new NioServerSocketChannel();

    ChannelPipeline pipeline = serverSocketChannel.pipeline();
    pipeline.addLast(new MyAcceptHandler(nioEventLoopGroup,new MyInitHandler()));
    nioEventLoopGroup.register(serverSocketChannel);//注册到selector
    ChannelFuture bind = serverSocketChannel.bind(new InetSocketAddress("192.168.25.1", 9090));
    bind.sync();//绑定阻塞
    ChannelFuture closeFuture = serverSocketChannel.closeFuture();
    closeFuture.sync();
  }

  class MyAcceptHandler extends ChannelInboundHandlerAdapter {

    private final NioEventLoopGroup nioEventLoopGroup;
    private final ChannelHandler handler;


    public MyAcceptHandler(NioEventLoopGroup nioEventLoopGroup,
        ChannelHandler handler) {
      this.nioEventLoopGroup = nioEventLoopGroup;
      this.handler = handler;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
      System.out.println("server registered");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      //接收到一个客户端
      System.out.println("接收到一个客户端:"+msg);
      NioSocketChannel socketChannel = (NioSocketChannel) msg;
      nioEventLoopGroup.register(socketChannel);
      ChannelPipeline pipeline = socketChannel.pipeline();
      pipeline.addLast(handler);
    }
  }

  class MyInitHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
      Channel channel = ctx.channel();
      ChannelPipeline pipeline = channel.pipeline();
      pipeline.addLast(new MyInHandler());
      pipeline.remove(this);//把MyInitHandler移除
    }
  }

  class MyInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
      System.out.println("channelRegistered");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      //读事件
      ByteBuf byteBuf = (ByteBuf) msg;
      System.out.println(byteBuf.getCharSequence(0,byteBuf.readableBytes(), Charset.forName("utf8")));
      Channel channel = ctx.channel();
      channel.writeAndFlush(msg);
    }


  }
}
