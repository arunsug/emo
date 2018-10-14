package com.example.aruns.emo;

import android.content.Context;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CloudVisionTask extends AsyncTask<Void, Void, Void> {

    public Vision vision;
    public byte[] imageData;
    public String appName;
    public long time;
    public Context context;

    public CloudVisionTask(byte[] imageData, String appName, long time, Vision vision, Context context) {
        this.imageData = imageData;
        this.appName = appName;
        this.time = time;
        this.vision = vision;
        this.context = context;
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

            if (!Information.information.data.containsKey(appName))
                Information.information.data.put(appName, new ArrayList<Pair>());

            if (occurrences == 0 || occurrences > 1)
                Information.information.data.get(appName).add(new Pair(time, Emotion.NEUTRAL));
            else
                for (int i = 0; i < likelihoods.size(); i++){
                    if (likelihoods.get(i).equals("VERY_LIKELY") || likelihoods.get(i).equals("LIKELY") || likelihoods.get(i).equals("POSSIBLE"))
                    {
                        switch (i){
                            case 1:
                                Information.information.data.get(appName).add(new Pair(time, Emotion.ANGER));
                                Information.information.createInfoFromMemory(this.context);
                            case 2:
                                Information.information.data.get(appName).add(new Pair(time, Emotion.JOY));
                                Information.information.createInfoFromMemory(this.context);
                            case 3:
                                Information.information.data.get(appName).add(new Pair(time, Emotion.SORROW));
                                Information.information.createInfoFromMemory(this.context);
                            case 4:
                                Information.information.data.get(appName).add(new Pair(time, Emotion.SURPRISE));
                                Information.information.createInfoFromMemory(this.context);
                        }
                    }
                }

            Log.v("CloudVisionTask", "Hashmap: " + Arrays.toString(Information.information.data.entrySet().toArray()));
        }catch (Exception e){
            Log.e("CloudVisionTask", e.getLocalizedMessage());
        }

        return null;
    }

}