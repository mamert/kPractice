package bloody.hell.kpractice.things.dropbox.requests;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.things.dropbox.Dropbox;
import bloody.hell.kpractice.things.dropbox.model.Entry;
import bloody.hell.kpractice.things.volley.GsonPostRequestWithGsonBody;
import bloody.hell.kpractice.things.volley.GsonRequest;

/**
 * Created by Mamert on 23.02.2017.
 */

public class GetMetadataReq extends DropboxRequest<Entry, GetMetadataReq.Params> {
    private static final String ENDPOINT = "2/files/get_metadata";

    public GetMetadataReq(String path, Response.Listener<Entry> listener, Response.ErrorListener errorListener) {
        super(ENDPOINT,
                Entry.class,
                createParams(path),
                listener,
                errorListener);

    }

    private static Params createParams(String path) {
        Params p = new Params();
        p.path = path;
        p.include_media_info = true;
        return p;
    }


    public static class Params {
        String path;
        boolean include_media_info = true;
//        boolean include_deleted = false;
//        boolean include_has_explicit_shared_members = false;
//        boolean include_property_templates = false;
    }
}
