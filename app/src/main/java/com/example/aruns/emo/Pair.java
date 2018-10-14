package com.example.aruns.emo;

import com.example.aruns.emo.Emotion;

import java.io.Serializable;

public class Pair implements Serializable, Comparable<Pair> {
    public String time;
    public Emotion value;

    @Override
    public String toString() {
        return "Pair{" +
                "time='" + time + '\'' +
                ", value=" + value +
                '}';
    }

    public Pair(String time, Emotion value){
        this.time = time;
        this.value = value;
    }

    @Override
    public int compareTo(Pair other) {
        return -1;
    }
}
