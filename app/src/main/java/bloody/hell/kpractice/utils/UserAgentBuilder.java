package bloody.hell.kpractice.utils;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * simplified and modified (app label) from https://gist.github.com/abombss/844325
 */


public class UserAgentBuilder{
    private static String userAgentString = null;

    public static String getUserAgentString(Context c) {
        if (userAgentString == null) {
            // build
            String appLabel = "";
            String appVersion = "";
            int height = 0;
            int width = 0;
            DisplayMetrics display = c.getResources().getDisplayMetrics();
            Configuration config = c.getResources().getConfiguration();

            // Always send screen dimension for portrait mode
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                height = display.widthPixels;
                width = display.heightPixels;
            } else {
                width = display.widthPixels;
                height = display.heightPixels;
            }

            try {
                PackageManager pm = c.getPackageManager();
                PackageInfo packageInfo = pm.getPackageInfo(c.getPackageName(), PackageManager.GET_CONFIGURATIONS);
                appLabel = (String) pm.getApplicationLabel(packageInfo.applicationInfo);
                appVersion = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException ignore) {
                // this should never happen, we are looking up ourself
            }

            // Tries to conform to default android UA string without the Safari / webkit noise, plus adds the screen dimensions
            userAgentString = String.format("%1$s/%2$s (%3$s; U; Android; %5$s-%6$s; %12$s; %8$s) %9$dX%10$d %11$s %12$s"
                    , appLabel
                    , appVersion
                    , System.getProperty("os.name", "Linux")
                    , null
                    , config.locale.getLanguage().toLowerCase()
                    , config.locale.getCountry().toLowerCase()
                    , null
                    , Build.BRAND
                    , width
                    , height
                    , Build.MANUFACTURER
                    , Build.MODEL);
        }
        Log.d("userAgentString", "userAgentString="+userAgentString);
        return userAgentString;
    }
}