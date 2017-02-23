package bloody.hell.kpractice.things.dropbox;


import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentManager;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.utils.GenericOAuthFrag;

/**
 * Created by Mamert on 17.02.2017.
 */

public class DropboxAuthFrag extends GenericOAuthFrag {


    public DropboxAuthFrag() {
        super();
    }

    public static DropboxAuthFrag showAuthDialog(Context c, FragmentManager fm, Callback callback) {
        return showAuthDialog(c.getResources(), fm, callback);
    }
    public static DropboxAuthFrag showAuthDialog(Resources res, FragmentManager fm, Callback callback) {
        DropboxAuthFrag authWebviewFragment = new DropboxAuthFrag();
        authWebviewFragment.setCallback(callback);
        String uri = res.getString(R.string.dropbox_auth_url);
        String redirectUri = res.getString(R.string.dropbox_redirect_url);

        return (DropboxAuthFrag) authWebviewFragment.show(fm, uri, redirectUri,
                res.getString(R.string.dropbox_api_key),
                true, KEY_TOKEN);
    }

}