package bloody.hell.kpractice.things.dropbox;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

import bloody.hell.kpractice.utils.GenericOAuthFrag;

/**
 * Created by Mamert on 23.02.2017.
 */

public class DropboxAuth implements GenericOAuthFrag.Callback {

    private static final String KEY_ACCESS_TOKEN = "ACCESS_TOKEN";

    private String authToken = null;
    private DropboxAuthFrag authFrag;
    private Callback mCallback;
    private Context mContext;

    public DropboxAuth(Context c) {
        mContext = c;
        loadAuth();
    }

    public void login(FragmentManager fragmentManager, Callback callback) {
        mCallback = callback;
        authFrag =
                DropboxAuthFrag.showAuthDialog(mContext.getResources(), fragmentManager, this);
    }

    public void logout() {
        clearStoredAuth();
        authToken = null;
        mCallback = null;
    }


    public String getAuthToken() {
        if(authToken == null)
            loadAuth();
        return authToken;
    }

    public boolean isSignedIn(){
        return getAuthToken() != null;
    }

    private void loadAuth() {
        SharedPreferences prefs = android.preference.PreferenceManager
                .getDefaultSharedPreferences(mContext);
        authToken = prefs.getString(KEY_ACCESS_TOKEN, null);
    }

    private void storeAuth() {
        if (authToken != null) {
            SharedPreferences prefs = android.preference.PreferenceManager
                    .getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(KEY_ACCESS_TOKEN, authToken);
            edit.commit();
            return;
        }
    }

    private void clearStoredAuth() {
        SharedPreferences prefs = android.preference.PreferenceManager
                .getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(KEY_ACCESS_TOKEN, null);
        edit.commit();
    }


    // DropboxAuthFrag.Callback stuff
    @Override
    public void onSuccess(HashMap<String, String> data) {
        authToken = data.get(DropboxAuthFrag.KEY_TOKEN);
        storeAuth();
        if(mCallback != null)
            mCallback.onSuccess(data);
    }

    @Override
    public void onFail(GenericOAuthFrag.Reason reason) {
        if(mCallback != null)
            mCallback.onFail(reason);
    }

    @Override
    public void onCanceled() {
        if(mCallback != null)
            mCallback.onCanceled();
    }

    @Override
    public boolean isReturnedDataOK(HashMap<String, String> data) {
        if(data == null || data.isEmpty())
            return false;
        String token = data.get(DropboxAuthFrag.KEY_TOKEN);
        return token != null && token.length() > 0;
    }

    // simplified from DropboxAuthFrag.Callback
    public static interface Callback {
        public void onSuccess(HashMap<String, String> data);
        public void onFail(GenericOAuthFrag.Reason reason);
        public void onCanceled();
    }


}
