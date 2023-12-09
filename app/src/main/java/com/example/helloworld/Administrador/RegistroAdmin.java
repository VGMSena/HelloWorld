package com.example.helloworld.Administrador;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.helloworld.R;
//import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;

/* *
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistroAdmin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistroAdmin extends Fragment {

    TextView FechaRegistro;
    EditText Correo, Password, Nombres, Apellidos, FechaNacimiento;
    Button RegistrarAdministrador;
//    FirebaseAuth auth;
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

        // Inflate the layout for this fragment
        return view;
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