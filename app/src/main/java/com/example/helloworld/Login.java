package com.example.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.helloworld.Administrador.MainActivityAdministrador;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.AccessController;

public class Login extends AppCompatActivity {

    EditText Correo, Password;

    Button Acceder;

    FirebaseAuth firebaseAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

/*        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Inicio de Sesión");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
*/
        Correo = findViewById(R.id.correoLogin);
        Password = findViewById(R.id.passwordLogin);
        Acceder = findViewById(R.id.btnIngresar);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Ingresando como administrador, espere por favor");
        progressDialog.setCancelable(false);

        Acceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = Correo.getText().toString();
                String password = Password.getText().toString();

                if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                    Correo.setError("Correo inválido");
                    Correo.setFocusable(true);
                } else if (password.length() < 6) {
                    Password.setError("La contraseña debe tener mínimo 6 caracteres");
                    Password.setFocusable(true);
                } else {
                    LoginAdministrador(correo, password);
                }
            }
        });
    }

    private void LoginAdministrador(String correo, String password) {
        progressDialog.show();
        progressDialog.setCancelable(false);
        firebaseAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    startActivity(new Intent(Login.this, MainActivityAdministrador.class));
                    assert user != null;
                    Toast.makeText(Login.this,"Bienvenid@ "+user.getEmail(),Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    InvalidUser();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                InvalidUser();
            }
        });
    }

    private void InvalidUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setCancelable(false);
        builder.setTitle("!Ha ocurrido un error!");
        builder.setMessage("Verifique sus credenciales de inicio de sesión").setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    //@Override
    public boolean OnSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}