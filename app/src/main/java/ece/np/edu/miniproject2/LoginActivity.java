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

import ece.np.edu.miniproject2.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private FirebaseAuth fAuth;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fAuth = FirebaseAuth.getInstance();


        //Logging user into the app
        binding.btLogin.setOnClickListener(v -> {
            binding.progressBar2.setVisibility(View.VISIBLE);
            String strEmail = binding.etEmail.getText().toString().trim();
            String strPassword = binding.etPassword.getText().toString().trim();

            if(TextUtils.isEmpty(strEmail)){
                binding.etEmail.setError("Email is required.");
                binding.progressBar2.setVisibility(View.GONE);
                return;
            }
            if(TextUtils.isEmpty(strPassword)){
                binding.etPassword.setError("Password is required.");
                binding.progressBar2.setVisibility(View.GONE);
                return;
            }

            fAuth.signInWithEmailAndPassword(strEmail, strPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(getApplicationContext(), "Logged in successfully", Toast.LENGTH_SHORT).show();
                                binding.progressBar2.setVisibility(View.GONE);
                                FirebaseUser user = fAuth.getCurrentUser();
                                i = new Intent(getApplicationContext(), UploadActivity.class);
                                startActivity(i);
                            }
                            else{
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                binding.progressBar2.setVisibility(View.GONE);
                                return;
                            }
                        }
                    });
        });
    }

    public void SignUp(View view) {
        i = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(i);
        finish();
    }
}