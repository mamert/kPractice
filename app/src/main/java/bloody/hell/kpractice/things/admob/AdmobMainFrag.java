package bloody.hell.kpractice.things.admob;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.things.volley.VolleySingleton;
import bloody.hell.kpractice.utils.BaseFrag;
import bloody.hell.kpractice.utils.NoFastClick;

/**
 * testing ads
 */
public class AdmobMainFrag extends BaseFrag {
    public static final String TAG = "admob_stuff";
    private ViewGroup rootView;
    private AdView topAdView;
    private AdView bottomAdView;
    private AdmobUtils.AdBetweenStuff adBetweenStuff;
    private AdmobUtils.AdWithReward adWithReward;

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
        adBetweenStuff = AdmobUtils.getAdBetweenStuff(getContext());
        adWithReward = AdmobUtils.getAdWithReward(getActivity());
    }
    @Override
    public void onStart(){
        super.onStart();
        AdmobUtils.loadBanner(topAdView);
        AdmobUtils.loadBanner(bottomAdView);
        adBetweenStuff.load();
        adWithReward.load();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.frag_admob_main, container, false);
        topAdView = (AdView) rootView.findViewById(R.id.topAdView);
        bottomAdView = (AdView) rootView.findViewById(R.id.bottomAdView);

        Button b = (Button) rootView.findViewById(R.id.ad_button);
        b.setOnClickListener(new NoFastClick.ViewOnClickListener(){
            @Override
            public void doOnClick(View view) {
                adWithReward.show();
                // TODO: listener
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        VolleySingleton.getInstance(getContext()).cancelAllRequestsFor(this);
        Activity a = getActivity();
        if(a != null && !a.isFinishing()) {
            adBetweenStuff.show();
        }
    }





}
