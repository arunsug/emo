package com.example.aruns.emo;

public class EmotionDataPoint {
    private String emotion;
    private String value;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "EmotionDataPoint{" +
                "emotion='" + emotion + '\'' +
                ", value='" + value + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public EmotionDataPoint(String emotion, String value, String time) {

        this.emotion = emotion;
        this.value = value;
        this.time = time;
    }
}
