package com.example.moviesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.moviesapp.MoviesTypes.Movie;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    FloatingActionButton addButton;
    Button ActionBtn;
    Button ComedyBtn;
    Button HorrorBtn;
    String selectedCategory="action";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView=findViewById(R.id.recyclerView);
        addButton=findViewById(R.id.addButton);
        ActionBtn=findViewById(R.id.actionBtn);
        ComedyBtn=findViewById(R.id.ComedyBtn);
        HorrorBtn=findViewById(R.id.horrorBtn);
        addButton.setOnClickListener(this);
        ActionBtn.setOnClickListener(this);
        ComedyBtn.setOnClickListener(this);
        HorrorBtn.setOnClickListener(this);
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if(!user.getEmail().equals("mais@gmail.com")){
            addButton.setVisibility(View.GONE);
        }
        getData();
    }
    void getData(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("movies");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //
                Iterator<DataSnapshot> itr= dataSnapshot.getChildren().iterator();
                ArrayList<Movie> movies=new ArrayList<Movie>();
                while (itr.hasNext()){
                    DataSnapshot d=itr.next();
                    Movie movie = d.getValue(Movie.class);
                    movie.setId(d.getKey());
                    if(movie.getCategory()!=null && movie.getCategory().equals(selectedCategory)){
                        movies.add(movie);
                    }

                }
                ListAdapter viewAdapter=new ListAdapter(HomeActivity.this,movies);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                recyclerView.setAdapter(viewAdapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read valu
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addButton:
                Intent i=new Intent(HomeActivity.this,AddMovieActivity.class);
                i.putExtra("category",selectedCategory);
                startActivity(i);
                break;
            case R.id.actionBtn:
                selectedCategory="action";
                getData();
                break;
            case R.id.ComedyBtn:
                selectedCategory="comedy";
                getData();
                break;
            case R.id.horrorBtn:
                selectedCategory="horror";
                getData();
                break;

        }
    }
}
