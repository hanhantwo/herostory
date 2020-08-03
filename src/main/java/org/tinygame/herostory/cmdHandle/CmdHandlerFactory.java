package org.tinygame.herostory.cmdHandle;

import com.google.protobuf.GeneratedMessageV3;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CmdHandlerFactory
 * @Description 指令处理器工厂
 * @Author zhanghongjun
 * @Date 2020-08-03 22:53
 * @Version 1.0
 */
public final class CmdHandlerFactory {
    static private Map<Class<?>,ICmdHandler<? extends GeneratedMessageV3>> _handlerMap= new HashMap<>();

    /**
     * 私有化类构造器
     */
    private CmdHandlerFactory(){

    }

    static public void init(){
        _handlerMap.put(GameMsgProtocol.UserEntryCmd.class,new UserEntityCmdHandler() );
        _handlerMap.put(GameMsgProtocol.WhoElseIsHereCmd.class,new WhoElseIsHereCmdHandler());
        _handlerMap.put(GameMsgProtocol.UserMoveToCmd.class,new UserMoveToCmdHandler());
    }

    static public ICmdHandler<? extends GeneratedMessageV3> create(Class<?> msgClazz){
        if (msgClazz==null) {
            return null;
        }
        return _handlerMap.get(msgClazz);
    }
}
