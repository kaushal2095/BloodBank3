package com.example.bloodbank4.activity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Login extends AppCompatActivity {

    private TextInputLayout mailInput;
    private TextInputLayout passInput;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponent();
        
        applyListener();
    }

    private void applyListener() {
         loginButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 login_user();
             }
         });

         register.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(new Intent(Login.this,Register.class));
             }
         });
    }

    private void login_user() {
         String email=mailInput.getEditText().getText().toString();
         final String password=passInput.getEditText().getText().toString();
          if(!email.isEmpty()&& !password.isEmpty()){
               if(!isValid(email)){
                   Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
               }else{
                   progressDialog.setMessage("Logging In....");
                   progressDialog.show();
                   mAuth.signInWithEmailAndPassword(email,password)
                   .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {

                           if(task.isSuccessful()){
                               Intent intent=new Intent(Login.this,ChooseActivity.class);
                               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               startActivity(intent);
                               finish();
                           }
                           progressDialog.hide();
                       }
                   });
               }
          }else{
              Toast.makeText(this, "Please fill information carefully", Toast.LENGTH_SHORT).show();
          }
    }

    private void initComponent() {
        mailInput =  findViewById(R.id.login_email_field);
        passInput =  findViewById(R.id.login_pass_field);
        loginButton = findViewById(R.id.login_button);
        register=findViewById(R.id.register);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        mAuth=FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() !=null) {
            Intent intent = new Intent(Login.this, ChooseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }

   private boolean isValid(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
   }

}
