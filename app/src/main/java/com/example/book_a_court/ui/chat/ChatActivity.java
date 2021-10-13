package com.example.book_a_court.ui.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book_a_court.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    String ReceiverName,ReceiverUid,SenderUid;
    static String senderRoom,receiverRoom;
    TextView receiverName;
    CardView sendMessage,sendBtn;
    EditText edtMsg;
    FirebaseFirestore fStore;
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView messageRecycler;
    MessageAdapter messageAdapter;
    ArrayList<Messages> messagesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ReceiverName = getIntent().getStringExtra("name");
        ReceiverUid = getIntent().getStringExtra("uid");

        receiverName = findViewById(R.id.chatpage_name);
        receiverName.setText(ReceiverName);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();

        sendBtn = findViewById(R.id.send_btn);
        sendMessage = findViewById(R.id.send_message);
        edtMsg = findViewById(R.id.edtMsg);
        messageRecycler = findViewById(R.id.messages);
        SenderUid = auth.getUid();
        messagesArrayList = new ArrayList<>();

        senderRoom = SenderUid + ReceiverUid;
        receiverRoom = ReceiverUid + SenderUid;

        DatabaseReference chatReference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edtMsg.getText().toString();
                if(msg.isEmpty()){
                    Toast.makeText(ChatActivity.this, "Please enter valid message", Toast.LENGTH_SHORT).show();
                    return;
                }
                edtMsg.setText("");
                Date date = new Date();

                Messages messages = new Messages(msg,SenderUid,ReceiverUid,date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        database.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                                Toast.makeText(ChatActivity.this, "Data send", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });

        messageRecycler =findViewById(R.id.messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageRecycler.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(ChatActivity.this,messagesArrayList);
        messageRecycler.setAdapter(messageAdapter);
    }
}