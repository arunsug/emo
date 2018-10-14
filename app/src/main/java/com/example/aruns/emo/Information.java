package com.example.aruns.emo;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Information implements Serializable {
    public static Information information;
    public static HashMap<String, ArrayList<Pair>> data = new HashMap<>();

    private static final String DATA_FILE = "emo_data";

    public Information() {
    };


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


    public boolean createInfoFromMemory(Context context) {
        try {

            FileInputStream fileIn = context.getApplicationContext().openFileInput(Information.DATA_FILE);
            ObjectInputStream foodIn = new ObjectInputStream(fileIn);
            Information.information.setData((HashMap<String, ArrayList<Pair>>) foodIn.readObject());
            fileIn.close();
            foodIn.close();

            if(Information.information.getData() == null) {
                throw new NullPointerException("Did not instantiate properly from files/files didnt exist");
            }

            return true;
        } catch (Exception c) {
            c.printStackTrace();
            return false;
        }
    }

    public boolean writeInfoToMemory(Context context) {
        try {
            this.deleteFiles(context);

            FileOutputStream foodFileOut =
                    context.getApplicationContext().openFileOutput(Information.DATA_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream foodOut = new ObjectOutputStream(foodFileOut);
            foodOut.writeObject(Information.information.getData());
            foodFileOut.close();
            foodOut.close();

            return true;
        } catch (Exception c) {
            c.printStackTrace();
            return false;
        }
    }

    public void deleteFiles(Context context) {
        context.getApplicationContext().deleteFile(Information.DATA_FILE);
    }

    public void setData(HashMap<String, ArrayList<Pair>> myData) {
        this.data = myData;
    }

    public HashMap<String, ArrayList<Pair>> getData() {
        return this.data;
    }
}
