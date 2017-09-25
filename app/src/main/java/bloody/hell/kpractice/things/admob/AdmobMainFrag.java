package bloody.hell.kpractice.things.admob;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.utils.BaseFrag;

/**
 * testing ads
 */
public class AdmobMainFrag extends BaseFrag {
    public static final String TAG = "admob_stuff";
    private ViewGroup rootView;
    private AdView mAdView;

    // init stuff

    public AdmobMainFrag() {
        super();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment
     */
    public static AdmobMainFrag newInstance() {
        AdmobMainFrag fragment = new AdmobMainFrag();
        // set arguments in Bundle
        return fragment;
    }



    // lifcycle stuff

    @Override
    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        AdmobUtils.init(getActivity()); // only needs to be done once for the app, really...
    }
    @Override
    public void onStart(){
        super.onStart();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.frag_admob_main, container, false);
        mAdView = (AdView) rootView.findViewById(R.id.adView);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }





}
