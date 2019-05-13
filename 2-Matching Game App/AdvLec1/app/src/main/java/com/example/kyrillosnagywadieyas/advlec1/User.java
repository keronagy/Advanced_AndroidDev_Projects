package com.example.kyrillosnagywadieyas.advlec1;

public class User {
    private int id;
    private String name;
    private int score;
    private String timeStamp;

    public User(int id,String name,int score, String time)
    {
        this.id=id;
        this.name= name;
        this.score=score;
        this.timeStamp=time;
    }
    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setId(int id) {
        this.id = id;
    }
}
