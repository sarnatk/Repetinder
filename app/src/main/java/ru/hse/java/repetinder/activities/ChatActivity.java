package ru.hse.java.repetinder.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.chat.Chat;
import ru.hse.java.repetinder.chat.ChatAdapter;

public class ChatActivity extends AppCompatActivity {
    public static final String TEXT1 = "matchId";
    public static final String TEXT2 = "matchUserRole";

    private RecyclerView.Adapter adapter;
    private final ArrayList<Chat> resultsChats = new ArrayList<>();
    private String currentUId;
    private String chatId;

    private DatabaseReference databaseUser, databaseChat;

    private EditText messageEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        String matchId = getIntent().getExtras().getString(TEXT1);
        String matchUserRole = getIntent().getExtras().getString(TEXT2);
        currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        databaseUser = getDatabaseInstance().getReference().child("Users").child(matchUserRole).child(matchId)
                .child("Connections").child("Yes").child(currentUId).child("ChatId");
        databaseChat = getDatabaseInstance().getReference().child("Chats");
        getChatId();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(getDataSetChats(), ChatActivity.this);
        recyclerView.setAdapter(adapter);

        messageEditText = findViewById(R.id.message);
        ImageButton sendButton = findViewById(R.id.send);

        sendButton.setOnClickListener(v -> sendMessage());
    }

    private FirebaseDatabase getDatabaseInstance() {
        return FirebaseDatabase.getInstance("https://repetinder-cb68d-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    private void sendMessage() {
        String message = messageEditText.getText().toString();

        if (!message.isEmpty()) {
            String currentChatId = databaseChat.push().getKey();
            DatabaseReference newMessageDb = databaseChat.child(currentChatId);

            Map newMessage = new HashMap();
            newMessage.put("SendBy", currentUId);
            newMessage.put("Message", message);

            newMessageDb.setValue(newMessage);
        }
        messageEditText.setText(null);
    }

    private void getChatId() {
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    chatId = Objects.requireNonNull(snapshot.getValue()).toString();
                    databaseChat = databaseChat.child(chatId);
                    getChatMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChatMessages() {
        databaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    String message = null, sendBy = null;

                    if (snapshot.child("Message").getValue() != null) {
                        message = Objects.requireNonNull(snapshot.child("Message").getValue()).toString();
                    }
                    if (snapshot.child("SendBy").getValue() != null) {
                        sendBy = Objects.requireNonNull(snapshot.child("SendBy").getValue()).toString();
                    }

                    if (message != null && sendBy != null) {
                        boolean isCurrentUser = false;
                        if (sendBy.equals(currentUId)) {
                            isCurrentUser = true;
                        }

                        Chat newMessage = new Chat(message, isCurrentUser);
                        resultsChats.add(newMessage);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private List<Chat> getDataSetChats() {
        return resultsChats;
    }
}
