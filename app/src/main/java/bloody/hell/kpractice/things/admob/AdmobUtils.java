package bloody.hell.kpractice.things.admob;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import bloody.hell.kpractice.R;

/**
 * Created by Mamert on 22.09.2017.
 */

public class AdmobUtils {
    private static boolean initialized = false;

    private static AdBetweenStuff adBetweenStuff = null; // single, available anywhere
    private static AdWithReward adWithReward = null; // warning: single Activity only

    public static void init(Context ctx){
        if(initialized)
            return;
        String appId = ctx.getString(R.string.admobAppId);
        MobileAds.initialize(ctx, appId);
        initialized = true;
    }

    public static void loadBanner(AdView adView){
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
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


    public static AdWithReward getAdWithReward(final Activity act){
        if(adWithReward != null)
            return adWithReward;

        adWithReward = new AdWithReward() {
            RewardedVideoAd rewardedVideoAd = createRewardedVideoAd(act, this);
            @Override
            public void load() {
                String adUnitId = act.getString(R.string.admobUnit_TestRewardedVidAd);
                rewardedVideoAd.loadAd(adUnitId, new AdRequest.Builder().build());
            }

            @Override
            public boolean show() {
                if(!rewardedVideoAd.isLoaded())
                    return false;
                rewardedVideoAd.show();
                return true;
            }
        };
        return adWithReward;
    }
    private static RewardedVideoAd createRewardedVideoAd(Activity act, AdWithReward adWithReward){
        RewardedVideoAd rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(act); // must be Activity not Application context
        attachAdListener(rewardedVideoAd, adWithReward);
        return rewardedVideoAd;
    }
    private static void attachAdListener(final RewardedVideoAd rewardedVideoAd, final AdWithReward adWithReward){
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener(){
            @Override
            public void onRewarded(RewardItem reward){

            }
            @Override
            public void onRewardedVideoAdLeftApplication() {

            }
            @Override
            public void onRewardedVideoAdClosed(){
                adWithReward.load();
            }
            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode){

            }
            @Override
            public void onRewardedVideoAdLoaded(){

            }
            @Override
            public void onRewardedVideoAdOpened(){

            }
            @Override
            public void onRewardedVideoStarted() {

            }
        });
    }


    public interface AdBetweenStuff{
        abstract void load();
        abstract boolean show();
    }

    public interface AdWithReward{
        abstract void load();
        abstract boolean show();
    }
}
