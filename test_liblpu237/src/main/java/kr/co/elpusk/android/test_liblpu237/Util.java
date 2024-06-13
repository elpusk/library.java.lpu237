package kr.co.elpusk.android.test_liblpu237;

import static androidx.core.content.ContextCompat.registerReceiver;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;

import kr.co.elpusk.android.liblpu237.UsbDevHandle;

public class Util {
    static public boolean sendRequstPermission(Context ctx, String sAction,UsbDevHandle h){
        boolean b_result = false;

        do{
            if(h==null){
                continue;
            }
            if(h.is_empty()){
                continue;
            }
            if(ctx==null){
                continue;
            }
            UsbManager usbManager = (UsbManager) ctx.getSystemService(Context.USB_SERVICE);

            @SuppressLint("MutableImplicitPendingIntent") PendingIntent pendingIntent =
                    PendingIntent.getBroadcast(
                            ctx
                            , 0
                            , new Intent(sAction)
                            , PendingIntent.FLAG_MUTABLE
                    );
            usbManager.requestPermission(h.get_usbdevice(), pendingIntent);

            b_result = true;
        }while (false);
        return b_result;
    }

    static public boolean sendBroadercast(Context ctx, String sAction,String sKey,String sData){
        boolean b_result = false;
        do{
            if(ctx == null){
                continue;
            }
            if(sAction == null || sAction.isEmpty()){
                continue;
            }
            if(sKey==null || sKey.isEmpty()){
                continue;
            }
            if(sData==null){
                continue;
            }
            Intent intent = new Intent(sAction );
            intent.putExtra(sKey, sData);
            ctx.sendBroadcast(intent);
            //
            b_result = true;
        }while (false);
        return b_result;
    }

    static public  void registerIntentFilter(Context ctx,BroadcastReceiver rcv, String[] arryActions){
        if(arryActions!=null && arryActions.length>0 ) {
            IntentFilter filter = new IntentFilter();

            for(int i=0; i<arryActions.length; i++ ) {
                filter.addAction(arryActions[i]);
            }
            ctx.registerReceiver(rcv, filter);
        }

    }
}
