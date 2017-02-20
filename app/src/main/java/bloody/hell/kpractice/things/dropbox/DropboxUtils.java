package bloody.hell.kpractice.things.dropbox;

import android.content.res.Resources;

import bloody.hell.kpractice.R;

/**
 * Created by Mamert on 20.02.2017.
 */

public class DropboxUtils {

    public static String getEndpointUrl(Resources res, int endpointStringId){
        return res.getString(R.string.dropbox_api_url) + res.getString(endpointStringId);
    }


}
