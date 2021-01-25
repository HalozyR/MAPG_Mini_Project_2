package ece.np.edu.miniproject2;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import ece.np.edu.miniproject2.databinding.ActivityImageBinding;

public class ImageActivity extends AppCompatActivity implements ImageAdapter.onItemClickListener {

    ActivityImageBinding binding;
    private ImageAdapter imageAdapter;
    private ValueEventListener mDBListener;
    private FirebaseStorage mStorage;
    private DatabaseReference mRef;
    private List<Image> imageList;
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Images");
        fAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fAuth.getCurrentUser();
        String currentUserUID = currentUser.getUid();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageList = new ArrayList<>();
        imageAdapter = new ImageAdapter(getApplicationContext(), imageList);
        binding.recyclerView.setAdapter(imageAdapter);
        imageAdapter.setOnItemClickListener(ImageActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("Storage").child("Users").child(currentUserUID).child("Images");

        mDBListener = mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                imageList.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Image retrieveImage = postSnapshot.getValue(Image.class);
                    retrieveImage.setKey(postSnapshot.getKey());
                    imageList.add(retrieveImage);
                }
                imageAdapter.notifyDataSetChanged();
                binding.pbImage.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                binding.pbImage.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onSelectClick(int position) {
        Toast.makeText(this, "Long press for option to delete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Image selectedItem = imageList.get(position);
        String selectedKey = selectedItem.getKey();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRef.child(selectedKey).removeValue();
                Toast.makeText(ImageActivity.this, "Image deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRef.removeEventListener(mDBListener);
    }
}