package org.tinygame.herostory;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @ClassName Broadcaster
 * @Description TODO
 * @Author zhanghongjun
 * @Date 2020-08-03 21:46
 * @Version 1.0
 */
public final class Broadcaster {
    /**
     * 私有化类默认构造器
     */
    private Broadcaster() {
    }

    /**
     * 客户端信道数组, 一定要使用 static, 否则无法实现群发
     */
    static private final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 添加信道
     *
     * @param channel
     */
    static public void addChannel(Channel channel) {
        _channelGroup.add(channel);
    }

    /**
     * 移除信道
     *
     * @param channel
     */
    static public void removeChannel(Channel channel) {
        _channelGroup.remove(channel);
    }

    /**
     * 关播消息
     * @param msg
     */
    static public void broadcast(Object msg) {
        if (null == msg) {
            return;
        }

        _channelGroup.writeAndFlush(msg);
    }
}
