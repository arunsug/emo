package com.example.aruns.emo;

import android.app.Service;
import android.content.*;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.*;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;

import java.io.IOException;
import java.util.List;

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
                handler.postDelayed(runnable, 100000);

                Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
                    public void onPictureTaken(byte[] imageData, Camera c) {

                        if (imageData == null) {

                            Log.e("CameraService","Camera Data Returned Null");

                        }else{
                            Log.v("CameraService", "Data not null " + imageData.length);
                            runCloudVisionTask(imageData, vision);
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
                    Log.e("CameraService", "Error with camera");
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
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
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

    public void runCloudVisionTask(byte[] imageData, Vision vision){
        (new CloudVisionTask(imageData, vision)).execute();
    }
}
