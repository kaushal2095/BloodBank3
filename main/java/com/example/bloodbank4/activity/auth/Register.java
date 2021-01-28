package com.example.bloodbank4.activity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbank4.R;
import com.example.bloodbank4.activity.ChooseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private TextInputLayout nameInput;
    private TextInputLayout phoneInput;
    private TextInputLayout emailInput;
    private TextInputLayout passInput;
    private TextInputLayout cpassInput;
    private TextView move_login;
    private Button btn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initComponent();

        applyListener();

    }

    private void applyListener(){
        move_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
            }
        });
        
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccount();
            }
        });

    }

    private void registerAccount() {
        final String name = nameInput.getEditText().getText().toString();
        final String phone=phoneInput.getEditText().getText().toString();
        String mail = emailInput.getEditText().getText().toString();
        String password = passInput.getEditText().getText().toString();
        String cpassword = cpassInput.getEditText().getText().toString();

        if (!name.isEmpty() && !phone.isEmpty() &&!mail.isEmpty() && !password.isEmpty() && !cpassword.isEmpty()) {

            if (!isValidMobil(phone)) {
                Toast.makeText(this, "Please Enter A Valid Phone Number", Toast.LENGTH_SHORT).show();
            }
            else if (!isValidMail(mail)) {
                Toast.makeText(this, "Please Enter A Valid E-Mail Address", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(this, "Password Should Be Atleast 6 Characters Long", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(cpassword)) {
                Toast.makeText(this, "Passsword Doesn't Match", Toast.LENGTH_SHORT).show();
            }else{
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(mail,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                  if(task.isSuccessful()){
                                      insertInDatabase(name,phone);
                                      Intent intent=new Intent(Register.this, ChooseActivity.class);
                                      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                      startActivity(intent);
                                      finish();
                                  }else{
                                      Toast.makeText(Register.this, "Signup Failed::Wrong Credentials", Toast.LENGTH_SHORT).show();

                                  }
                                  progressDialog.dismiss();
                            }
                        });
            }
        }
    }

    private void insertInDatabase(String name, String phone) {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String userId=user.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("phone",phone);
        mDatabase.setValue(hashMap);
    }

    private boolean isValidMail(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidMobil(String phone){
        return Patterns.PHONE.matcher(phone).matches();
    }

    private void initComponent() {
        nameInput = findViewById(R.id.sign_name_field);
        phoneInput=findViewById(R.id.sign_phone_field);
        emailInput = findViewById(R.id.sign_email_field);
        passInput = findViewById(R.id.sign_pass_field);
        btn =  findViewById(R.id.registerBtn);
        move_login=findViewById(R.id.move_login);
        cpassInput = findViewById(R.id.sign_cpass_field);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please wait....");
        mAuth=FirebaseAuth.getInstance();
    }
}
