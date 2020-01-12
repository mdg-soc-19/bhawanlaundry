package com.example.android.bhawanlaundry;

import com.google.type.Date;

import java.io.Serializable;

public class Task {
    String bucketColour, nameOfStudent, roomOfStudent;
    int numberOfClothes;

    public Task(){}

    public Task(String name, String room, int num, String colour){
        this.bucketColour = colour;
        this.nameOfStudent = name;
        this.numberOfClothes = num;
        this.roomOfStudent = room;
    }


    public void setBucketColour(String bucketColour) {
        this.bucketColour = bucketColour;
    }

    public void setNameOfStudent(String nameOfStudent) {
        this.nameOfStudent = nameOfStudent;
    }

    public void setNumberOfClothes(int numberOfClothes) {
        this.numberOfClothes = numberOfClothes;
    }

    public void setRoomOfStudent(String roomOfStudent) {
        this.roomOfStudent = roomOfStudent;
    }

    public String getBucketColour() {
        return bucketColour;
    }

    public int getNumberOfClothes() {
        return numberOfClothes;
    }

    public String getNameOfStudent() {
        return nameOfStudent;
    }

    public String getRoomOfStudent() {
        return roomOfStudent;
    }


}
