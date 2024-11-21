package com.example.mtpdemo.control;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.wizarpos.wizarviewagentassistant.aidl.ISystemExtApi;

public class SystemExtApiController {


    private static final String TAG = "mtpService";

    private static int cmd = 1;

    private ISystemExtApi systemExtApi;
    private SystemExtApiServiceConnection connection = null;
    private static SystemExtApiController instance;
    private IAIDLListener aidlListener;

    public static SystemExtApiController getInstance() {
        if (instance == null) {
            instance = new SystemExtApiController();
        }
        return instance;
    }

    private SystemExtApiController() {
        Log.d(TAG, "create ModifyAdminPwdController");
    }

    private class SystemExtApiServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(TAG, "onServiceConnected begin ");
            systemExtApi = ISystemExtApi.Stub.asInterface(service);
            aidlListener.serviceConnected(systemExtApi, connection, cmd);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            systemExtApi = null;
        }
    }

    public boolean startConnectService(Context mContext, IAIDLListener aidlListener, int cmd) {
        this.aidlListener = aidlListener;
        this.cmd = cmd;
        Intent intent = new Intent();
        ComponentName comp = new ComponentName(
                "com.wizarpos.wizarviewagentassistant",
                "com.wizarpos.wizarviewagentassistant.SystemExtApiService");
        intent.setComponent(comp);
        connection = new SystemExtApiServiceConnection();
        mContext.startService(intent);
        boolean isSuccess = mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "invoke SystemExtApiController start service method! isSuccess = " + isSuccess);
        return isSuccess;
    }
}
