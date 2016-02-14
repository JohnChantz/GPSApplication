package gr.hua.android.gpsapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            Intent i = new Intent(context, MyService.class);
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (statusOfGPS) {
                System.out.println("Starting Services");
                context.startService(i);
            } else {
                System.out.println("Stopping Services");
                context.stopService(i);
            }
        }
    }
}