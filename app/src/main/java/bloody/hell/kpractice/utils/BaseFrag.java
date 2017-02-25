package bloody.hell.kpractice.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.lang.reflect.Field;

import bloody.hell.kpractice.things.volley.VolleySingleton;

/**
 * Created by kaay on 2016-04-22.
 * Contains fixes to some Android bugs, as well as convenience methods
 */
public abstract class BaseFrag extends Fragment {
    // fix for onActivityResult not going to nested Fragments not necessary anymore: https://code.google.com/p/android/issues/detail?id=40537#c39


    //bug with nested Fragments, see http://stackoverflow.com/a/15656428
    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }



    public BaseFrag() {
        super();
        Bundle args = new Bundle();
        setArguments(args); // so that getArguments() never gives null
    }


    @Override
    public void onStop() { // cancel all requests initiated by this Fragment
        super.onStop();
        RequestQueue requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue();
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
    }

    protected void addToRequestQueue(Request req) { // shortcut; assigns tag
        req.setTag(this);
        VolleySingleton.getInstance(getContext()).getRequestQueue().add(req);
    }

    protected abstract String getVolleyTag();
}
