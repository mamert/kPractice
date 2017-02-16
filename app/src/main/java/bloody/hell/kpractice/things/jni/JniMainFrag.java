package bloody.hell.kpractice.things.jni;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.utils.BaseFrag;
import bloody.hell.kpractice.utils.NoFastClick;

/**
 * test: interface with native code
 */
public class JniMainFrag extends BaseFrag implements AJniCallbackReceiver {
    public static final String TAG = "jniMain";
    ViewGroup rootView;
    TextView textView;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("complicated");
    }

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

        textView = (TextView) rootView.findViewById(R.id.text_from_jni);
        // Example of a call to a native method
        textView.setText(stringFromJNI());
        Button b = (Button) rootView.findViewById(R.id.jni_button1);
        // Example of a call to a native method
        b.setOnClickListener(new NoFastClick.ViewOnClickListener(){
            @Override
            public void doOnClick(View view) {
                textView.setText(testCallback1(JniMainFrag.this));
            }
        });
        b = (Button) rootView.findViewById(R.id.jni_button2);
        // Example of a call to a native method
        b.setOnClickListener(new NoFastClick.ViewOnClickListener(){
            @Override
            public void doOnClick(View view) {
                testCallback2(JniMainFrag.this);
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native String testCallback1(AJniCallbackReceiver callback);
    public native void testCallback2(AJniCallbackReceiver callback);


    @Override
    public void simpleJniCallback(String s) { // to be called FROM the C++ code
            Toast.makeText(getContext(), "callback from jni: "+s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String anotherJniCallback(String s) {
        return s+"pong";
    }

}
