package bloody.hell.kpractice.things.admob;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import bloody.hell.kpractice.R;

/**
 * Created by Mamert on 22.09.2017.
 */

public class AdmobUtils {
    private static boolean initialized = false;

    private static AdBetweenStuff adBetweenStuff = null; // single, available anywhere

    public static void init(Context ctx){
        if(initialized)
            return;
        String appId = ctx.getString(R.string.admobAppId);
        MobileAds.initialize(ctx, appId);
        initialized = true;
    }



    public static AdBetweenStuff getAdBetweenStuff(final Context ctx){
        if(adBetweenStuff != null)
            return adBetweenStuff;

        adBetweenStuff = new AdBetweenStuff() {
            InterstitialAd interstitialAd = createInterstitialAd(ctx);
            @Override
            public void load() {
                interstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public boolean show() {
                if(!interstitialAd.isLoaded())
                    return false;
                interstitialAd.show();
                return true;
            }
        };
        return adBetweenStuff;
    }
    private static InterstitialAd createInterstitialAd(Context ctx){
        InterstitialAd interstitialAd = new InterstitialAd(ctx);
        String adUnitId = ctx.getString(R.string.admobUnit_TestInterstitial);
        interstitialAd.setAdUnitId(adUnitId);
        attachAdListener(interstitialAd);
        return interstitialAd;
    }
    private static void attachAdListener(final InterstitialAd interstitialAd){
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed(){
                interstitialAd.loadAd(new AdRequest.Builder().build()); // load next
            }
            @Override
            public void onAdOpened(){

            }
            @Override
            public void onAdFailedToLoad(int errorCode){

            }
            @Override
            public void onAdLoaded(){

            }
        });
    }


    public interface AdBetweenStuff{
        abstract void load();
        abstract boolean show();
    }
}
