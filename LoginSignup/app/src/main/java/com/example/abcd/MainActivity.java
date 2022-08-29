package com.example.abcd;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText firstN = findViewById(R.id.fname);
        EditText lastN = findViewById(R.id.lname);
        EditText email = findViewById(R.id.email);
        EditText pass = findViewById(R.id.pswrd);
        EditText cpass = findViewById(R.id.cpswrd);
        Button registers = findViewById(R.id.register);
        ProgressBar loading = findViewById(R.id.progressBar);
        FirebaseAuth fauth = FirebaseAuth.getInstance();
        loading.setVisibility(View.INVISIBLE);

        getSupportActionBar().hide();
        registers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                String fn = firstN.getText().toString().trim();
                String ln = lastN.getText().toString().trim();
                String em = email.getText().toString().trim();
                String p = pass.getText().toString().trim();
                String cp = cpass.getText().toString().trim();
                if(!p.equals(cp)){
                    cpass.setError("Password doesn't match");
                    return;
                }
                if(TextUtils.isEmpty(em)){
                    email.setError("Please enter the email");
                    return;
                }
                if(TextUtils.isEmpty(fn)){
                    firstN.setError("Please enter your first name");
                    return;
                }
                if(TextUtils.isEmpty(ln)){
                    lastN.setError("Please enter your last name");
                    return;
                }
                if(TextUtils.isEmpty(p)){
                    pass.setError("Please enter the password");
                    return;
                }
                if(p.length()<6){
                    pass.setError("Password is pretty short. Keep it at least 6 characters long");
                    return;
                }

                // registering process
                fauth.createUserWithEmailAndPassword(em,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            Toast.makeText(MainActivity.this, "You have been registered successfully", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(),login.class));
                            Intent i = new Intent(MainActivity.this,login.class);
                            startActivity(i);
                            finish();

                        }
                        else{
                            Toast.makeText(MainActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });




            }
        });

    }

}