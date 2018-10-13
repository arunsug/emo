package com.example.aruns.emo;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.FaceAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.util.Arrays;
import java.util.List;

public class CloudVisionTask extends AsyncTask<Void, Void, Void> {
    public Vision vision;
    public byte[] imageData;

    public CloudVisionTask(byte[] imageData, Vision vision) {
        this.imageData = imageData;
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
            Log.v("CloudVisionTask", "Here");

            //List<FaceAnnotation> faces = batchResponse.getResponses()
            //        .get(0).getFaceAnnotations();

            Log.v("CloudVisionTask", "Received: "+batchResponse.getResponses().size());
        }catch (Exception e){
            Log.e("CloudVisionTask", e.getLocalizedMessage());
        }

        return null;
    }


}