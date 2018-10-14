package com.example.aruns.emo;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CloudVisionTask extends AsyncTask<Void, Void, Void> {

    public static HashMap<String, ArrayList<EmotionDataPoint>> map = new HashMap<>();

    public Vision vision;
    public byte[] imageData;
    public String appName;
    public String time;

    public CloudVisionTask(byte[] imageData, String appName, String time, Vision vision) {
        this.imageData = imageData;
        this.appName = appName;
        this.time = time;
        this.vision = vision;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Image inputImage = new Image();
            inputImage.encodeContent(imageData);
            Feature desiredFeature = new Feature();
            desiredFeature.setType("FACE_DETECTION");
            AnnotateImageRequest request = new AnnotateImageRequest();
            request.setImage(inputImage);
            request.setFeatures(Arrays.asList(desiredFeature));
            BatchAnnotateImagesRequest batchRequest =
                    new BatchAnnotateImagesRequest();
            batchRequest.setRequests(Arrays.asList(request));

            BatchAnnotateImagesResponse batchResponse =
                    vision.images().annotate(batchRequest).execute();

            AnnotateImageResponse response = batchResponse.getResponses()
                    .get(0);
            String angerLikelihood = (String) ((FaceAnnotation) ((ArrayList)response.get("faceAnnotations")).get(0)).get("angerLikelihood");
            String joyLikelihood = (String) ((FaceAnnotation) ((ArrayList)response.get("faceAnnotations")).get(0)).get("joyLikelihood");
            String sorrowLikelihood = (String) ((FaceAnnotation) ((ArrayList)response.get("faceAnnotations")).get(0)).get("sorrowLikelihood");
            String surpriseLikelihood = (String) ((FaceAnnotation) ((ArrayList)response.get("faceAnnotations")).get(0)).get("surpriseLikelihood");
            //Log.v("Classtype", "Anger: " + angerLikelihood);
            //Log.v("Classtype", "Joy: " + joyLikelihood);
            //Log.v("Classtype", "Sorrow: " + sorrowLikelihood);
            //Log.v("Classtype", "Surprise: " + surpriseLikelihood);
            EmotionDataPoint angerDataPoint = new EmotionDataPoint("ANGER", angerLikelihood, time);
            EmotionDataPoint joyDataPoint = new EmotionDataPoint("JOY", joyLikelihood, time);
            EmotionDataPoint sorrowDataPoint = new EmotionDataPoint("SORROW", sorrowLikelihood, time);
            EmotionDataPoint surpriseDataPoint = new EmotionDataPoint("SURPRISE", surpriseLikelihood, time);

            if (!map.containsKey(appName))
                map.put(appName, new ArrayList<EmotionDataPoint>());

            map.get(appName).add(angerDataPoint);
            map.get(appName).add(joyDataPoint);
            map.get(appName).add(sorrowDataPoint);
            map.get(appName).add(surpriseDataPoint);

            Log.v("CloudVisionTask", "Hashmap: " + Arrays.toString(map.entrySet().toArray()));
        }catch (Exception e){
            Log.e("CloudVisionTask", e.getLocalizedMessage());
        }

        return null;
    }


}