package ece.np.edu.miniproject2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ece.np.edu.miniproject2.databinding.ActivityImageBinding;

public class ImageActivity extends AppCompatActivity {

    ActivityImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle("Images");

    }
}