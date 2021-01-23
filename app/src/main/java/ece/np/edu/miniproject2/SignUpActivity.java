package ece.np.edu.miniproject2;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

import ece.np.edu.miniproject2.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference dBRef;
    private ActivitySignUpBinding binding;
    private FirebaseAuth fAuth;
    private String TAG = "SignUp";
    private Intent i;

    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = fAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(SignUpActivity.this,MainActivity.class);
            finish();
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("SignUp Page");
        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        binding.btRegister.setOnClickListener(v -> {
            String strEmail = binding.etEmail.getText().toString().trim();
            String strPassword = binding.etPassword.getText().toString().trim();
            String strName = binding.etName.getText().toString().trim();
            String strPhone = binding.etPhone.getText().toString().trim();

            if (TextUtils.isEmpty(strEmail)) {
                binding.etEmail.setError("Email is required.");
                return;
            }
            if (!isValid(strEmail)) {
                binding.etEmail.setError("Please enter a valid email.");
                return;
            }
            if (TextUtils.isEmpty(strPassword)) {
                binding.etPassword.setError("Password is required.");
                return;
            }
            if (strPassword.length() <= 6) {
                binding.etPassword.setError("Password length must be more than 6.");
                return;
            }
            binding.progressBar.setVisibility(View.VISIBLE);

            //Registering user into Firebase
            fAuth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "signInWithEmail:success");
                    Toast.makeText(SignUpActivity.this, "User created", Toast.LENGTH_SHORT).show();

                    FirebaseUser user = fAuth.getCurrentUser();
                    String UserID = user.getUid();
                    dBRef = database.getReference("Users");
                    Users setUser = new Users(strName, strEmail, strPhone);
                    dBRef.child(UserID).setValue(setUser);
                    Intent i = new Intent(SignUpActivity.this, UploadActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Log.w(TAG, task.getException());
                    Toast.makeText(SignUpActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }
            });
        });
    }

    public static boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public void ForgotPass(View v){
        i = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(i);
        finish();
    }

    public void Login(View view) {
        i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        finish();
    }
}