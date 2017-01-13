package bloody.hell.kpractice.things.jni;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.utils.BaseFrag;
import bloody.hell.kpractice.utils.NoFastClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class JniMainFrag extends BaseFrag {
    public static final String TAG = "jniMain";
    ViewGroup rootView;

    // init stuff

    public JniMainFrag() {
        super();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment
     */
    public static JniMainFrag newInstance() {
        JniMainFrag fragment = new JniMainFrag();
        // set arguments in Bundle
        return fragment;
    }




    // lifcycle stuff

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.jni_frag_main, container, false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv = (TextView) rootView.findViewById(R.id.text_from_jni);
        // Example of a call to a native method
        tv.setText(stringFromJNI());
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();




}
