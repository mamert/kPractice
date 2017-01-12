package bloody.hell.kpractice.utils;

import android.support.v4.app.Fragment;

import java.lang.reflect.Field;

/**
 * Created by kaay on 2016-04-22.
 * Contains fixes to some Android bugs, as well as convenience methods
 */
public abstract class BaseFragment extends Fragment {
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

}
