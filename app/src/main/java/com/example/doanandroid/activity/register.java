package com.example.doanandroid.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import com.example.doanandroid.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import ObjectClass.Constants;
import ObjectClass.PreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class register extends AppCompatActivity {
    public Spinner spinner;
    private PreferenceManager preferenceManager;
    private TextView Signin;
    private EditText userName, email, pass, rePass;
    private MaterialButton signUpbtn;
    private ImageFilterButton addImage;
    private CircleImageView avatar;
    private ProgressBar load;
    private String avaString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        spinner = (Spinner) findViewById(R.id.sex_selection);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_selection, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        userName = (EditText) findViewById(R.id.input_userName);
        load = findViewById(R.id.progressBar);
        Signin = findViewById(R.id.txt_signIn);
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });
        email = (EditText) findViewById(R.id.txt_inputEmail);
        pass = findViewById(R.id.txt_inputPass);
        rePass = findViewById(R.id.txt_inputRePass);
        addImage = (ImageFilterButton) findViewById(R.id.btn_addImage);
        avatar = (CircleImageView) findViewById(R.id.img_avatar);
        signUpbtn = (MaterialButton) findViewById(R.id.btn_register);
        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidRegisterDetail()){
                    sigup();
                }
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImage.launch(intent);
            }
        });
        preferenceManager = new PreferenceManager(getApplicationContext());
    }

    private void sigup() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, userName.getText().toString());
        user.put(Constants.KEY_EMAIL, email.getText().toString());
        user.put(Constants.KEY_SEX, spinner.getSelectedItem().toString());
        user.put(Constants.KEY_PASSWORD, pass.getText().toString());
        user.put(Constants.KEY_IMAGE, avaString);
        database.collection(Constants.KEY_COLLECTION_USER)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_SIGN_IN,true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, userName.getText().toString());
                    preferenceManager.putString(Constants.KEY_SEX, spinner.getSelectedItem().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, avaString);
                    Intent intent = new Intent(getApplicationContext(), FirstInAp.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
                });
    }

    private String encodeImage(Bitmap bitmap){

        int preViewWidth = 150;
        int preViewHeight = bitmap.getHeight() * preViewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, preViewWidth, preViewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK) {
                    if(result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            avatar.setImageBitmap(bitmap);
                            avaString = encodeImage(bitmap);
                            addImage.setVisibility(View.GONE);
                        }
                        catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void showToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }
    private boolean isValidRegisterDetail(){
        if(avaString == null) {
            showToast("Chọn ảnh nền đại diện");
            return false;
        }
        else if(userName.getText().toString().trim().isEmpty()){
            showToast("Nhập tên người dùng");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher((email.getText().toString())).matches()){
            showToast("Nhập email hợp lệ");
            return false;
        }
        else if(spinner.getSelectedItem().toString().isEmpty()){
            showToast("Chọn giới tính");
            return false;
        }
        else if(pass.getText().toString().trim().isEmpty()){
            showToast("Nhập password");
            return false;
        }
        else if(rePass.getText().toString().trim().isEmpty()){
            showToast("Nhập lại mật khẩu");
            return false;
        }
        else if(!pass.getText().toString().equals(rePass.getText().toString())){
            showToast("Xác nhận mật khẩu không hợp lệ!");
            return false;
        }
        else {
            return true;
        }
    }
    private void loading(boolean isloading){
        if(isloading){
            signUpbtn.setVisibility(View.INVISIBLE);
            load.setVisibility(View.VISIBLE);
        }
        else {
            load.setVisibility(View.INVISIBLE);
            signUpbtn.setVisibility(View.VISIBLE);
        }
    }
}