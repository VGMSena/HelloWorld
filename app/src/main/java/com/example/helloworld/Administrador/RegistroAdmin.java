package com.example.helloworld.Administrador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/* *
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistroAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroAdmin extends Fragment {

    TextView FechaRegistro;
    EditText Correo, Password, Nombres, Apellidos, FechaNacimiento;
    Button RegistrarAdministrador;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
/*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistroAdmin() {
        // Required empty public constructor
    }
*/
    /* *
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistroAdmin.
     */
    // TODO: Rename and change types and number of parameters
/*
    public static RegistroAdmin newInstance(String param1, String param2) {

        RegistroAdmin fragment = new RegistroAdmin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registro_admin, container, false);

        FechaRegistro = view.findViewById(R.id.fechaAdmin);

        Correo = view.findViewById(R.id.correoAdmin);
        Password = view.findViewById(R.id.contrasenaAdmin);
        Nombres = view.findViewById(R.id.nombresAdmin);
        Apellidos = view.findViewById(R.id.apellidosAdmin);
        FechaNacimiento = view.findViewById(R.id.nacimientoAdmin);

//        FechaNacimiento.setOnClickListener(this);

        RegistrarAdministrador = view.findViewById(R.id.btnRegistrar);

//        auth = FirebaseAuth.getInstance();

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d'/'MM'/'yyyy");

        String Sfecha = fecha.format(date);
        FechaRegistro.setText(Sfecha);

        RegistrarAdministrador.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String correo = Correo.getText().toString();
                String pass = Password.getText().toString();
                String nombres = Nombres.getText().toString();
                String apellidos = Apellidos.getText().toString();
                String nacimiento = FechaNacimiento.getText().toString();
                String tipoUsuario = "A";

                if (correo.isEmpty() || pass.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || nacimiento.isEmpty()) {
                    Toast.makeText(getActivity(),"Por favor llenar todos los campos", Toast.LENGTH_LONG).show();
                }
                else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                        Correo.setError("Correo inválido");
                        Correo.setFocusable(true);
                    } else if (pass.length() < 6) {
                        Password.setError("La contraseña debe tener mínimo 6 caracteres");
                        Password.setFocusable(true);
                    } else {
                        AlmacenarAdministrador(correo, pass); // no me gusta estos parametros
                    }
                }
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrando, espere por favor");
        progressDialog.setCancelable(false);

        return view;
    }

    private void AlmacenarAdministrador(String email, String clave) {

        progressDialog.show();

        // com.google.android.firebase.aut.FirebaseAuth
        auth.createUserWithWmailAndPassword(email, clave).addOnCompleteListener(new OncompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    FirebaseUser user = auth.getCurrentUser();
                    assert user != null;

                    String UID = user.getUid();
                    String correo = Correo.getText().toString();
                    String pass = Password.getText().toString();
                    String nombres = Nombres.getText().toString();
                    String apellidos = Apellidos.getText().toString();
                    String nacimiento = FechaNacimiento.getText().toString();
                    String tipoUsuario = "A";

                    HashMap UsrAdministrador = new HashMap();
                    UsrAdministrador.put("UID", UID); // Generar un userid de alguna manera que no sea creando el registro en firebase
                    UsrAdministrador.put("CORREO", correo);
                    UsrAdministrador.put("PASSWORD", pass);
                    UsrAdministrador.put("NOMBRES", nombres);
                    UsrAdministrador.put("APELLIDOS", apellidos);
                    UsrAdministrador.put("FECHANACIMIENTO", nacimiento);
                    UsrAdministrador.put("TIPO", tipoUsuario);

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("TIENDA CELULARES");

                    // escribir en la coleccion de administradores y/o de usuarios

                    reference.child(UID).setValue(UsrAdministrador);

                    startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                    Toast.makeText(getActivity(),"Administrador registrado exitosamente", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Error al registrar el administrador", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }



/*    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nacimientoAdmin:
                showDatePickerDialog();
                break;
        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker")
    }*/
}