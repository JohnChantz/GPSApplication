package gr.hua.android.gpsapplication;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimerTask;

public class UpdateLocation extends TimerTask {

    private String locationCoordinates;

    public UpdateLocation() {
    }

    @Override
    public void run() {
        new LocationUpdater().execute("");
    }

    public void setLocationCoordinates(String location) {
        this.locationCoordinates = location;
    }

    private class LocationUpdater extends AsyncTask<String, String, String> {
        static final String SERVER_URL = "http://10.0.2.2/android/locationsApi.php";        //emulator reroutes to computer's localhost when it sees 10.0.2.2
        static final String REQUEST_METHOD = "POST";
        HttpURLConnection conn = null;

        @Override
        protected String doInBackground(String... params) {
            try {
                this.updateUserLocation();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "DONE";
        }

        protected void onPostExecute(String status) {
            System.out.println(status);
        }

        public void updateUserLocation() throws IOException {

            String userId = "userIdTest";
            String username = "usernameTest";

            Map<String, Object> params = new LinkedHashMap<>();
            params.put("userId", userId);
            params.put("username", username);
            params.put("location", locationCoordinates);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            URL url = null;
            try {
                url = new URL(SERVER_URL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            try {
                conn.setRequestMethod(REQUEST_METHOD);
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            try {
                conn.getOutputStream().write(postDataBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.err.println(conn.getResponseCode());
            try {
                conn.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

