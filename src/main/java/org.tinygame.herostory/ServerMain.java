package org.tinygame.herostory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ServerMain
 * @Description TODO
 * @Author zhanghongjun
 * @Date 2020-07-31 1:04
 * @Version 1.0
 */
public class ServerMain {
    public static void main(String[] args) {
        //启动两个线程池
        //bossGroup负责客户端的连接（通过nio），workGroup负责客户端消息读写
        //NioEventLoopGroup运行原理--例子餐馆点餐y
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup =  new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workGroup);
        b.channel(NioServerSocketChannel.class);

       b.childHandler(new ChannelInitializer<SocketChannel>() {
           @Override
           protected void initChannel(SocketChannel socketChannel) throws Exception {
               //pipeline管道
               socketChannel.pipeline().addLast(
                       new HttpServerCodec(),
                       new HttpObjectAggregator(65535),
                       new WebSocketServerProtocolHandler("/websocket"),
                       new GameMsgDecoder(), //自定义消息解码器
                       new GameMsgEncoder(),
                       new GameMsgHandler()
               );
           }
       });
        try {
            ChannelFuture f = b.bind(12345).sync();
            if(f.isSuccess()){
                System.out.println("服务器启动成功");
            }
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
