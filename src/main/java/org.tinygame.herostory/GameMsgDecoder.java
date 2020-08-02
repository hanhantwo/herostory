package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @ClassName GameMsgDecoder
 * @Description 解码器
 * @Author zhanghongjun
 * @Date 2020-08-02 11:02
 * @Version 1.0
 */
public class GameMsgDecoder extends ChannelInboundHandlerAdapter{
    @Override
    public void channelRead(ChannelHandlerContext context,Object msg) throws Exception{
        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }
        // WebSocket 二进制消息会通过 HttpServerCodec 解码成 BinaryWebSocketFrame 类对象
        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();

        byteBuf.readShort(); // 读取消息的长度
        int msgCode = byteBuf.readShort(); // 读取消息的编号

        // 拿到消息体
        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        GeneratedMessageV3 cmd = null;

        switch (msgCode) {
            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                cmd = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
                break;

            case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                cmd = GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
                break;

            case GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                cmd = GameMsgProtocol.UserMoveToCmd.parseFrom(msgBody);
                break;
        }

        if (null != cmd) {
            context.fireChannelRead(cmd);
        }
    }
}
