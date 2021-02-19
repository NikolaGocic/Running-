package amg.team.runningpp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import amg.team.runningpp.R;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListKorisnik;
import amg.team.runningpp.entities.Korisnik;
import amg.team.runningpp.services.UpdateKorisnikService;

public class FragmentEditProfile extends Fragment {
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etIme;
    private EditText etPreizme;
    private EditText etDatum;
    private EditText etTezina;
    private EditText etVisina;
    private RadioButton rbM;
    private RadioButton rbZ;
    private Button btnUpdate;
    private Button btnBack;
    private Korisnik korisnik;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile,container,false);

        etEmail = (EditText)view.findViewById(R.id.Email);
        etPassword = (EditText)view.findViewById(R.id.etPassword);
        etIme = (EditText)view.findViewById(R.id.Ime);
        etPreizme = (EditText)view.findViewById(R.id.Prezime);
        etDatum = (EditText)view.findViewById(R.id.Datum);
        etTezina = (EditText)view.findViewById(R.id.Tezina);
        etVisina = (EditText)view.findViewById(R.id.Visina);
        rbM = (RadioButton)view.findViewById(R.id.radioButtonM);
        rbZ = (RadioButton)view.findViewById(R.id.radioButtonZ);
        btnUpdate = (Button)view.findViewById(R.id.btnUpdate);
        btnBack = (Button)view.findViewById(R.id.btnBack);

        preferences  = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);

        korisnik = new Korisnik();
        try {
            Bundle bundle = ((ActivityApp) getActivity()).getBundle(3);
            if (bundle != null) {
                korisnik = ListKorisnik.getInstance().getKorisnikWithUsername(bundle.getString(Constants.BUNDLE_PASSING_USERNAME));
                setDatas();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUpdateClick();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBackClick();
            }
        });



        return  view;
    }

    public void btnBackClick(){
        ((ActivityApp)getActivity()).changeFragment(new FragmentProfil(),3);
    }

    public void btnUpdateClick(){
        try {
            korisnik.setIme(etIme.getText().toString());
            korisnik.setPrezime(etPreizme.getText().toString());
            korisnik.setEmail(etEmail.getText().toString());
            korisnik.setPassword(etPassword.getText().toString());
            korisnik.setTezina(Integer.parseInt(etTezina.getText().toString()));
            korisnik.setVisina(Integer.parseInt(etVisina.getText().toString()));
            korisnik.setDatumRodjenja(etDatum.getText().toString());
            if(rbM.isChecked())
                korisnik.setPol("M");
            else
                korisnik.setPol("Z");
            UpdateKorisnikService updateKorisnikService = new UpdateKorisnikService(this);
            updateKorisnikService.execute(korisnik.getListParams());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void successUpdate(){
        korisnik.setIme(etIme.getText().toString());
        korisnik.setPrezime(etPreizme.getText().toString());
        korisnik.setEmail(etEmail.getText().toString());
        korisnik.setPassword(ListKorisnik.getInstance().getHashedPassword(etPassword.getText().toString()));
        korisnik.setTezina(Integer.parseInt(etTezina.getText().toString()));
        korisnik.setVisina(Integer.parseInt(etVisina.getText().toString()));
        korisnik.setDatumRodjenja(etDatum.getText().toString());
        ListKorisnik.getInstance().setKorisnik(korisnik);
        Toast.makeText(getContext(), "Successful update!", Toast.LENGTH_SHORT).show();
        ((ActivityApp)getActivity()).changeFragment(new FragmentProfil(),3);
    }

    public void failedUpdate(){
        Toast.makeText(getContext(), "Wrong data, please try again !", Toast.LENGTH_SHORT).show();
    }

    public void setDatas(){
        etEmail.setText(korisnik.getEmail());
        etIme.setText(korisnik.getIme());
        etPreizme.setText(korisnik.getPrezime());
        etTezina.setText(String.valueOf(korisnik.getTezina()));
        etVisina.setText(String.valueOf(korisnik.getVisina()));
        etDatum.setText(korisnik.getDatumRodjenja());
        if(korisnik.getPol().equals("M"))
            rbM.setChecked(true);
        else
            rbZ.setChecked(true);
    }
}
