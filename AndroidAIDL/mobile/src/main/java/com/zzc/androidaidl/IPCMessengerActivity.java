package com.zzc.androidaidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IPCMessengerActivity extends AppCompatActivity {
    private static final String TAG = "IPCMessengerActivity";

    private Button btnStartService;
    private Button btnSendMsg;
    private Button btnStopService;
    private EditText etMsgContent;
    private TextView tvLog;
    private StringBuilder sbLog;
    /**
     * 连接上服务器后,由服务器返回Binder对象构造出一个用来发送消息的Messenger
     */
    private Messenger mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    private void initView() {
        btnStartService = (Button) findViewById(R.id.btn_start_service);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
        btnSendMsg = (Button) findViewById(R.id.btn_send_msg);
        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMsg();
            }
        });
        btnStopService = (Button) findViewById(R.id.btn_stop_service);
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });
        etMsgContent = (EditText) findViewById(R.id.et_msg_content);
        tvLog = (TextView) findViewById(R.id.tv_log);
    }

    private void initData() {
        sbLog = new StringBuilder();
    }

    private void startService() {
        Intent intent = new Intent(this, MessengerService.class);
        getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void sendMsg() {
        if (mService == null) {
            Toast.makeText(this, "服务未连接!", Toast.LENGTH_SHORT).show();
            return;
        }

        String msgContent = etMsgContent.getText().toString();
        if (TextUtils.isEmpty(msgContent)) {
            Toast.makeText(this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //获取一个新的Message
        Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
        msg.replyTo = mGetReplyMessenger;
        Bundle bundle = new Bundle();
        bundle.putString("msg", msgContent);
        msg.setData(bundle);
        try {
            mService.send(msg);
            sbLog.append("向服务器发送消息:").append(msgContent).append("\n");
            showLog(sbLog.toString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void stopService() {
        if (mService == null) {
            Toast.makeText(this, "服务未连接!", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, "stopService: mConnection:" + mConnection);
        getApplicationContext().unbindService(mConnection);
        mService = null;
        sbLog.append("与服务器断开连接\n");
        showLog(sbLog.toString());
    }

    /**
     * 和服务器的连接
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                Log.i(TAG, "onServiceConnected: " + "服务已连接");
                Log.i(TAG, "onServiceConnected: ComponentName = " + name.getClassName());
                Log.i(TAG, "onServiceConnected: IBinder = " + service.getInterfaceDescriptor().toLowerCase());
                sbLog.append("服务已连接\n");
                showLog(sbLog.toString());

                //拿到服务器的Binder构造一个消息管理器
                mService = new Messenger(service);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceConnected: " + "服务已断开连接");
            Log.i(TAG, "onServiceConnected: ComponentName = " + name.getClassName());
            sbLog.append("服务已断开连接\n");
            showLog(sbLog.toString());
        }
    };

    /**
     * 处理从服务器发送过来的消息
     */
    private class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_FROM_SERVER:
                    String msgFromServer = msg.getData().getString("msg");
                    sbLog.append("收到服务器的消息:").append(msgFromServer).append("\n");
                    showLog(sbLog.toString());
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    private void showLog(String content) {
        tvLog.setText(content);
    }
}
