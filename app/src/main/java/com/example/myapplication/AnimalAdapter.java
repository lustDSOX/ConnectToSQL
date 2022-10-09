package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Base64;

public class AnimalAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Animal> animals;

    AnimalAdapter(Context context, ArrayList<Animal> animals) {
        ctx = context;
        this.animals = animals;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return animals.size();
    }

    @Override
    public Object getItem(int i) {
        return animals.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_layout, parent, false);
        }

        Animal animal = getAnimal(i);

        ((TextView) view.findViewById(R.id.animal_name)).setText(animal.name );
        ((TextView) view.findViewById(R.id.kind)).setText(animal.kind +" (" +animal.weight+")");
        ((TextView) view.findViewById(R.id.age)).setText(animal.age + " age");
        if(animal.image != null){
            ((ImageView) view.findViewById(R.id.image)).setImageBitmap(animal.image);
        }
        else {
            ((ImageView) view.findViewById(R.id.image)).setImageResource(R.drawable.icon);
        }
        return view;
    }
    Animal getAnimal(int position) {
        return ((Animal) getItem(position));
    }



}
