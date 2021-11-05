package com.example.moviesapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp.MoviesTypes.Movie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter {
    private List<Movie> movies;
    private LayoutInflater inflater;
    private Context context;

    public ListAdapter(Context context, List<Movie> data) {
        this.inflater = LayoutInflater.from(context);
        this.movies = data;
        this.context=context;
    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        TextView name=holder.itemView.findViewById(R.id.name);
        TextView desc=holder.itemView.findViewById(R.id.desc);
        final ImageView imageView=holder.itemView.findViewById(R.id.imageView);

        name.setText(movie.getName());
        desc.setText(movie.getDesc());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference islandRef = storageRef.child("movies/"+movie.getImg()+".jpg");

        final long size = 500 * 500;
        islandRef.getBytes(size).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0, bytes .length);
                //movie.setBitmap(bitmap);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MovieInfoActivity.class);
                intent.putExtra("movie",movie);
                context.startActivity(intent);
            }
        });
    }





    public int getItemCount() {
        return movies.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView titleTextView;
        TextView descTextView;
        ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.name);
            descTextView=itemView.findViewById(R.id.desc);

        }

    }

}
