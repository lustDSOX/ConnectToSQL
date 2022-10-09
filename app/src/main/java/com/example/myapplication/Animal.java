package com.example.myapplication;

import android.graphics.Bitmap;

public class Animal {
    String name,kind,age,weight,id;
    Bitmap image;

    public Animal(String name,String kind,String age,String weight, String id,Bitmap image){
        this.name = name;
        this.kind = kind;
        this.age = age;
        this.weight = weight;
        this.id = id;
        this.image =image;
    }

    public int GetAge(){
        return Integer.parseInt(age);
    }
    public float GetWeight(){
        return Float.parseFloat(weight);
    }
}
