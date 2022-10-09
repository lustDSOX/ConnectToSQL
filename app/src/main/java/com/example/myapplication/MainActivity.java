package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    Connection connect;
    String ConnectionResult = "";
    ArrayList<Animal> animalList = new ArrayList<>();
    ArrayList<Animal> animalList_s  ;
    ListView item_list;
    Intent add_activity;
    Intent item_activity;
    EditText txt_search;
    AnimalAdapter adapter;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] sort_name = { "По кол-ву лет", "По весу", "По имени", "По виду"};
        spinner= findViewById(R.id.spinner);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sort_name);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
        item_list = findViewById(R.id.item_list);
        txt_search = findViewById(R.id.txtsearch);
        add_activity = new Intent(MainActivity.this,AddActivity.class);
        item_activity = new Intent(MainActivity.this,ItemActivity.class);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position) {
                    case 0:
                        Collections.sort(animalList, new Comparator<Animal>() {
                            @Override
                            public int compare(Animal o1, Animal o2) {
                                return o1.GetAge()-o2.GetAge();
                            }
                        });
                        adapter = new AnimalAdapter(MainActivity.this, animalList);
                        item_list.setAdapter(adapter);

                        break;
                    case 1:
                        Collections.sort(animalList, new Comparator<Animal>() {
                            @Override
                            public int compare(Animal o1, Animal o2) {
                                return Float.compare(o1.GetWeight(), o2.GetWeight());
                            }
                        });
                        adapter = new AnimalAdapter(MainActivity.this, animalList);
                        item_list.setAdapter(adapter);
                        break;
                    case 2:
                        Collections.sort(animalList, new Comparator<Animal>() {
                            @Override
                            public int compare(Animal o1, Animal o2) {
                                return o1.name.compareTo(o2.name);
                            }
                        });
                        adapter = new AnimalAdapter(MainActivity.this, animalList);
                        item_list.setAdapter(adapter);
                    break;
                    case 3:
                        Collections.sort(animalList, new Comparator<Animal>() {
                            @Override
                            public int compare(Animal o1, Animal o2) {
                                return o1.kind.compareTo(o2.kind);
                            }
                        });
                        adapter = new AnimalAdapter(MainActivity.this, animalList);
                        item_list.setAdapter(adapter);
                        break;
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });
        GetAnimalList();

       item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Animal item = (Animal) item_list.getItemAtPosition(i);
               item_activity.putExtra("name",item.name);
               item_activity.putExtra("kind",item.kind);
               item_activity.putExtra("age",item.age);
               item_activity.putExtra("weight",item.weight);
               item_activity.putExtra("id",item.id);
               if(item.image != null) {
                   item_activity.putExtra("image",encodeImage(item.image));
               }
               startActivity(item_activity);
           }
       });

       txt_search.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence s, int i, int i1, int i2) {
               if(s.toString().equals("")){
                   // reset listview
                   adapter = new AnimalAdapter(MainActivity.this,animalList_s);
                   item_list.setAdapter(adapter);
               } else {
                   for(Animal item:animalList_s){
                       if(!item.name.contains(s.toString())){
                           animalList.remove(item);
                       }
                   }
                   adapter.notifyDataSetChanged();
               }
           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });

    }
    private String encodeImage(Bitmap bitmap) {
        int prevW = 150;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();
        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        return "";
    }

    public void AddAnimal(View v){
        startActivity(add_activity);
    }
    public void UpdateList(View v){GetAnimalList();}

    public void GetAnimalList(){
        try{
            animalList.clear();
            connect = SQLConnection.connect();
            if(connect != null){
                String qu = "select * from animal";
                Statement statement = connect.createStatement();
                ResultSet resultSet = statement.executeQuery(qu);
                while (resultSet.next()){
                    animalList.add(new Animal(resultSet.getString("nickname_animal"),resultSet.getString("kind"),resultSet.getString("age"),resultSet.getString("weight_animal"),resultSet.getString("id_animal"),
                            getImageBitmap(resultSet.getString("image"))));
                }
                ConnectionResult = "Success";
                adapter = new AnimalAdapter(this,animalList);
                item_list.setAdapter(adapter);
                animalList_s = (ArrayList<Animal>) animalList.clone();
            }
            else {
                ConnectionResult = "Failed";
            }
            Log.d(ConnectionResult,"");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Log.d(ConnectionResult, throwables.getMessage());
        }
    }
    private Bitmap getImageBitmap(String encodedImg) {
        if (encodedImg != null) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return BitmapFactory.decodeResource(this.getResources(),
                R.drawable.icon);
    }

}