package org.tinygame.herostory.cmdHandle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.model.User;
import org.tinygame.herostory.model.UserManager;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * @ClassName UserEntityCmdHandler
 * @Description TODO
 * @Author zhanghongjun
 * @Date 2020-08-03 22:16
 * @Version 1.0
 */
public class UserEntityCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd> {

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd msg) {
        // 从指令对象中获取用户 Id 和英雄形象
        GameMsgProtocol.UserEntryCmd cmd = msg;
        int userId = cmd.getUserId();
        String heroAvatar = cmd.getHeroAvatar();

        GameMsgProtocol.UserEntryResult.Builder resultBuilder = GameMsgProtocol.UserEntryResult.newBuilder();
        resultBuilder.setUserId(userId);
        resultBuilder.setHeroAvatar(heroAvatar);

        // 将用户加入字典
        User newUser = new User();
        newUser.userId = userId;
        newUser.heroAvatar = heroAvatar;

        UserManager.add(newUser);

        // 将用户 Id 附着到 Channel
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);


        // 构建结果并发送
        GameMsgProtocol.UserEntryResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}
