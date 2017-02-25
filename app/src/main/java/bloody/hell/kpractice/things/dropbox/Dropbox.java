package bloody.hell.kpractice.things.dropbox;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mamert on 24.02.2017.
 */

public class Dropbox {
    private static Dropbox instance = null;
    private static DropboxAuth auth;


    private Dropbox(Context c){
        auth = new DropboxAuth(c);
    }

    public static Dropbox getInstance(Context c){
        if(instance == null)
            instance = new Dropbox(c);
        return instance;
    }

    // wrappers for auth

    public boolean isSignedIn(){
        return auth.isSignedIn();
    }

    public void login(FragmentManager fragmentManager, DropboxAuth.Callback callback) {
        auth.login(fragmentManager, callback);
    }

    public void logout() {
        auth.logout();
    }


    public Map<String, String> createAuthHeaders(@Nullable Map<String, String> baseHeaders) {
        if (baseHeaders == null)
            baseHeaders = new HashMap<>();

        String authValue = "Bearer " + auth.getAuthToken();
        try {
            baseHeaders.put("Authorization", authValue);
        }catch(UnsupportedOperationException uoe){ // we somehow gave it an immutable map
            baseHeaders = new HashMap<>(baseHeaders); // copy  to a mutable map
            baseHeaders.put("Authorization", authValue); // and retry
        }
        return baseHeaders;
    }


}
