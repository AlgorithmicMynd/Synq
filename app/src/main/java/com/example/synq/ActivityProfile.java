package com.example.synq;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.bumptech.glide.Glide;  // <-- Important if you want to load profile images

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityProfile extends AppCompatActivity {

    MaterialCardView usernameCard, emailCard, buttonCard;

    CircleImageView profile;
    TextView usernameTextView, emailTextView;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // View Bindings
        usernameCard = findViewById(R.id.usernameCard);
        emailCard = findViewById(R.id.emailCard);
//        buttonCard = findViewById(R.id.buttonCard);
        profile = findViewById(R.id.profileimage);
//        cardButton = findViewById(R.id.cardButton);
        usernameTextView = findViewById(R.id.UserNameTxtView);
        emailTextView = findViewById(R.id.UserEmailTxtView);

        // Firebase Instances
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        reference = database.getReference().child("users");

        // Fetch and Display User Data
        loadUserProfile();


    }

    private void loadUserProfile() {
        String uid = auth.getCurrentUser().getUid();

        reference.child(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    String username = task.getResult().child("userName").getValue(String.class);
                    String email = task.getResult().child("mail").getValue(String.class);
                    String profileImageUrl = task.getResult().child("profilepic").getValue(String.class);

                    usernameTextView.setText("Username: " + username);
                    emailTextView.setText("Email: " + email);

                    // Load profile image if available
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        String cleanUrl = profileImageUrl.replace("\\/", "/");
                        Glide.with(ActivityProfile.this)
                                .load(cleanUrl)
                                .placeholder(R.drawable.photocamera)
                                .into(profile);
                    }

                } else {
                    Toast.makeText(ActivityProfile.this, "User data not found!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ActivityProfile.this, "Failed to fetch user data!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
