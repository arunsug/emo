package com.example.aruns.emo;

import android.content.Context;

import java.io.File;
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
    public static HashMap<String, ArrayList<Pair>> data;

    public static final int VERY_LIKELY = 5;
    public static final int LIKELY = 4;
    public static final int POSSIBLE = 3;
    public static final int UNLIKELY = 2;
    public static final int VERY_UNLIKELY = 1;
    public static final int UNKNOWN = 0;

    private static final String DATA_FILE = "emo_data";

    public Information() {
         this.data = new HashMap<>();
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


    public static Emotion getIntAnswer(int[] arr) {
        //int angerS, int joyS, int sorrowS, int surpriseS
        int max = -1;
        int count = 0;
        int index = 0;
        for(int i = 0; i < 4; i++) {
            if(arr[i] > max) {
                max = arr[i];
                count = 1;
                index = i;
            }
            else if(arr[i] == max) count++;
        }
        if(count == 1) {
            if(index == 0) return Emotion.ANGER;
            if(index == 1) return Emotion.JOY;
            if(index == 2) return Emotion.SORROW;
            else return Emotion.SURPRISE;
        }
        else {
            return Emotion.NEUTRAL;
        }
    }

    public static int strToIntVal(String str) {
        if(str.equals("VERY_LIKELY")) return 5;
        if(str.equals("LIKELY")) return 4;
        if(str.equals("POSSIBLE")) return 3;
        if(str.equals("UNLIKELY")) return 2;
        if(str.equals("VERY_UNLIKELY")) return 1;
        else return 0;
    }




    public void createInfoFromMemory(Context context) {
        try {
            File file = context.getFileStreamPath(Information.DATA_FILE);
            if(file == null || !file.exists()) {
                return;
            }

            FileInputStream fileIn = context.getApplicationContext().openFileInput(Information.DATA_FILE);
            ObjectInputStream foodIn = new ObjectInputStream(fileIn);
            Information.information.setData((HashMap<String, ArrayList<Pair>>) foodIn.readObject());
            fileIn.close();
            foodIn.close();

            if(Information.information.getData() == null) {
                throw new NullPointerException("Did not instantiate properly from files/files didnt exist");
            }
        } catch (Exception c) {
            c.printStackTrace();
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
