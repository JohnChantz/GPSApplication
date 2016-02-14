package gr.hua.android.gpsapplication;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.Timer;

public class MyService extends Service {
    Timer mTimer = new Timer();
    String userLocation;
    long period = 30000;
    long delay = 5000;
    UpdateLocation foo = new UpdateLocation();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {

//        UpdateLocation foo = new UpdateLocation();
//        mTimer.scheduleAtFixedRate(foo, delay, period);

        Toast.makeText(getApplicationContext(), "Service Started!", Toast.LENGTH_SHORT).show();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 0;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = Double.toString(location.getLatitude()) + "_" + Double.toString(location.getLongitude());
//                Intent i = new Intent();
//                i.putExtra("location", userLocation);
                System.out.println("Location Changed!");
                System.out.println(userLocation);
//                foo.run();
            }

            @Override       //not used
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override       //not used
            public void onProviderEnabled(String provider) {
                Toast.makeText(MyService.this, "GPS enabled " + provider, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(MyService.this, "GPS disabled " + provider, Toast.LENGTH_SHORT).show();

            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(MyService.this, "Service Destroyed", Toast.LENGTH_SHORT).show();
//        mTimer.cancel();
    }
}
