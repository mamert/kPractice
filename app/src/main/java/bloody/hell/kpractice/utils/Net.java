package bloody.hell.kpractice.utils;

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

}
