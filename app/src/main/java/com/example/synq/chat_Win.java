package com.example.synq;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class chat_Win extends AppCompatActivity {

    String reciverimg, reciverName, reciverUid, SenderUID;
    CircleImageView profile;
    TextView reciverNName;
    CardView sendbtn;
    EditText textmsg;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    public static String senderImg;
    public static String reciverIImg;
    String senderRoom, reciverRoom;
    RecyclerView mmessagesAdpter;
    ArrayList<msgModelclass> messagesArrayList;
    messageAdpter messagesAdpter;

    private static final int SHIFT = 3;
    private static final String SECRET_KEY = "SecretSynQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_win);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        reciverName = getIntent().getStringExtra("nameeee");
        reciverimg = getIntent().getStringExtra("reciverImg");
        reciverUid = getIntent().getStringExtra("uid");

        sendbtn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);
        profile = findViewById(R.id.profileimgg);
        reciverNName = findViewById(R.id.recivername);
        mmessagesAdpter = findViewById(R.id.msgadpter);
        messagesArrayList = new ArrayList<>();

        messagesAdpter = new messageAdpter(messagesArrayList, chat_Win.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagesAdpter.setLayoutManager(linearLayoutManager);
        mmessagesAdpter.setAdapter(messagesAdpter);

        reciverNName.setText(reciverName);
        Picasso.get().load(reciverimg).into(profile);

        SenderUID = firebaseAuth.getUid();
        senderRoom = SenderUID + reciverUid;
        reciverRoom = reciverUid + SenderUID;

        DatabaseReference reference = database.getReference().child("users").child(SenderUID);
        DatabaseReference chat_reference = database.getReference().child("chats").child(senderRoom).child("messages");

        chat_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    msgModelclass messages = dataSnapshot.getValue(msgModelclass.class);

                    if (messages != null) {
                        messages.setMessage(decrypt(messages.getMessage()));
                        messagesArrayList.add(messages);
                    }
                }
                messagesAdpter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(chat_Win.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.child("profilepic").getValue() != null) {
                    senderImg = snapshot.child("profilepic").getValue().toString();
                }
                reciverIImg = reciverimg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(chat_Win.this, "Failed to load profile image", Toast.LENGTH_SHORT).show();
            }
        });

        sendbtn.setOnClickListener(view -> {
            String message = textmsg.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(chat_Win.this, "Enter a message first...", Toast.LENGTH_SHORT).show();
                return;
            }

            textmsg.setText("");
            Date date = new Date();
            String encryptedMessage = encrypt(message);
            msgModelclass messagesss = new msgModelclass(encryptedMessage, SenderUID, date.getTime());

            DatabaseReference senderRef = database.getReference().child("chats").child(senderRoom).child("messages");

            if (SenderUID.equals(reciverUid)) {
                senderRef.push().setValue(messagesss)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(chat_Win.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                senderRef.push().setValue(messagesss)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                database.getReference().child("chats").child(reciverRoom).child("messages")
                                        .push().setValue(messagesss);
                            }
                        });
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public static String encrypt(String message) {
        StringBuilder encrypted = new StringBuilder();

        for (char ch : message.toCharArray()) {
            encrypted.append((char) (ch + SHIFT));
        }

        return xorWithKey(encrypted.toString(), SECRET_KEY);
    }

    public static String decrypt(String encryptedMessage) {
        String xorDecrypted = xorWithKey(encryptedMessage, SECRET_KEY);

        StringBuilder decrypted = new StringBuilder();
        for (char ch : xorDecrypted.toCharArray()) {
            decrypted.append((char) (ch - SHIFT));
        }

        return decrypted.toString();
    }

    private static String xorWithKey(String text, String key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            result.append((char) (text.charAt(i) ^ key.charAt(i % key.length())));
        }
        return result.toString();
    }
}
