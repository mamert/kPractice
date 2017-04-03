package bloody.hell.kpractice.things.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.utils.BaseFrag;
import bloody.hell.kpractice.utils.NoFastClick;

/**
 * testing some convenient UI / graphics stuff
 */
public class UiMainFrag extends BaseFrag {
    public static final String TAG = "ui_stuff";
    ViewGroup rootView;

    // init stuff

    public UiMainFrag() {
        super();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment
     */
    public static UiMainFrag newInstance() {
        UiMainFrag fragment = new UiMainFrag();
        // set arguments in Bundle
        return fragment;
    }



    // lifcycle stuff

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.frag_ui_main, container, false);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView iv1 = (ImageView) rootView.findViewById(R.id.iv1);
        iv1.setImageBitmap(addTransparencyMask(getContext(), R.drawable.tile_instagram_bg, R.drawable.mask));

    }



    public static Bitmap addTransparencyMask(Context c, int bitmapResId, int maskResId){
        return addTransparencyMask(c, bitmapResId, maskResId, PorterDuff.Mode.DST_IN);
    }
    public static Bitmap addTransparencyMask(Context c, int bitmapResId, int maskResId, PorterDuff.Mode mode){
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            options.inMutable = true;
        }
        // We could also use ARGB_4444, but not RGB_565 (we need an alpha layer).
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Resources res = c.getResources();
        Bitmap source = BitmapFactory.decodeResource(res, bitmapResId, options);
        Bitmap bitmap;
        if (source.isMutable()) {
            bitmap = source;
        } else {
            bitmap = source.copy(Bitmap.Config.ARGB_8888, true);
            source.recycle();
        }
        // The bitmap is opaque, we need to enable alpha compositing.
        bitmap.setHasAlpha(true);

        Canvas canvas = new Canvas(bitmap);
        Bitmap mask = BitmapFactory.decodeResource(res, maskResId);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(mode));
        canvas.drawBitmap(mask, 0, 0, paint);
        mask.recycle();
        return bitmap;
    }
//
//    public static Drawable getMenuBgDrawable(Context c, int bgDrawableResId) {
//        Resources res = c.getResources();
//        Bitmap src = addTransparencyMask(c, bgDrawableResId, R.drawable.menuitem_mask); // TODO: programmatically create mask with the correct corners
//        return new BitmapDrawable(res, src);
//    }




}
