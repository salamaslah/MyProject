package com.example.moviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.moviesapp.MoviesTypes.Movie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Random;

public class AddMovieActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView img;
    Button selectImgButton;
    EditText name;
    EditText desc;
    Button add;
    Uri imgUri;
    String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        category=getIntent().getStringExtra("category");
        img=findViewById(R.id.img);
        selectImgButton=findViewById(R.id.selectImageButton);
        name=findViewById(R.id.name);
        desc=findViewById(R.id.desc);
        add=findViewById(R.id.addButton);
        selectImgButton.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.selectImageButton:
                selectImage();
                break;
            case R.id.addButton:
                addMovie();
                break;
        }
    }
    void selectImage(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1){
            imgUri = data.getData();
            img.setImageURI(imgUri);
        }
    }
    void addMovie(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Random random=new Random();
        String img=name.getText().toString()+ random.nextInt()+".jpg";
        final Movie movie=new Movie(name.getText().toString(),desc.getText().toString(),img,category);
        database.child("movies").push().setValue(movie).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                uploadImageToFireBase(movie);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddMovieActivity.this,"error",Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void uploadImageToFireBase(Movie movie){
        try{
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("movies/"+movie.getImg());
            //Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() ,imgUri);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(AddMovieActivity.this,"error",Toast.LENGTH_LONG).show();
                    //addItemProgressBar.setVisibility(View.GONE);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddMovieActivity.this,"added successfuly",Toast.LENGTH_LONG).show();
                    //addItemProgressBar.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            Toast.makeText(AddMovieActivity.this,"error",Toast.LENGTH_LONG).show();
        }

    }
}
