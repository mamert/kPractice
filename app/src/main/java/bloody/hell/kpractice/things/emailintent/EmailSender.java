package bloody.hell.kpractice.things.emailintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.List;
import java.util.Stack;

/**
 * Created by Mamert on 14.12.2017.
 */

public class EmailSender {


    // source: https://stackoverflow.com/a/12804063/1034042
    public static Intent createEmailOnlyChooserIntent(Context c, Intent source,
                                                      CharSequence chooserTitle) {
        Stack<Intent> intents = new Stack<Intent>();
        // the address below will be overridden with data from source Intent
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                "info@domain.com", null));
        List<ResolveInfo> activities = c.getPackageManager()
                .queryIntentActivities(i, 0);

        for(ResolveInfo ri : activities) {
            Intent target = new Intent(source);
            target.setPackage(ri.activityInfo.packageName);
            intents.add(target);
        }

        if(!intents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(intents.remove(0),
                    chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    intents.toArray(new Parcelable[intents.size()]));

            return chooserIntent;
        } else {
            return Intent.createChooser(source, chooserTitle);
        }
    }


    public static void sendExampleEmail(Activity a){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/html");
        shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@someaddress.net"});
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "subject text");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "body text");
        // TODO: attachment with FileProvider

        Intent chooser = createEmailOnlyChooserIntent(a, shareIntent, "chooser title");
        a.startActivityForResult(chooser, 42);
        // in Activity's onActivityResult can then remove attachment, etc
    }


}
