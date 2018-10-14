package com.example.aruns.emo;

public class Graph {
    @Override
    public String toString() {
        return "Graph{" +
                "key='" + key + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Graph(String key) {

        this.key = key;
    }

    private String key;
}
