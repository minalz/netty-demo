package cn.minalz.netty.c5;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler & Pipeline 客户端
 * @author zhouwei
 * @date 2024/6/26 11:42
 */
@Slf4j
public class PipeLineClientDemo {

    public static void main(String[] args) {
        new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost", 8080)
                .addListener((ChannelFutureListener) future -> {
                    future.channel().writeAndFlush("hello, world");
                });
    }
}
