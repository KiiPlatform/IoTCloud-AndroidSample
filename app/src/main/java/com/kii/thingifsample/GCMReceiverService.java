package com.kii.thingifsample;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kii.cloud.storage.PushMessageBundleHelper;
import com.kii.cloud.storage.ReceivedMessage;
import com.kii.thingifsample.utils.Utils;

public class GCMReceiverService extends IntentService {
    private Handler handler;
    public GCMReceiverService() {
        super("GCMReceiverService");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.handler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            String messageType = gcm.getMessageType(intent);
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                final Bundle extras = intent.getExtras();
                final ReceivedMessage message = PushMessageBundleHelper.parse(extras);
                PushMessageBundleHelper.MessageType type = message.pushMessageType();
                final String msg = Utils.bundleToJson(message.getMessage()).toString();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Received a push notification msg=" + msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        } finally {
            PushBroadcastReceiver.completeWakefulIntent(intent);
        }
    }
}
