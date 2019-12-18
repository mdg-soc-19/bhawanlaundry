package com.example.appone;

public class StudentUser {
    String name, room, bucketColor;

    int numBuckets;

    public  StudentUser(){}

    public StudentUser(String n, String r){
        name = n;
        room = r;
    }
    public String getRoom() {
        return room;
    }

    public String getName() {
        return name;
    }

    public void setBucketColor(String b){
        bucketColor = b;
    }

    public void setNumClothes(int numBuckets) {
        this.numBuckets = numBuckets;
    }
}
