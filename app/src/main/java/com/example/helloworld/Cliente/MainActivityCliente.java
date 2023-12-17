package com.example.helloworld.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.helloworld.Cliente.InicioCliente;
import com.example.helloworld.R;
import com.google.android.material.navigation.NavigationView;

public class MainActivityCliente extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cliente);

        Toolbar toolbar = findViewById(R.id.toolbarCliente);
        drawerLayout = findViewById(R.id.drawer_menuCliente);
        NavigationView navigationView = findViewById(R.id.nav_viewCliente);

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemIconTintList(null);

        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.abrirMenu, R.string.cerrarMenu);

        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerCliente,
                    new InicioCliente()).commit();

            navigationView.setCheckedItem(R.id.InicioCliente);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int opcion = item.getItemId();

        if (opcion == R.id.InicioCliente) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerCliente, new InicioCliente()).commit();
        } else if (opcion == R.id.IniciarSesion) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containerCliente, new IniciarSesion()).commit();
        }else if (opcion == R.id.SalirApp) {
            Toast.makeText(this, "Saliendo de la Applicacion... Gracias por visitarnos", Toast.LENGTH_SHORT).show();
            
        } else {
            Toast.makeText(this, "No se seleccionó opción", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}