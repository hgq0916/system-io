package com.mashibing.mashibing.system.io.testRPC.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gangquan.hu
 * @Package: com.mashibing.mashibing.system.io.testRPC.client.ClientFactory
 * @Description: 客户端工厂类
 * @date 2020/7/23 13:35
 */
public class ClientFactory {

  private static final ConcurrentHashMap<InetSocketAddress,ClientPool> clientPoolMap = new ConcurrentHashMap<>();


  public static synchronized NioSocketChannel getClientConnection(InetSocketAddress inetSocketAddress) {

    ClientPool clientPool = clientPoolMap.get(inetSocketAddress);
    if(clientPool == null){
      clientPool = createClientPool(inetSocketAddress);
    }

    return clientPool.getConnection();
  }

  private static ClientPool createClientPool(InetSocketAddress inetSocketAddress) {
    ClientPool clientPool= new ClientPool(1,inetSocketAddress);
    return clientPool;
  }

  /**
   * @author gangquan.hu
   * @Package: com.mashibing.mashibing.system.io.testRPC.client.ClientPool
   * @Description: 客户端连接池
   * @date 2020/7/23 13:30
   */
  static class ClientPool {

    private NioSocketChannel[] clients;
    private Object[] locks;
    private InetSocketAddress inetSocketAddress;

    public ClientPool(int n, InetSocketAddress inetSocketAddress) {

      this.inetSocketAddress = inetSocketAddress;
      this.clients = new NioSocketChannel[n];
      for(int i=0;i<clients.length;i++){
        clients[i] = createClient();
      }
      this.locks = new Object[n];
      for(int i=0;i<locks.length;i++){
        locks[i] = new Object();
      }

    }

    private int next(){
      return new Random().nextInt(clients.length);
    }

    public NioSocketChannel getConnection(){

      int index = next();
      NioSocketChannel nioSocketChannel = clients[index];

      if(nioSocketChannel == null || !nioSocketChannel.isActive()){
        nioSocketChannel = createClient();
        clients[index] = nioSocketChannel;
      }

      return nioSocketChannel;
    }

    public NioSocketChannel createClient() {

      try {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        Bootstrap bs = new Bootstrap();
        ChannelFuture connect = bs.group(nioEventLoopGroup)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<NioSocketChannel>() {
              @Override
              protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                ChannelPipeline pipeline = nioSocketChannel.pipeline();
                pipeline.addLast(new ClientResponseDecoder());
                pipeline.addLast(new ClientResponseHandler());
              }
            })
            .connect(inetSocketAddress);
        connect.sync();
        return (NioSocketChannel)connect.channel();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
     return null;

    }

  }

}
