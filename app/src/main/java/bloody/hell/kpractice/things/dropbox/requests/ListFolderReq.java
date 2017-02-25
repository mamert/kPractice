package bloody.hell.kpractice.things.dropbox.requests;

import android.content.Context;

import com.android.volley.Response;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.things.dropbox.model.Entry;
import bloody.hell.kpractice.things.dropbox.model.EntryList;

/**
 * Created by Mamert on 23.02.2017.
 */

public class ListFolderReq extends DropboxRequest<EntryList, ListFolderReq.Params> {
    private static final String ENDPOINT = "2/files/list_folder";
    private static final String CONTINUE = "/continue";

    public ListFolderReq(String path, Response.Listener<EntryList> listener, Response.ErrorListener errorListener) {
        super(ENDPOINT,
                EntryList.class,
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
        boolean recursive = false;
        boolean include_media_info = true;
//        boolean include_deleted = false;
//        boolean include_has_explicit_shared_members = false;
//        boolean include_property_templates = false;
    }
}
