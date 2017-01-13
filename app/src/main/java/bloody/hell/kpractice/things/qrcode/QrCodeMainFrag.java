package bloody.hell.kpractice.things.qrcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.utils.BaseFrag;
import bloody.hell.kpractice.utils.NoFastClick;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * A placeholder fragment containing a simple view.
 */
public class QrCodeMainFrag extends BaseFrag implements ZBarScannerView.ResultHandler {
    public static final String TAG = "qrMain";
    ViewGroup rootView;

    // init stuff

    public QrCodeMainFrag() {
        super();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment
     */
    public static QrCodeMainFrag newInstance() {
        QrCodeMainFrag fragment = new QrCodeMainFrag();
        // set arguments in Bundle
        return fragment;
    }




    // lifcycle stuff

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.qr_frag_main, container, false);
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

        Button b = (Button) rootView.findViewById(R.id.qr_button);
        // Example of a call to a native method
        b.setOnClickListener(new NoFastClick.ViewOnClickListener(){
            @Override
            public void doOnClick(View view) {
                showScanner();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(shouldScannerBeShown)
            showScanner();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideScanner();
    }




    // qr code stuff

    private ZBarScannerView mScannerView;
    private boolean shouldScannerBeShown = false;

    @Override
    public void handleResult(Result rawResult) { // ZBarScannerView.ResultHandler
        TextView tv = (TextView) rootView.findViewById(R.id.text_from_qrcode);
        tv.setText("format: "+rawResult.getBarcodeFormat().getName()+
                "; content: "+rawResult.getContents());
        shouldScannerBeShown = false;
        hideScanner();
    }


    private void showScanner() {
        QRScannerPermissionCheck.doPermissionStuffForCamera(getActivity(), new QRScannerPermissionCheck.CameraPermissionListener() {
            @Override
            public void onCamPermGranted() {
                mScannerView = new ZBarScannerView(getActivity());    // Programmatically initialize the scanner view
                rootView.addView(mScannerView);                // Set the scanner view as the content view
                mScannerView.setResultHandler(QrCodeMainFrag.this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();          // Start camera on resume
            }
            @Override
            public void onCamPermDenied() {
                Toast.makeText(getContext(), "Permission dened", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void hideScanner() {
        if(mScannerView != null) {
            mScannerView.stopCamera();           // Stop camera on pause
            rootView.removeView(mScannerView);
            mScannerView = null;
        }
    }

    @Override
    public void onRequestPermissionsResult (final int requestCode,
                                            final String[] permissions,
                                            final int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);

        QRScannerPermissionCheck.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
