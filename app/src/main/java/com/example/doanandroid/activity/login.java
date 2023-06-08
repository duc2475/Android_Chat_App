package com.example.doanandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanandroid.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

import ObjectClass.Constants;
import ObjectClass.PreferenceManager;


public class login extends AppCompatActivity {
    private TextView goRegister ;
    private MaterialButton login;
    private EditText email, pass;
    private ProgressBar load;
    private PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGN_IN)){
            Intent intent = new Intent(getApplicationContext(), FirstInAp.class);
            startActivity(intent);
            finish();
        }
        goRegister = findViewById(R.id.txt_regis);
        email = findViewById(R.id.txt_inputEmail);
        pass = findViewById(R.id.txt_inputPass);
        load = findViewById(R.id.progressBar);
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this,register.class);
                startActivity(intent);
            }
        });
        login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidSignInDetails()){
                    signIn();
                }
            }
        });
    }
    private void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USER)
                .whereEqualTo(Constants.KEY_EMAIL, email.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, pass.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0){
                       DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                       preferenceManager.putBoolean(Constants.KEY_IS_SIGN_IN, true);
                       preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                       preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                       preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                       preferenceManager.putString(Constants.KEY_SEX, documentSnapshot.getString(Constants.KEY_SEX));
                       Intent intent = new Intent(getApplicationContext(), FirstInAp.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       startActivity(intent);
                   }
                   else {
                       loading(false);
                       showToast("Sai tài khoản mật khẩu!");
                   }
                });
    }

    private void loading(Boolean isLoading){
        if(isLoading){
            login.setVisibility(View.INVISIBLE);
            load.setVisibility(View.VISIBLE);
        }
        else {
            login.setVisibility(View.VISIBLE);
            load.setVisibility(View.INVISIBLE);
        }
    }

     private Boolean isValidSignInDetails(){
        if(email.getText().toString().trim().isEmpty()){
            showToast("Nhập email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            showToast("Email chưa hợp lệ");
            return false;
        }
        else if(pass.getText().toString().trim().isEmpty()){
            showToast("Mật khẩu không hợp lệ");
            return false;
        }
        else{
            return true;
        }
     }

}