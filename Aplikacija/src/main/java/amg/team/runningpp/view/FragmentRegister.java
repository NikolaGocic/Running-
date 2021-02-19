package amg.team.runningpp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import amg.team.runningpp.R;
import amg.team.runningpp.controller.AdapterUserAvatar;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListKorisnik;
import amg.team.runningpp.data.UserAvatar;
import amg.team.runningpp.entities.Korisnik;
import amg.team.runningpp.services.AddKorisnikService;

public class FragmentRegister extends Fragment {
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etIme;
    private EditText etPreizme;
    private EditText etDatum;
    private EditText etTezina;
    private EditText etVisina;
    private RadioButton rbM;
    private Button register;
    private Korisnik korisnik;
    private Spinner spinner;
    int avatarInterno = 0;
    private List<UserAvatar> avatarList;
    private static int avatrs[]={R.drawable.s0,R.drawable.s1,R.drawable.s2,R.drawable.s3,R.drawable.s4,R.drawable.s5,R.drawable.s6,R.drawable.s7,R.drawable.s8,R.drawable.s9};

    SharedPreferences preferences;

    public FragmentRegister() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);

        preferences  = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        
        etUsername = (EditText)view.findViewById(R.id.Username);
        etEmail = (EditText)view.findViewById(R.id.Email);
        etPassword = (EditText)view.findViewById(R.id.etPassword);
        etIme = (EditText)view.findViewById(R.id.Ime);
        etPreizme = (EditText)view.findViewById(R.id.Prezime);
        etDatum = (EditText)view.findViewById(R.id.Datum);
        etTezina = (EditText)view.findViewById(R.id.Tezina);
        etVisina = (EditText)view.findViewById(R.id.Visina);
        rbM = (RadioButton)view.findViewById(R.id.radioButtonM);
        register = (Button)view.findViewById(R.id.btnUpdate);
        rbM.setChecked(true);
        spinner=(Spinner)view.findViewById(R.id.spinner);
        avatarList= new ArrayList<>();

        for(int i=0;i<10;i++)
            avatarList.add(new UserAvatar(avatrs[i]));

        AdapterUserAvatar adapterUserAvatar= new AdapterUserAvatar(getActivity(),R.layout.avatar,avatarList);
        spinner.setAdapter(adapterUserAvatar);


        Bundle bundle = getArguments();
        if(bundle!=null){
            korisnik = new Korisnik();
            korisnik.setUsername(bundle.getString(Constants.BUNDLE_PASSING_USERNAME));
            korisnik.setPassword(bundle.getString(Constants.BUNDLE_PASSING_PASSWORD));
            korisnik.setEmail(bundle.getString(Constants.BUNDLE_PASSING_EMAIL));
            korisnik.setIme(bundle.getString(Constants.BUNDLE_PASSING_IME));
            korisnik.setPrezime(bundle.getString(Constants.BUNDLE_PASSING_PREZIME));
            setDatas();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegisterClick(v);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                avatarInterno = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return  view;
    }

    private void setDatas(){
        etUsername.setText(korisnik.getUsername());
        etEmail.setText(korisnik.getEmail());
        etIme.setText(korisnik.getIme());
        etPreizme.setText(korisnik.getPrezime());
    }

    private void btnRegisterClick(View view){
        try {
            if (etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("") ||
                    etEmail.getText().toString().equals("") || etIme.getText().toString().equals("") ||
                    etPreizme.getText().toString().equals("") || etDatum.getText().toString().equals("") ||
                    etTezina.getText().toString().equals("") || etVisina.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Some fields are empty, please fill all fields !", Toast.LENGTH_SHORT).show();
            }
            else {
                Korisnik korisnik = new Korisnik();
                korisnik.setUsername(etUsername.getText().toString());
                korisnik.setPassword(ListKorisnik.getInstance().getHashedPassword(etPassword.getText().toString()));
                korisnik.setEmail(etEmail.getText().toString());
                korisnik.setDatumRodjenja(etDatum.getText().toString());
                korisnik.setIme(etIme.getText().toString());
                korisnik.setPrezime(etPreizme.getText().toString());
                korisnik.setTezina(Integer.parseInt(etTezina.getText().toString()));
                korisnik.setVisina(Integer.parseInt(etVisina.getText().toString()));
                korisnik.setAvatar(avatarInterno);
                if (rbM.isChecked())
                    korisnik.setPol("M");
                else
                    korisnik.setPol("Z");
                ListKorisnik.getInstance().addKorisnik(korisnik);
                AddKorisnikService addKorisnikService = new AddKorisnikService(this);
                addKorisnikService.execute(korisnik.getListParams());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public  void successRegister(){
        Intent i = new Intent(getActivity(), ActivityApp.class);
        i.putExtra(Constants.INTENT_PASSING_USERNAME, etUsername.getText().toString());
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(Constants.SHARED_PREFERENCES_LOGGED, true);
        edit.putString(Constants.SHARED_PREFERENCES_USERNAME, etUsername.getText().toString());
        edit.putString(Constants.SHARED_PREFERENCES_PASSWORD, ListKorisnik.getInstance().getHashedPassword(etPassword.getText().toString()));
        edit.commit();
        getActivity().startActivity(i);
//        getActivity().onBackPressed();
        ((ActivityStart)getActivity()).superBackPressed();
    }

    public  void failedRegister(){
        Toast.makeText(getActivity(), "Wrong data, please try again !", Toast.LENGTH_SHORT).show();
    }
}
