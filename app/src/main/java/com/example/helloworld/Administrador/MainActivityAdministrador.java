package com.example.helloworld.Administrador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.helloworld.Cliente.MainActivityCliente;
import com.example.helloworld.R;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivityAdministrador extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    FirebaseAuth firebaseAuth;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_administrador);

        Toolbar toolbar = findViewById(R.id.toolbarA);
        drawerLayout = findViewById(R.id.drawer_menu);
        NavigationView navigationView = findViewById(R.id.nav_viewA);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toogle= new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.abrirMenu, R.string.cerrarMenu);

        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA,
                    new InicioAdmin()).commit();

            navigationView.setCheckedItem(R.id.InicioAdmin);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int opcion = item.getItemId();

        if (opcion == R.id.InicioAdmin) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA, new InicioAdmin()).commit();
        } else if (opcion == R.id.RegistroAdmin) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerA, new RegistroAdmin()).commit();
        } else if (opcion == R.id.Salir) {
            Toast.makeText(this, "Cerrando sesión del Administrador", Toast.LENGTH_LONG).show();
            userLogout();
        } else {
            Toast.makeText(this, "No se seleccionó opción", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkUserLogin() {
        if(user != null) {
            Toast.makeText(this, "Se ha iniciado sesión", Toast.LENGTH_LONG). show();
        } else {
            startActivity(new Intent(MainActivityAdministrador.this, MainActivityCliente.class));
            finish();
        }
    }

    private void userLogout() {
        firebaseAuth.signOut() ;
        startActivity(new Intent(MainActivityAdministrador.this, MainActivityCliente.class));
        finish();
    }

    @Override
    protected void onStart() {
        checkUserLogin();
        super.onStart();
    }
}