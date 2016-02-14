package gr.hua.android.gpsapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetLocations {

    ArrayList<Location> locations = new ArrayList<>();


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
            } else {
                for (Location loc : locations) {
                    System.out.println(loc.getUserID());
                    System.out.println(loc.getUsername());
                    System.out.println(loc.getCurrent_location());
                }
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
