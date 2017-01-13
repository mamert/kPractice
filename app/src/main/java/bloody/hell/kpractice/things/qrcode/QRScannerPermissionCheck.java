package bloody.hell.kpractice.things.qrcode;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.utils.NoFastClick;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Mamert on 11.01.2017.
 */

public class QRScannerPermissionCheck {
    private static final int PERM_REQUEST_CAMERA = 1;
    private static CameraPermissionListener activeListener = null; // TODO: a better way without cluttering Activity


    public static boolean isCameraPermGranted(Activity a){
        int permissionCheck = ContextCompat.checkSelfPermission(a,
                Manifest.permission.CAMERA);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean showCameraPermissionRationale(Activity a){
        return ActivityCompat.shouldShowRequestPermissionRationale(a,
                Manifest.permission.CAMERA);
    }


    public static void doPermissionStuffForCamera(Activity a, CameraPermissionListener listener){
        activeListener = listener;
        if(isCameraPermGranted(a)){
            listener.onCamPermGranted();
            activeListener = null;
        } else if(showCameraPermissionRationale(a)){
            showCamRationale(a, listener);
        } else {
            requestCameraPermission(a, listener);
        }
    }

    private static void showCamRationale(final Activity a, final CameraPermissionListener listener){
        // dialog.
        // activeListener = null
        // buttons: requestCameraPermission or listener.onCamPermDenied
        Log.d ("camPerm", "showCamRationale");

        AlertDialog dialog = new AlertDialog.Builder(a)
                .setTitle(R.string.qrscan_perm_title)
                .setMessage(R.string.qrscan_perm_rationale)
                .setPositiveButton(R.string.qrscan_perm_grant, new NoFastClick.DialogInterfaceOnClickListener() {
                    @Override
                    public void doOnClick(DialogInterface dialog, int id) {
                        requestCameraPermission(a, listener);
                    }
                })
                .setNegativeButton(R.string.qrscan_perm_deny, new NoFastClick.DialogInterfaceOnClickListener() {
                    @Override
                    public void doOnClick(DialogInterface dialog, int id) {
                        listener.onCamPermDenied();
                        activeListener = null;
                    }
                })
                .setCancelable(false)
                .create();
        dialog.show();
    }

    private static void requestCameraPermission(Activity a, CameraPermissionListener listener){

        ActivityCompat.requestPermissions (a,
                new String[] {Manifest.permission.CAMERA},
                PERM_REQUEST_CAMERA);
    }


    // use this in Activity/Fragment's onRequestPermissionsResult, after super
    public static void onRequestPermissionsResult (final int requestCode,
                                                   @NonNull final String[] permissions,
                                                   @NonNull final int[] grantResults) {
        if(activeListener == null){
            Log.d ("camPerm", "activeListener is null, that shouldn't have happened!");
            return;
        }
        Log.d ("camPerm",
                String.format (Locale.ENGLISH,
                        "Request permission result for request %d.\nPermissions: %s.\nResults: %s",
                        requestCode,
                        Arrays.toString (permissions),
                        Arrays.toString (grantResults)));

        if (requestCode != PERM_REQUEST_CAMERA)
            return;

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.d ("camPerm", "perm granted");
            activeListener.onCamPermGranted();
        } else {
            Log.d ("camPerm", "perm denied");
            activeListener.onCamPermDenied();
        }
        activeListener = null;
    }




    public static interface CameraPermissionListener{
        void onCamPermGranted();
        void onCamPermDenied();
    }
}
