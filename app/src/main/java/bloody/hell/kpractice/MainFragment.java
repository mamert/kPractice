package bloody.hell.kpractice;

import android.content.Context;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import bloody.hell.kpractice.utils.BaseFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends BaseFragment {
    public static final String TAG = "base";
    ViewGroup rootView;

    // init stuff

    public MainFragment() {
        super();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecFragment.
     */
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        // set arguments in Bundle
        return fragment;
    }




    // lifcycle stuff

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
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

        tv = (Button) rootView.findViewById(R.id.qr_button);
        tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        });
        tv = (Button) rootView.findViewById(R.id.fraginteraction_button);
        tv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mListener.testSendingStuffToActivity(stringFromJNI());
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        storeOnFragmentInteractionListener(context);
    }




    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();





    // stuff for communication with hosting Activity\

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void testSendingStuffToActivity(String s);
    }

    private OnFragmentInteractionListener mListener;

    private void storeOnFragmentInteractionListener(Context context){
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

}
