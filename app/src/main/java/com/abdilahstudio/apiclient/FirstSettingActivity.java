package com.abdilahstudio.apiclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.prefs.Preferences;

public class FirstSettingActivity extends AppCompatActivity {

    private Button button;
    private EditText url;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_setting);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi komponen UI setelah setContentView
        button = findViewById(R.id.btnSimpan);
        url = findViewById(R.id.endPointApi);

        // Inisialisasi SharedPreferences
        preferences = getSharedPreferences("ApiClient", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("endPointApi", url.getText().toString());
                editor.apply(); // Simpan perubahan secara asinkron
                // atau editor.commit(); //untuk penyimpanan sinkron

                Intent intent = new Intent(FirstSettingActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}