package bloody.hell.kpractice.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Net {
    private static final String TAG = "Net util";

    /**
     * Great Azathoth, it's impossible to simply PING a site!
     * InetAddress.isReachable will often return false for no reason;
     * /system/bin/ping might not be available...
     *
     * ended up using this: https://stackoverflow.com/questions/6493517/detect-if-android-device-has-internet-connection/25816086#25816086
     */
    public static void checkInternet() throws IOException {
//        InetAddress ina = InetAddress.getByAddress(new byte[]{8, 8, 4, 4});
//        if(!ina.isReachable(3000))
//            throw new RuntimeException("Network N/A");
        HttpURLConnection urlc = (HttpURLConnection)
                (new URL("http://clients3.google.com/generate_204") // 204: no content. Maybe http://g.cn/generate_204 instead
                        .openConnection());
        urlc.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android; CAM-L21 Build/HUAWEICAM-L21; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36, can't simply ping, InetAddress.isReachable is broken");
        urlc.setRequestProperty("Connection", "close");
        urlc.setConnectTimeout(3000);
        urlc.connect();
        if (urlc.getResponseCode() != 204 ||
                urlc.getContentLength() != 0)
            throw new RuntimeException("Network N/A");
    }

    /**
     * Checks whether the device is connected to the LTE network. Also checks if
     * LTE is the active connection, not the WiFi.
     * @return true if the device is connected to the LTE network.
     */
    public static boolean isLTEConnected(final Context context) {
        boolean DEBUG = false;
        if (!DEBUG) {
            try {
                ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                boolean notWifi = connectivity.getActiveNetworkInfo().getType() != ConnectivityManager.TYPE_WIFI;
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                return notWifi && tm.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE;
            } catch (Exception e) {
                return false;
            }
        } else {
            return true;
        }
    }

    /** Checks whether WiFi connection is available.
     *  @return true if the device is connected to wifi network.
     */
    public static boolean isWifiAvailable(final Context context) {
        try {
            WifiManager wm = (WifiManager) context.getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            int netId = wm.getConnectionInfo().getNetworkId();
            return netId != -1;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks whether 3G connection is available.
     *
     * @param context
     * @return true if 3G connection is active.
     */
    public static boolean is3GConnected(final Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo network = connectivity.getActiveNetworkInfo();
            boolean notWifi = network.getType() != ConnectivityManager.TYPE_WIFI
                    && network.isConnected();
            return notWifi;
        } catch (Exception e) {
            return false;
        }
    }

}
