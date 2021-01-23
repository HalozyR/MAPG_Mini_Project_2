package ece.np.edu.miniproject2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ece.np.edu.miniproject2.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity {

    ActivityForgotPasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}