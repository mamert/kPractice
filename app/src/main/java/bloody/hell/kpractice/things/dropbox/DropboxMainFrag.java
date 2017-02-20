package bloody.hell.kpractice.things.dropbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.utils.BaseFrag;
import bloody.hell.kpractice.utils.GenericOAuthFrag;
import bloody.hell.kpractice.utils.NoFastClick;

/**
 * test: Dropbox
 */
public class DropboxMainFrag extends BaseFrag implements DropboxAuthFrag.CallBack{
    public static final String TAG = "dropboxMain";
    ViewGroup rootView;
    TextView textView;


    public DropboxMainFrag() {
        super();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment
     */
    public static DropboxMainFrag newInstance() {
        DropboxMainFrag fragment = new DropboxMainFrag();
        // set arguments in Bundle
        return fragment;
    }




    // lifcycle stuff

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.dropbox_frag_main, container, false);
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

        textView = (TextView) rootView.findViewById(R.id.dropbox_status);
        Button b = (Button) rootView.findViewById(R.id.dropbox_button1);
        b.setOnClickListener(new NoFastClick.ViewOnClickListener(){
            @Override
            public void doOnClick(View view) {
                DropboxAuthFrag.showAuthDialog(getResources(), getFragmentManager(), DropboxMainFrag.this);
            }
        });
    }



    // dropbox auth
    @Override
    public void onSuccess(HashMap<String, String> data) {
        textView.setText(data.get(DropboxAuthFrag.KEY_TOKEN));
    }

    @Override
    public void onFail(GenericOAuthFrag.Reason reason) {
        textView.setText("Fail: "+reason.name());
    }

    @Override
    public void onCanceled() {
        textView.setText("canceled");
    }

    @Override
    public boolean isReturnedDataOK(HashMap<String, String> data) {
        String token = data.get(DropboxAuthFrag.KEY_TOKEN);
        return token != null && token.length() > 0;
    }
}
