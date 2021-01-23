package ece.np.edu.miniproject2;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import ece.np.edu.miniproject2.databinding.ActivityDashBoardBinding;

public class UploadActivity extends AppCompatActivity {

    private final static String TAG = "DashBoard";
    private String strCurrentUserUID;
    private Intent i;
    private ActivityDashBoardBinding binding;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri ImageUri;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dBRef, nRef;
    private FirebaseStorage fStorage;
    private StorageReference sRef;
    private FirebaseAuth fAuth;
    private FirebaseUser currentUser;
    private StorageTask uploadTask;
    private DataSnapshot dataSnapshot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Upload Image");
        fAuth = FirebaseAuth.getInstance();
        currentUser = fAuth.getCurrentUser();
        strCurrentUserUID = currentUser.getUid();
        fStorage = FirebaseStorage.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        //Adding username
        nRef = fDatabase.getReference("Users").child(strCurrentUserUID);
        nRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users currentUser = snapshot.getValue(Users.class);
                binding.tvWelcome.setVisibility(View.VISIBLE);
                String name = currentUser.getName();
                binding.tvWelcome.setText("Welcome "+name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UploadActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        sRef = fStorage.getReference("Users").child(strCurrentUserUID).child("Images");
        dBRef = fDatabase.getReference("Storage").child("Users").child(strCurrentUserUID).child("Images");
        Log.i(TAG, "Current UserID: " + strCurrentUserUID);

        binding.btChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadActivity.this.OpenFile();
            }
        });

        binding.btUpload.setOnClickListener(v -> {
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(getApplicationContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            }
            UploadFile();
        });

        binding.btLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            i = new Intent(UploadActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        });
    }

    private void UploadFile() {
        String strfileName = binding.etFileName.getText().toString();
        if (ImageUri != null) {
            //Uploading image to storage with current system time in millis as file name with image extension
            StorageReference fileUpload = sRef.child(System.currentTimeMillis() + "." + GetExtension(ImageUri));
            fileUpload.putFile(ImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100 * snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                    binding.progressBar3.setProgress((int) progress);
                    binding.btUpload.setClickable(false);
                }
            }).addOnSuccessListener(taskSnapshot -> {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.progressBar3.setProgress(0);
                        binding.btUpload.setClickable(true);
                    }
                },1000);
                Toast.makeText(getApplicationContext(), "Upload success!", Toast.LENGTH_SHORT).show();
            }).continueWithTask(task -> {
                if(!task.isSuccessful())
                    throw task.getException();
                else
                    return fileUpload.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Task is successful");
                    Uri downloadUri = task.getResult();
                    Log.i(TAG, downloadUri.toString());
                    UploadImage upload = new UploadImage(strfileName, downloadUri.toString());
                    dBRef.push().setValue(upload);
                    Toast.makeText(UploadActivity.this.getApplicationContext(), "Upload success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UploadActivity.this.getApplicationContext(), "Upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Please select an image to upload", Toast.LENGTH_SHORT).show();
        }
    }

    //Needs review
    String GetExtension(Uri uri) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void OpenFile() {
        i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageUri = data.getData();
            Log.i(TAG, "ImageUri: " + ImageUri.toString());
            binding.ivImageUpload.setImageURI(ImageUri);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    public void ShowUploads(View view) {
        openImagesActivity();
    }

    private void openImagesActivity() {
        Intent i = new Intent(this, ImageActivity.class);
        startActivity(i);
    }
}