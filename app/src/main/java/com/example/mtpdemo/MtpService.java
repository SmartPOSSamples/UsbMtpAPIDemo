package com.example.mtpdemo;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.mtpdemo.control.IAIDLListener;
import com.example.mtpdemo.control.SystemExtApiController;
import com.wizarpos.wizarviewagentassistant.aidl.ISystemExtApi;

public class MtpService extends Service implements IAIDLListener {

    private final static String TAG = "MtpService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Run enable mtp mode...");
        modifyPassword();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void serviceConnected(Object objService, ServiceConnection connection, int cmd) {
        try {
            if (objService instanceof ISystemExtApi) {
                ISystemExtApi systemExtApi = (ISystemExtApi) objService;
                if (cmd == 1) {
                    try {
                        systemExtApi.enableMtp(true);
                        boolean isSuccess = systemExtApi.getMtpStatus();
                        Log.i(TAG, "enable MTP mode result :" + (isSuccess ? "Success" : "failed"));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                this.unbindService(connection);
            }
        }
    }

    public void modifyPassword() {
        SystemExtApiController.getInstance().startConnectService(this, this, 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MtpService destroyed");
    }
}
