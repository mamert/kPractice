package bloody.hell.kpractice.things.dropbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.things.dropbox.model.Entry;
import bloody.hell.kpractice.things.dropbox.model.EntryList;
import bloody.hell.kpractice.things.dropbox.requests.DropboxRequest;
import bloody.hell.kpractice.things.dropbox.requests.ListFolderReq;
import bloody.hell.kpractice.utils.BaseFrag;
import bloody.hell.kpractice.utils.GenericOAuthFrag;
import bloody.hell.kpractice.utils.NoFastClick;

/**
 * test: Dropbox
 */
public class DropboxMainFrag extends BaseFrag implements DropboxAuth.Callback {
    public static final String TAG = "dropboxMain";
    ViewGroup rootView;
    TextView textView;
    Button loginButton;
    Button listButton;


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

    @Override
    protected String getVolleyTag() {
        return TAG;
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
        loginButton = (Button) rootView.findViewById(R.id.dropbox_button1);
        loginButton.setOnClickListener(new NoFastClick.ViewOnClickListener(){
            @Override
            public void doOnClick(View view) {
                boolean isSignedIn = Dropbox.getInstance(getContext()).isSignedIn();
                if(isSignedIn) {
                    Dropbox.getInstance(getContext()).logout();
                    setDropboxStateDependentStuff();
                } else {
                    Dropbox.getInstance(getContext()).login(getFragmentManager(), DropboxMainFrag.this);
                }
            }
        });

        listButton = (Button) rootView.findViewById(R.id.dropbox_button2);
        listButton.setOnClickListener(new NoFastClick.ViewOnClickListener(){
            @Override
            public void doOnClick(View view) {
                startDataRequest();
            }
        });
        setDropboxStateDependentStuff();
    }

    private void setDropboxStateDependentStuff(){
        boolean isSignedIn = Dropbox.getInstance(getContext()).isSignedIn();
        loginButton.setText(isSignedIn ? R.string.dropbox_signout : R.string.dropbox_signin);
        listButton.setVisibility(isSignedIn ? View.VISIBLE : View.GONE);
        if(!isSignedIn)
            textView.setText("");
    }


    private void startDataRequest(){
        addToRequestQueue(new ListFolderReq(
                Entry.ROOT_FOLDER_PATH,
                new Response.Listener<EntryList>() {
                    @Override
                    public void onResponse(EntryList response) {
                        String digest = String.valueOf(response.entries.size()) + ": ";
                        for(Entry e : response.entries){
                            digest += e.getType().name() + ": " + e.path_display + "; ";
                        }
                        textView.setText(digest);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO
                error = DropboxRequest.translateError(error);
                Log.d(TAG, "GetMetadataReq failed: "+error);
                error.printStackTrace();
            }
        }));
    }



    // dropbox auth
    @Override
    public void onSuccess(HashMap<String, String> data) {
        textView.setText(data.get(DropboxAuthFrag.KEY_TOKEN));
        setDropboxStateDependentStuff();
    }

    @Override
    public void onFail(GenericOAuthFrag.Reason reason) {
        textView.setText("Fail: "+reason.name());
    }

    @Override
    public void onCanceled() {
        textView.setText("canceled");
    }

}
