package com.example.aruns.emo;

import android.app.ActivityManager;
import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.*;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class CameraService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        //startActivity(intent);
        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();

        Vision.Builder visionBuilder = new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer("AIzaSyCH1UWvzGtELIMHdC3WW_gOD0D9xeb9wms"));
        final Vision vision = visionBuilder.build();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
                Log.e("CameraService", getTopAppName(context));
                handler.postDelayed(runnable, 5000);

                Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
                    public void onPictureTaken(byte[] imageData, Camera c) {

                        if (imageData == null) {

                            Log.e("CameraService","Camera Data Returned Null");

                        }else{
                            Log.v("CameraService", "Data not null " + imageData.length);
                            runCloudVisionTask(imageData, getTopAppName(context), getTimeStamp(),  vision);
                        }
                        c.release();
                    }

                };


                try {
                    Camera cam = openFrontFacingCameraGingerbread();
                    SurfaceTexture surfaceTexture = new SurfaceTexture(10);
                    cam.setPreviewTexture(surfaceTexture);
                    Camera.Parameters params = cam.getParameters();
                    params.setPreviewSize(640, 480);
                    //params.setPreviewSize(1, 1);
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    params.setPictureFormat(ImageFormat.JPEG);
                    cam.setParameters(params);

                    cam.startPreview();
                    cam.takePicture(null,null,mPictureCallback);
                }catch (Exception e){
                    Log.e("CameraService", "Error with camera"+e.getLocalizedMessage());
                }
            }
        };

        handler.postDelayed(runnable, 15000);
    }

    private Camera openFrontFacingCameraGingerbread() throws RuntimeException {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e("Camera Service", "Camera failed to open: " + e.getLocalizedMessage());
                    throw e;
                }
            }
        }

        return cam;
    }

    public String getTimeStamp(){
        Timestamp ts = new Timestamp((new Date()).getTime());
        return ts.toString();
    }

    public static String getTopAppName(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String strName = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                strName = getLollipopFGAppPackageName(context);
            } else {
                strName = mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PackageManager packageManager= context.getPackageManager();
        String appName = null;
        try {
            appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(strName, PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException e)
        {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            context.startActivity(intent);
            return strName;
        }
        return appName;
    }


    private static String getLollipopFGAppPackageName(Context ctx) {

        try {
            UsageStatsManager usageStatsManager = (UsageStatsManager) ctx.getSystemService(USAGE_STATS_SERVICE);
            long milliSecs = 60 * 1000;
            Date date = new Date();
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, date.getTime() - milliSecs, date.getTime());
            if (queryUsageStats.size() > 0) {
                Log.i("LPU", "queryUsageStats size: " + queryUsageStats.size());
            }
            long recentTime = 0;
            String recentPkg = "";
            for (int i = 0; i < queryUsageStats.size(); i++) {
                UsageStats stats = queryUsageStats.get(i);
                if (i == 0 && !"org.pervacio.pvadiag".equals(stats.getPackageName())) {
                    Log.i("LPU", "PackageName: " + stats.getPackageName() + " " + stats.getLastTimeStamp());
                }
                if (stats.getLastTimeStamp() > recentTime) {
                    recentTime = stats.getLastTimeStamp();
                    recentPkg = stats.getPackageName();
                }
            }
            return recentPkg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void runCloudVisionTask(byte[] imageData, String appName, String time, Vision vision){
        (new CloudVisionTask(imageData, appName, time, vision)).execute();
    }
}
