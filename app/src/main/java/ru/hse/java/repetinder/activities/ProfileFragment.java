package ru.hse.java.repetinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ru.hse.java.repetinder.R;
import ru.hse.java.repetinder.photo.PhotoWorker;
import ru.hse.java.repetinder.user.Storage;
import ru.hse.java.repetinder.user.UserRepetinder;

public class ProfileFragment extends Fragment {
    private static final int GALLERY_REQUEST = 1;

    private View view;
    private DatabaseReference mCustomerDatabase;
    private String currentUId;
    private String userRole;
    private String profileImageUrl;
    private ImageView profileView;

    private FirebaseDatabase getDatabaseInstance() {
        return FirebaseDatabase.getInstance("https://repetinder-cb68d-default-rtdb.europe-west1.firebasedatabase.app/");
    }

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        Storage storage = (Storage) this.getArguments().getSerializable(MainActivity.TEXT);
        userRole = storage.userRole;
        currentUId = storage.userId;

        TextView userEmail = view.findViewById(R.id.mailProfile);
        TextView userFullname = view.findViewById(R.id.fullnameProfile);
        TextView userRoleView = view.findViewById(R.id.userRoleProfile);
        TextView userUsername = view.findViewById(R.id.usernameProfile);
        TextView userSubject = view.findViewById(R.id.subjectProfile);

        userRoleView.setText(String.format("Status: %s", userRole));

        UserRepetinder currentUser = storage.currentUser;
        userFullname.setText(currentUser.getFullname());
        userUsername.setText(currentUser.getUsername());
        userEmail.setText(currentUser.getEmail());
        String subject = currentUser.getSubject().toString();
        userSubject.setText(subject.substring(0, 1)  + subject.substring(1).toLowerCase());


        Button logOutButton = view.findViewById(R.id.logOut);
        profileView = view.findViewById(R.id.profileImage);

        setProfilePhoto();

        logOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        profileView.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            // TODO: fix deprecated
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });

        return view;
    }

    private void setProfilePhoto() {
        mCustomerDatabase = getDatabaseInstance().getReference().child("Users").child(userRole).child(currentUId);
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                    if (Objects.requireNonNull(map).get("profileImageUrl") != null) {
                        profileImageUrl = Objects.requireNonNull(map.get("profileImageUrl")).toString();
                        if (!profileImageUrl.equals("default")) {
                            Glide.with(getActivity().getApplication()).load(profileImageUrl).into(profileView);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Bitmap bitmap = null;
        ImageView profileView = view.findViewById(R.id.profileImage);

        if (requestCode == GALLERY_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = imageReturnedIntent.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    bitmap = PhotoWorker.cropBitmap(bitmap);
                    bitmap = PhotoWorker.createResizedBitmap(bitmap);
                    addImageToDatabase(bitmap);
                } catch (IOException e) {
                    Toast.makeText(getActivity(), "Can't load image", Toast.LENGTH_SHORT).show();
                }
                profileView.setImageBitmap(bitmap);
            }
        }
    }

    private void addImageToDatabase(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(currentUId);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] data = stream.toByteArray();
        UploadTask uploadTask = filepath.putBytes(data);
        uploadTask.addOnFailureListener(e -> getActivity().finish());
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            if (taskSnapshot.getMetadata() != null) {
                if (taskSnapshot.getMetadata().getReference() != null) {
                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(uri -> {
                        Map userInfo = new HashMap();
                        userInfo.put("profileImageUrl", uri.toString());
                        mCustomerDatabase.updateChildren(userInfo);
                    });
                }
            }
        });
    }
}
