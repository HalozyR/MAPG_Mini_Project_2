package ece.np.edu.miniproject2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ece.np.edu.miniproject2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private FirebaseAuth fAuth;
    private static final int REQ_CODE_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("DS Storage");

        binding.btGo.setOnClickListener(v -> {
            CheckConnection connection = new CheckConnection();
            if(!connection.isConnected(getApplicationContext())){
                Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.i(TAG, "Go Button click");
            fAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = fAuth.getCurrentUser();
            if (currentUser != null) {
                Intent i = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(i);
                finishAffinity();
            } else {
                binding.btGo.setVisibility(View.GONE);
                binding.btLogin.setVisibility(View.VISIBLE);
                binding.btSignUp.setVisibility(View.VISIBLE);
            }
        });

        binding.btLogin.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            binding.btSignUp.setVisibility(View.GONE);
            binding.btLogin.setVisibility(View.GONE);
            binding.btGo.setVisibility(View.VISIBLE);
        });

        binding.btSignUp.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
            binding.btSignUp.setVisibility(View.GONE);
            binding.btLogin.setVisibility(View.GONE);
            binding.btGo.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}