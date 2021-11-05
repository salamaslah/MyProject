package com.example.moviesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moviesapp.MoviesTypes.Movie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MovieInfoActivity extends AppCompatActivity {
    Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);
        movie= (Movie) getIntent().getSerializableExtra("movie");
        TextView DeleteBtn=findViewById(R.id.DeleteBtn);
        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("movies").child(movie.getId()).removeValue();
                MovieInfoActivity.this.finish();
            }
        });
        final ImageView img=findViewById(R.id.img);
        TextView name=findViewById(R.id.name);
        TextView desc=findViewById(R.id.desc);
        name.setText(movie.getName());
        desc.setText(movie.getDesc());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child("movies/"+movie.getImg()+".jpg");
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(!user.getEmail().equals("mais@gmail.com")){
            DeleteBtn.setVisibility(View.GONE);
        }
        final long size = 500 * 500;
        islandRef.getBytes(size).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0, bytes .length);
                //movie.setBitmap(bitmap);
                img.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
       // img.setImageBitmap(movie.getBitmap());
    }
}
