package gr.hua.android.gpsapplication;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetLocationService extends Service {

    Uri uri = Uri.parse("content://gr.hua.android.gpsapplication/locations/add");
    ContentValues values = new ContentValues();
    ArrayList<Location> locations = new ArrayList<>();
    LocationUpdater locationUpdater = new LocationUpdater();

    @Nullable
    @Override       //not used
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        LocationUpdater locationUpdater = new LocationUpdater();
        locationUpdater.execute("");
//        GetLocations getLocations = new GetLocations();
//        getLocations.executeRequest();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void addToDataProvider() {
        for (Location loc : locations) {
            values.put(DBHelper.USERID, loc.getUserID());
            values.put(DBHelper.USERNAME, loc.getUsername());
            values.put(DBHelper.LOCATION, loc.getCurrent_location());
        }
        this.getContentResolver().insert(uri, values);
    }

    public void executeRequest() {
        locationUpdater.execute();
    }

    private class LocationUpdater extends AsyncTask<String, String, String> {

        static final String SERVER_URL = "http://10.0.2.2/android/locationsApi.php";        //emulator reroutes to computer's localhost when it sees 10.0.2.2
        static final String REQUEST_METHOD = "GET";
        HttpURLConnection conn = null;
        private XmlPullParserFactory xmlFactoryObject;
        private XmlPullParser xmlParser;

        @Override
        protected String doInBackground(String... params) {
            try {
                this.getLocations();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "DONE";
        }

        protected void onPostExecute(String status) {
            System.out.println(status);
            if (locations.isEmpty()) {
                System.out.println("Empty");
                return;
            } else {
                System.out.println("Adding to Data Provider");
                addToDataProvider();
            }
        }

        public void getLocations() throws IOException {
            try {
                URL url = new URL(SERVER_URL);
                conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod(REQUEST_METHOD);
                conn.setDoInput(true);
                conn.connect();

                InputStream stream = conn.getInputStream();
                xmlFactoryObject = XmlPullParserFactory.newInstance();
                xmlParser = xmlFactoryObject.newPullParser();

                xmlParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                xmlParser.setInput(stream, null);

                this.xmlParse(xmlParser);       //after download is complete, start parsing
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void xmlParse(XmlPullParser xmlParser) {
            Log.d("xmlHandler", "parseXML running!");
            String name = "name";
            int eventType = 0;
            Location loc = null;
            try {
                eventType = xmlParser.getEventType();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("Start document");
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        name = xmlParser.getName();
                        if (name.equals("location"))
                            loc = new Location();
                    } else if (eventType == XmlPullParser.TEXT) {
                        switch (name) {
                            case "userID":
                                loc.setUserID(xmlParser.getText());
                                break;
                            case "username":
                                loc.setUsername(xmlParser.getText());
                                break;
                            case "currentLocation":
                                loc.setCurrent_location(xmlParser.getText());
                                break;
                            default:
                                break;
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        name = xmlParser.getName();
                        if (name.equals("location"))
                            locations.add(loc);
                    }
                    eventType = xmlParser.next();
                }
                System.out.println("End document");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
