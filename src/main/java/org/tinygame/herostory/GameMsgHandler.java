package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.cmdHandle.*;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName GameMsgHandler
 * @Description TODO
 * @Author zhanghongjun
 * @Date 2020-07-31 23:58
 * @Version 1.0
 */
public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Broadcaster.addChannel(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        Broadcaster.removeChannel(ctx.channel());
        // 先拿到用户 Id
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }
        UserManager.remove(userId);
        GameMsgProtocol.UserQuitResult.Builder resultBuilder = GameMsgProtocol.UserQuitResult.newBuilder();
        resultBuilder.setQuitUserId(userId);
        GameMsgProtocol.UserQuitResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到客户端消息，msgClazz=" + msg.getClass().getName() + "msg=" + msg);
        ICmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.create(msg.getClass());
        if (cmdHandler != null) {
            cmdHandler.handle(ctx, cast(msg));
        }
    }

    /**
     * 消息对象转型
     * @param msg
     * @param <TCmd>
     * @return
     */
    static private <TCmd extends GeneratedMessageV3> TCmd cast(Object msg) {
        if(null==msg){
            return null;
        }else {
            return (TCmd) msg;
        }
    }
}
