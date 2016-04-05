package com.zzc.androidaidl;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 运行在单独进程的服务端
 * <p/>
 * Created by zczhang on 16/4/2.
 */
public class MessengerService extends Service {

    private static final String TAG = "MessengerService";

    /**
     * 服务端用来处理消息的Handler
     */
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_FROM_CLIENT:
                    Log.i(TAG, "handleMessage: receive msg from client:" + msg.getData().getString("msg"));
                    replyAutoMsgToClient(msg.replyTo);
                    break;
                default:
                    super.handleMessage(msg);

            }
        }
    }

    /**
     * 进程间通信的消息管理器,其实现是对Binder的包装.
     * 当有客户端连接时,返回Messenger中持有的Binder对象,实现跨进程通信
     */
    private final Messenger mMessenger = new Messenger(new MessengerHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    /**
     * 向客户端回复自动消息
     * @param messenger
     */
    private static void replyAutoMsgToClient(Messenger messenger) {
        Message msg = Message.obtain(null, Constant.MSG_FROM_SERVER);
        Bundle bundle = new Bundle();
        bundle.putString("msg", "恩,你的消息我已经收到,稍后回复你!");
        msg.setData(bundle);
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
