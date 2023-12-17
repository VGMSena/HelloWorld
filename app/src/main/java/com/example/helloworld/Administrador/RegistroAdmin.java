package com.example.helloworld.Administrador;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helloworld.R;
import com.example.helloworld.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.app.DatePickerDialog;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RegistroAdmin extends Fragment {

    TextView FechaRegistro;
    EditText Correo, Password, Nombres, Apellidos, NombreUsuario, FechaNacimiento;
    Button RegistrarAdministrador;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    Usuario usrAdmin ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registro_admin, container, false);

        FechaRegistro = view.findViewById(R.id.fechaAdmin);

        Correo = view.findViewById(R.id.correoAdmin);
        Password = view.findViewById(R.id.contrasenaAdmin);
        Nombres = view.findViewById(R.id.nombresAdmin);
        Apellidos = view.findViewById(R.id.apellidosAdmin);
        NombreUsuario = view.findViewById(R.id.usuarioAdmin);
        FechaNacimiento = view.findViewById(R.id.nacimientoAdmin);

        RegistrarAdministrador = view.findViewById(R.id.btnRegistrar);

        auth = FirebaseAuth.getInstance();

        Date date = new Date();
        SimpleDateFormat fecha = new SimpleDateFormat("d'/'MM'/'yyyy");

        String Sfecha = fecha.format(date);
        FechaRegistro.setText(Sfecha);

        FechaNacimiento.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //DatePickerFragment newFragment = new DatePickerFragment();
                //newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                showDatePickerDialog(FechaNacimiento);
            }
        });

        RegistrarAdministrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable iconoError  = ContextCompat.getDrawable(getContext(), R.drawable.error);
                iconoError.setBounds(0,0, 32, 32);

                usrAdmin = new Usuario(Correo.getText().toString(), Password.getText().toString(), Nombres.getText().toString(),
                        Apellidos.getText().toString(), NombreUsuario.getText().toString(), FechaNacimiento.getText().toString(), "A");

                if (usrAdmin.getCorreo().isEmpty() || usrAdmin.getPass().isEmpty() || usrAdmin.getNombres().isEmpty() || usrAdmin.getApellidos().isEmpty() ||
                        usrAdmin.getUsrname().isEmpty() || usrAdmin.getNacimiento().isEmpty()) {
                    Toast.makeText(getActivity(), "Por favor llenar todos los campos", Toast.LENGTH_LONG).show();
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(usrAdmin.getCorreo()).matches()) {
                        Correo.setError("Correo inválido", iconoError);
                        Correo.setFocusable(true);
                    } else if (usrAdmin.getPass().length() < 6) {
                        Password.setError("La contraseña debe tener mínimo 6 caracteres", iconoError);
                        Password.setFocusable(true);
                    } else if (BuscarUsuario(usrAdmin.getUsrname())) {
                        Toast.makeText(getActivity(), "El usuario " + usrAdmin.getUsrname() + " ya se encuentra registrado", Toast.LENGTH_LONG).show();
                    } else {
                        AlmacenarUsuario(usrAdmin);
                    }
                }
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Registrando, espere por favor");
        progressDialog.setCancelable(false);

        return view;
    }

    private void AlmacenarUsuario(Usuario usrAdmin) {

        progressDialog.show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("USUARIOS");

/*        reference.child(usrAdmin.getUsrname()).setValue(usrAdmin).addOnCompleteListener(new OnCompleteListener() {

            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();

                    startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                    Toast.makeText(getActivity(), "Administrador registrado exitosamente", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Error al registrar el administrador", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
*/

        auth.createUserWithEmailAndPassword(usrAdmin.getCorreo(), usrAdmin.getPass()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    FirebaseUser user = auth.getCurrentUser();
                    assert user != null;

                    String UID = user.getUid();

                    HashMap UsrAdministrador = new HashMap();
                    UsrAdministrador.put("UID", UID); // Generar un userid de alguna manera que no sea creando el registro en firebase
                    UsrAdministrador.put("CORREO", usrAdmin.getCorreo());
                    UsrAdministrador.put("PASSWORD", usrAdmin.getPass());
                    UsrAdministrador.put("NOMBRES", usrAdmin.getNombres());
                    UsrAdministrador.put("APELLIDOS", usrAdmin.getApellidos());
                    UsrAdministrador.put("FECHANACIMIENTO", usrAdmin.getNacimiento());
                    UsrAdministrador.put("USRNAME", usrAdmin.getUsrname());
                    UsrAdministrador.put("TIPO", usrAdmin.getTipoUsuario());

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("USUARIOS");

                    // escribir en la coleccion de administradores y/o de usuarios

                    reference.child(UID).setValue(UsrAdministrador);

                    startActivity(new Intent(getActivity(), MainActivityAdministrador.class));
                    Toast.makeText(getActivity(), "Administrador registrado exitosamente", Toast.LENGTH_LONG).show();
                    getActivity().finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Error al registrar el administrador", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        progressDialog.dismiss();
    };

    public static class DatePickerFragment extends DialogFragment {

        private DatePickerDialog.OnDateSetListener listener;

        public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
            DatePickerFragment fragment = new DatePickerFragment();
            fragment.setListener(listener);
            return fragment;
        }

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listener, year, month, day);

            c.set(year - 100, month, day);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            c.set(year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());

            return datePickerDialog;
        }
    }

    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                editText.setText(selectedDate);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public boolean BuscarUsuario(String username) {

        Boolean encontro = false ;

/*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("USUARIOS");
        DataSnapshot datos;

        Boolean encontro = false ;

        if(reference.child("usrname").get().isSuccessful()) {
            Toast.makeText(getActivity(), "Encontro " + username, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "No encuenttra ese nombre de usuario " + username, Toast.LENGTH_LONG).show();
        }
*/
        return encontro;
    }
}