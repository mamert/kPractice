package bloody.hell.kpractice.things.sensor;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bloody.hell.kpractice.R;
import bloody.hell.kpractice.utils.BaseFrag;

/**
 * testing gyro
 */
public class SensorMainFrag extends BaseFrag {
    public static final String TAG = "admob_stuff";
    private ViewGroup rootView;
    private TextView textGyroX, textGyroY, textGyroZ, textRotX, textRotY, textRotZ, textAccelZ, textAccelX, textAccelY;
    private SensorManager sensorManager;
    private Sensor gyroSensor;
    private Sensor accelSensor;
    private Sensor rotSensor;

    // init stuff

    public SensorMainFrag() {
        super();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment
     */
    public static SensorMainFrag newInstance() {
        SensorMainFrag fragment = new SensorMainFrag();
        // set arguments in Bundle
        return fragment;
    }



    // lifcycle stuff

    @Override
    public void onStart(){
        super.onStart();

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(gyroListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
        rotSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(rotListener, rotSensor, SensorManager.SENSOR_DELAY_NORMAL);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(accelListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.frag_sensor_main, container, false);
        textGyroX = rootView.findViewById(R.id.textGyroX);
        textGyroY = rootView.findViewById(R.id.textGyroY);
        textGyroZ = rootView.findViewById(R.id.textGyroZ);
        textRotX = rootView.findViewById(R.id.textRotX);
        textRotY = rootView.findViewById(R.id.textRotY);
        textRotZ = rootView.findViewById(R.id.textRotZ);
        textAccelX = rootView.findViewById(R.id.textAccelX);
        textAccelY = rootView.findViewById(R.id.textAccelY);
        textAccelZ = rootView.findViewById(R.id.textAccelZ);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(gyroListener);
    }


    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {}

        public void onSensorChanged(SensorEvent event) {
            textGyroX.setText(String.format("X : %.7f rad/s", event.values[0]));
            textGyroY.setText(String.format("Y : %.7f rad/s", event.values[1]));
            textGyroZ.setText(String.format("Z : %.7f rad/s", event.values[2]));
        }
    };

    public SensorEventListener rotListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {}

        public void onSensorChanged(SensorEvent event) {
            textRotX.setText(String.format("X : %.7f", event.values[0]));
            textRotY.setText(String.format("Y : %.7f", event.values[1]));
            textRotZ.setText(String.format("Z : %.7f", event.values[2]));
        }
    };

    public SensorEventListener accelListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {}

        public void onSensorChanged(SensorEvent event) {
            textAccelX.setText(String.format("X : %.7f m/s2", event.values[0]));
            textAccelY.setText(String.format("Y : %.7f m/s2", event.values[1]));
            textAccelZ.setText(String.format("Z : %.7f m/s2", event.values[2]));
        }
    };



}
