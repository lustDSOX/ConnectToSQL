package com.example.myapplication;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    Connection connect;
    TextInputLayout name;
    TextInputLayout kind;
    TextInputLayout age;
    TextInputLayout weight;
    ImageView image;
    String encodedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);
        name = findViewById(R.id.text_name);
        kind = findViewById(R.id.text_kind);
        age = findViewById(R.id.text_age);
        weight = findViewById(R.id.text_weight);
        image = findViewById(R.id.image);
    }

    public void ImageClick(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImg.launch(intent);
    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    image.setImageBitmap(bitmap);
                    encodedImage = encodeImage(bitmap);
                } catch (Exception e) {

                }
            }
        }
    });
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

    public void AddAnimal(View view){
        try{
            SQLConnection connection = new SQLConnection();
            connect = connection.connect();
            String qu = "insert into animal values("
                    + Float.parseFloat(String.valueOf(weight.getEditText().getText()))
                    + ",\'" + Objects.requireNonNull(name.getEditText()).getText()
                    + "\',\'" + Objects.requireNonNull(kind.getEditText()).getText()+
                    "\'," + Integer.parseInt(String.valueOf(age.getEditText().getText()))+
                    ",\'" +encodedImage +
                    "\')";
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery(qu);
            connect.close();
            ((MainActivity)getBaseContext()).GetAnimalList();
            this.finish();
            Log.d("", String.valueOf((resultSet.last())));
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            Log.d("Error - ",throwables.getMessage());
        }
    }

    public void BackBtn(View v1){
        this.finish();
    }
}
