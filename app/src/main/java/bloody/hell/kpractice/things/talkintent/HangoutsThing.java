package bloody.hell.kpractice.things.talkintent;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import bloody.hell.kpractice.R;

/**
 * Created by Mamert on 06.08.2018.
 */

public class HangoutsThing {

    public static void open(Activity a){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(a.getString(R.string.hangout_url)));
        i.setPackage("com.google.android.talk");
        a.startActivityForResult(i, 43);
        // in Activity's onActivityResult can then remove attachment, etc
    }


}
