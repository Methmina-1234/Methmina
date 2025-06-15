package com.s23010167.methmina;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        Button loginButton = findViewById(R.id.button);

        dbHelper = new DBHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    // 🟥 Show error Toast if fields are empty
                    Toast.makeText(MainActivity.this, "❗ Please fill in both Username and Password", Toast.LENGTH_SHORT).show();
                } else {
                    // ✅ Save data to SQLite Database
                    boolean isInserted = dbHelper.insertUser(username, password);

                    if (isInserted) {
                        Toast.makeText(MainActivity.this, "✅ Data Saved Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "❗ Failed to Save Data!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
