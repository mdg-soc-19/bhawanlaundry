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



}
