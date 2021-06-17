package ru.hse.java.repetinder.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.chat.Chat;
import ru.hse.java.repetinder.chat.ChatAdapter;
import ru.hse.java.repetinder.notifications.Client;
import ru.hse.java.repetinder.notifications.Data;
import ru.hse.java.repetinder.notifications.MyResponse;
import ru.hse.java.repetinder.notifications.Sender;
import ru.hse.java.repetinder.notifications.Token;
import ru.hse.java.repetinder.user.UserRepetinder;

public class ChatActivity extends AppCompatActivity {
    public static final String TEXT1 = "matchId";
    public static final String TEXT2 = "matchUserRole";

    private RecyclerView.Adapter adapter;
    private final ArrayList<Chat> resultsChats = new ArrayList<>();
    private String currentUId;
    private String chatId;

    private DatabaseReference databaseUser, databaseChat;

    private EditText messageEditText;

    private APIService apiService;
    private String userid;
    private boolean notify = false;

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

        userid = getIntent().getStringExtra("userid");
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(getDataSetChats(), ChatActivity.this);
        recyclerView.setAdapter(adapter);

        messageEditText = findViewById(R.id.message);
        ImageButton sendButton = findViewById(R.id.send);

        sendButton.setOnClickListener(v -> sendMessage(userid));

        /* while (true) {
            if (FirebaseMessaging.getInstance().getToken().isComplete()) {
                updateToken(FirebaseMessaging.getInstance().getToken().getResult());
                break;
            }
        } */
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token t = new Token(token);
        reference.child(currentUId).setValue(t);
    }

    private FirebaseDatabase getDatabaseInstance() {
        return FirebaseDatabase.getInstance("https://repetinder-cb68d-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    private void sendMessage(final String receiver) {
        String message = messageEditText.getText().toString();

        if (!message.isEmpty()) {
            String currentChatId = databaseChat.push().getKey();
            DatabaseReference newMessageDb = databaseChat.child(currentChatId);

            Map newMessage = new HashMap();
            newMessage.put("SendBy", currentUId);
            newMessage.put("Message", message);

            newMessageDb.setValue(newMessage);
            notify = true;
        }
        messageEditText.setText(null);

        final String msg = message;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(currentUId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserRepetinder user = dataSnapshot.getValue(UserRepetinder.class);
                if (notify) {
                    sendNotifiaction(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifiaction(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(currentUId, R.mipmap.ic_launcher, username + ": " + message, "New Message", userid);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<MyResponse> call, @NonNull Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<MyResponse> call, @NonNull Throwable t) {
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private List<Chat> getDataSetChats() {
        return resultsChats;
    }
}
