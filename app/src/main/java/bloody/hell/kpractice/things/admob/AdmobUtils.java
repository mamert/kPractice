package bloody.hell.kpractice.things.admob;

import android.content.Context;

import com.google.android.gms.ads.MobileAds;

import bloody.hell.kpractice.R;

/**
 * Created by Mamert on 22.09.2017.
 */

public class AdmobUtils {
    private static boolean initialized = false;

    public static void init(Context ctx){
        if(initialized)
            return;
        String appId = ctx.getString(R.string.admobAppId);
        MobileAds.initialize(ctx, appId);
        initialized = true;
    }

}
