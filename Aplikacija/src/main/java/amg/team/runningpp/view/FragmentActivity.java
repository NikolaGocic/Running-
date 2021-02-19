package amg.team.runningpp.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import amg.team.runningpp.R;
import amg.team.runningpp.data.Constants;

public class FragmentActivity extends Fragment implements OnMapReadyCallback, SensorEventListener {
    private GoogleMap mMap;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    AlertDialog dialog;

    private Button btnStart;
    private TextView tvPrikaz;

    boolean timerStarted = false;
    private int time = 0;

    private boolean start = false;
    private boolean firstSpot = false;

    private boolean isVisible = true;
    private boolean isMoving = false;

    private LatLng endLocation = null;
    private float distance = 0;
    private LatLng newLocation = null;
    private LatLng currentLocation = null;
    LatLngBounds.Builder builderLatLng ;

    private Date timeStartActivity = null;
    private Date timeEndActivity = null;

    boolean referentLocations = false;

    private Bitmap image = null;

    private float maxSpeed = 0;
    private float currentSpeed = 0;

    private SensorManager sensorMan;
    private Sensor accelerometer;

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    Handler mHandler;

    PowerManager.WakeLock mWakeLock;

    private int locationPriority =LocationRequest.PRIORITY_HIGH_ACCURACY;
    private int locationFastestInterval = 1000;
    private int locationInterval = 5000;
    private int lineThickness = 12;

    private int startPointImage = R.drawable.tenpx_blue_dot;
    private int endPointImage = R.drawable.tenpx_green_dot;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        PowerManager pm = (PowerManager)this.getActivity().getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, Constants.WAKELOCK_TAG);
        mWakeLock.acquire();

        btnStart = (Button) view.findViewById(R.id.btnStartActivity);
        tvPrikaz = (TextView) view.findViewById(R.id.textViewAktivnost);
        mHandler = new Handler();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);


        if(checkPermission()) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }

        if(!timerStarted) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(1000);
                            mHandler.post(new Runnable() {

                                @Override
                                public void run() {
                                    if (start) {
                                        time++;
                                        setText(time, distance);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        timerStarted = true;
        }


        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null)
                    return;
                else{
                    for(Location location : locationResult.getLocations()){
                        if(isLocationUsable(location)) {
                            drawRoute(location);
                        }
                        Log.i("Location : ", location.toString());
                    }
                }
            }


        };

        builderLatLng = new LatLngBounds.Builder();

        sensorMan = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;


        locationRequest = LocationRequest.create()
                .setPriority(locationPriority)
                .setInterval(locationInterval)
                .setFastestInterval(locationFastestInterval);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStartClick();
                mWakeLock.acquire();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWakeLock.acquire();
        sensorMan.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }


    private void onDialogStopClick(){
        dialog.hide();
        btnStart.setVisibility(View.VISIBLE);
        btnStopClick();
    }

    private void onDialogCancleClick(){
        dialog.hide();
        try {
            if (start) {
                btnStart.setVisibility(View.VISIBLE);
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                start = false;
                resetActivityData();
                setText(time,distance);
            } else {
                Toast.makeText(getActivity(), "You need to start activity first !", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDialogStopClick();
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDialogCancleClick();
            }
        });
        dialog = builder.create();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.setTitle("Stop Activity");
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.BOTTOM ;
        dialog.show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGravity = event.values.clone();
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float)Math.sqrt(x*x + y*y + z*z);
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if(mAccel > 8){
                isMoving = true;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWakeLock.release();
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
    }

    private void btnStartClick(){
        try {
            if (!start) {
                isMoving = true;
                sensorMan.registerListener(this, accelerometer,
                        SensorManager.SENSOR_DELAY_UI);
                btnStart.setVisibility(View.GONE);
                resetActivityData();
                setText(0, 0);
                start = true;
                if (checkPermission()) {
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                }
                showDialog();
            } else {
                Toast.makeText(getActivity(), "You need to stop activity first !", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
    }

    private void btnStopClick(){
        try {
            if (start) {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                timeEndActivity = new Date();
                start = false;
                mMap.addMarker(new MarkerOptions()
                        .position(endLocation)
                        .title("Start")
                        .icon(bitmapDescriptorFromVector(getActivity(), endPointImage)));
                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        image = bitmap;
                        DateFormat format = new SimpleDateFormat("yyyy-MM-d-HH:mm:ss");
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 13, stream);
                        byte[] byteArray = stream.toByteArray();
                        Bundle b = new Bundle();
                        b.putByteArray(Constants.BUNDLE_PASSING_SLIKA,byteArray);
                        if(timeEndActivity!=null)
                            b.putString(Constants.BUNDLE_PASSING_ZAVRSNO_VREME,format.format(timeEndActivity));
                        else
                            b.putString(Constants.BUNDLE_PASSING_ZAVRSNO_VREME,"");
                        if(timeStartActivity != null)
                            b.putString(Constants.BUNDLE_PASSING_POCETNO_VREME,format.format(timeStartActivity));
                        else
                            b.putString(Constants.BUNDLE_PASSING_POCETNO_VREME,"");
                        b.putString(Constants.BUNDLE_PASSING_DISTANCA,String.format("%.2f", distance / 1000));
                        b.putString(Constants.BUNDLE_PASSING_MAX_BRZINA,String.format("%.2f", maxSpeed));
                        b.putString(Constants.BUNDLE_PASSING_PROSECNA_BRZINA,String.format("%.2f", (distance / time) * 3.6));
                        ((ActivityApp)getActivity()).setBundle(1,b);
                        ((ActivityApp)getActivity()).changeCurrentFragment(new FragmentSaveActivity(),1,1);
                    }
                });

            } else {
                Toast.makeText(getActivity(), "You need to start activity first !", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setText(int time,float distance){
        try {
            String timeFormated = String.format("%02d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60));
            String distanceFormated = String.format("%.2f", distance / 1000);
            String avgSpeed = String.format("%.2f", (distance / time) * 3.6);
            String maxSpeedString = String.format("%.2f", maxSpeed);
            String currentSpeedString = String.format("%.2f", currentSpeed);
            tvPrikaz.setText("Duration : " + timeFormated + " s" + "\nDistacne : " + distanceFormated + " km" +
                    "\nAverage Speed : " + avgSpeed + " km/h" + "\nMaximum Speed : " + maxSpeedString + " km/h" +
                    "\nCurrent Speed : " + currentSpeedString + " km/h"
            );
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void resetActivityData(){
        mMap.clear();
        time = 0;
        isMoving = false;
        endLocation = null;
        distance=0;
        maxSpeed = 0;
        currentSpeed = 0;
        start = false;
        firstSpot = false;
        newLocation = null;
        currentLocation = null;
        timeStartActivity = null;
        timeEndActivity = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        if (checkPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }



    private void drawRoute(Location location){
        try {
            if (isVisible) {
                if (!firstSpot && !start) {
                    mMap.clear();
                    LatLng markerLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(markerLoc)
                            .title("Start")
                            .icon(bitmapDescriptorFromVector(getActivity(), startPointImage)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLoc, 17f));
                    firstSpot = true;
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    endLocation = currentLocation;
                } else if (!start) {
                    mMap.clear();
                    LatLng markerLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(markerLoc)
                            .title("Start")
                            .icon(bitmapDescriptorFromVector(getActivity(), startPointImage)));
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    endLocation = currentLocation;
                } else if (start && isMoving) {
                    if (!firstSpot) {
                        mMap.clear();
                        LatLng markerLoc = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions()
                                .position(markerLoc)
                                .title("Start")
                                .icon(bitmapDescriptorFromVector(getActivity(), startPointImage)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLoc, 17f));
                        firstSpot = true;
                        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        endLocation = currentLocation;
                    } else {
                        newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        Location loc = new Location("provider");
                        loc.setLatitude(currentLocation.latitude);
                        loc.setLongitude(currentLocation.longitude);

                        currentSpeed = (float) (location.getSpeed() * 3.6);
                        if (currentSpeed > 0)
                            distance += location.distanceTo(loc);


                        if (maxSpeed < currentSpeed || maxSpeed > 10000)
                            maxSpeed = currentSpeed;

                        mMap.addPolyline(new PolylineOptions()
                                .add(newLocation, currentLocation)
                                .width(lineThickness)
                                .color(Color.BLUE));

                        builderLatLng.include(newLocation);
                        if (!referentLocations) {
                            LatLng nw = new LatLng(newLocation.latitude + 0.001, newLocation.longitude + 0.001);
                            LatLng sw = new LatLng(newLocation.latitude - 0.001, newLocation.longitude - 0.001);

                            builderLatLng.include(nw);
                            builderLatLng.include(sw);
                            referentLocations = true;
                        }

                        LatLngBounds bounds = builderLatLng.build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                        mMap.animateCamera(cameraUpdate);

                        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        endLocation = currentLocation;
                    }

                } else if (!isMoving && start) {
                    mMap.clear();
                    LatLng markerLoc = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(markerLoc)
                            .title("Start")
                            .icon(bitmapDescriptorFromVector(getActivity(), startPointImage)));
                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    endLocation = currentLocation;
                }
                if (start && time == 0) {
                    timeStartActivity = new Date();

                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isLocationUsable(Location location){

        if(currentLocation != null) {
            if (Math.abs(currentLocation.latitude - location.getLatitude()) > 0.00005
                    || Math.abs(currentLocation.longitude - location.getLongitude()) > 0.00005) {
                return true;
            }
        }
        else
            return true;

        return true;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResourceID){
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorResourceID);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return  BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
                return false;
            }
        }
        return true;
    }

}
