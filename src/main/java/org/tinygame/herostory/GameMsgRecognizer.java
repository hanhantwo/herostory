package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName GameMsgRecognizer
 * @Description 消息识别器
 * @Author zhanghongjun
 * @Date 2020-08-03 23:44
 * @Version 1.0
 */
public final class GameMsgRecognizer {
    /**
     * 私有化
     */
    private GameMsgRecognizer() {
    }

    /**
     * 消息代码和消息体字典
     */
    static private final Map<Integer, GeneratedMessageV3> _msgCodeAndMsgBodyMap = new HashMap<>();
    /**
     *消息类型和消息编号字典
     */
    static private final Map<Class<?>, Integer> _msgClassAndMsgCodeMap = new HashMap<>();


    static public void init() {
        Class<?>[] innerClazzArray = GameMsgProtocol.class.getDeclaredClasses();

        for (Class<?> innerClazz : innerClazzArray) {
            if (!GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {
                continue;
            }

            String clazzName = innerClazz.getSimpleName();
            clazzName = clazzName.toLowerCase();

            for (GameMsgProtocol.MsgCode msgCode : GameMsgProtocol.MsgCode.values()) {
                String strMsgCode = msgCode.name();
                strMsgCode = strMsgCode.replaceAll("_", "");
                strMsgCode = strMsgCode.toLowerCase();

                if (!strMsgCode.startsWith(clazzName)) {
                    continue;
                }

                try {
                    Object returnObj = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);

                    System.out.println("{} <==> {}," +innerClazz.getName()+","+msgCode.getNumber());

                    _msgCodeAndMsgBodyMap.put(
                            msgCode.getNumber(),
                            (GeneratedMessageV3) returnObj
                    );

                    _msgClassAndMsgCodeMap.put(
                            innerClazz,
                            msgCode.getNumber()
                    );
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
//        _msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE, GameMsgProtocol.UserEntryCmd.getDefaultInstance());
//        _msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE, GameMsgProtocol.WhoElseIsHereCmd.getDefaultInstance());
//        _msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE, GameMsgProtocol.UserMoveToCmd.getDefaultInstance());
//
//        _msgClassAndMsgCodeMap.put(GameMsgProtocol.UserEntryResult.class,GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
//        _msgClassAndMsgCodeMap.put(GameMsgProtocol.WhoElseIsHereResult.class,GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
//        _msgClassAndMsgCodeMap.put(GameMsgProtocol.UserMoveToResult.class,GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
//        _msgClassAndMsgCodeMap.put(GameMsgProtocol.UserQuitResult.class,GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE);

    }

    static public Message.Builder getBuilderByMsgCode(int msgCode) {
        if (msgCode < 0) {
            return null;
        }
        GeneratedMessageV3 msg = _msgCodeAndMsgBodyMap.get(msgCode);

        if (msg == null) {
            return null;
        }
        return msg.newBuilderForType();
    }

    static public int getMsgCodeByMsgClazz(Class<?> msgClazz) {
        if(msgClazz==null){
            return -1;
        }
        Integer msgCode = _msgClassAndMsgCodeMap.get(msgClazz);
        if(msgCode==null){
            return -1;
        }
        return msgCode;
    }
}
