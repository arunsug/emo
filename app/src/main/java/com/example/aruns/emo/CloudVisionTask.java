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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CloudVisionTask extends AsyncTask<Void, Void, Void> {

    public static final HashMap<String, ArrayList<Pair>> data = new HashMap<>();

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
            String angerLikelihood = ((FaceAnnotation) ((ArrayList)response.get("faceAnnotations")).get(0)).getAngerLikelihood();
            String joyLikelihood = ((FaceAnnotation) ((ArrayList)response.get("faceAnnotations")).get(0)).getJoyLikelihood();
            String sorrowLikelihood = ((FaceAnnotation) ((ArrayList)response.get("faceAnnotations")).get(0)).getSorrowLikelihood();
            String surpriseLikelihood = ((FaceAnnotation) ((ArrayList)response.get("faceAnnotations")).get(0)).getSorrowLikelihood();
            String[] likelihoodsa = {angerLikelihood, joyLikelihood, sorrowLikelihood, surpriseLikelihood};
            List<String> likelihoods =  Arrays.asList(likelihoodsa);


            int occurrences = Collections.frequency(likelihoods, "VERY_LIKELY")+
                              Collections.frequency(likelihoods, "LIKELY")+
                              Collections.frequency(likelihoods, "POSSIBLE");

            if (!data.containsKey(appName))
                data.put(appName, new ArrayList<Pair>());

            if (occurrences == 0 || occurrences > 1)
                data.get(appName).add(new Pair(time, Emotion.NEUTRAL));
            else
                for (int i = 0; i < likelihoods.size(); i++){
                    if (likelihoods.get(i).equals("VERY_LIKELY") || likelihoods.get(i).equals("LIKELY") || likelihoods.get(i).equals("POSSIBLE"))
                    {
                        switch (i){
                            case 1:
                                data.get(appName).add(new Pair(time, Emotion.ANGER));
                            case 2:
                                data.get(appName).add(new Pair(time, Emotion.JOY));
                            case 3:
                                data.get(appName).add(new Pair(time, Emotion.SORROW));
                            case 4:
                                data.get(appName).add(new Pair(time, Emotion.SURPRISE));
                        }
                    }
                }

            Log.v("CloudVisionTask", "Hashmap: " + Arrays.toString(data.entrySet().toArray()));
        }catch (Exception e){
            Log.e("CloudVisionTask", e.getLocalizedMessage());
        }

        return null;
    }


    public class Pair implements Comparable<Pair> {
        public String time;

        @Override
        public String toString() {
            return "Pair{" +
                    "time='" + time + '\'' +
                    ", value=" + value +
                    '}';
        }

        public Emotion value;

        public Pair(String time, Emotion value){
            this.time = time;
            this.value = value;
        }

        @Override
        public int compareTo(Pair other) {
            return -1;
        }
    }

    public enum Emotion {
        SORROW, JOY, ANGER, SURPRISE, NEUTRAL;
        public static final Emotion values[] = values();
    }

    public static String getEnumString(Emotion e){
        if (e == Emotion.ANGER){
            return "Anger";
        } else if (e == Emotion.JOY) {
            return "Joy";
        } else if (e == Emotion.SORROW) {
            return "Sorrow";
        } else if (e == Emotion.SURPRISE) {
            return "Surprise";
        } else if (e == Emotion.NEUTRAL) {
            return "Neutral";
        }

        return " ";
    }




}