package amg.team.runningpp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import amg.team.runningpp.R;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListKorisnik;
import amg.team.runningpp.entities.Korisnik;
import amg.team.runningpp.services.AdminDeleteKorisnikService;
import amg.team.runningpp.services.AdminUpdateKorisnikService;
import amg.team.runningpp.services.UpdateKorisnikService;

public class FragmentKorisnikDeleteUpdate extends Fragment {

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
    private Button btnDelete;
    private Korisnik korisnik;
    SharedPreferences preferences;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_korisnik_update_delete,container,false);

        dialog =new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);

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
        btnDelete = view.findViewById(R.id.btnDelete);

        preferences  = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);

        korisnik = new Korisnik();
        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                korisnik = ListKorisnik.getInstance().getKorisnikWithUsername(bundle.getString(Constants.BUNDLE_PASSING_USERNAME));
                setDatas();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBackClick();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDeleteClick();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUpdateClick();
            }
        });

        return  view;
    }

    public void setDatas(){
        etPassword.setText(korisnik.getPassword());
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
    public void btnDeleteClick()
    {
        AdminDeleteKorisnikService adminDeleteKorisnikService = new AdminDeleteKorisnikService(this);
        adminDeleteKorisnikService.execute(korisnik.getUsername());
        dialogShow();
    }

    public void btnBackClick(){
        ((ActivityAdmin)getActivity()).loadFragment(new FragmentListKorisnik(),null);
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
            AdminUpdateKorisnikService adminUpdateKorisnikService = new AdminUpdateKorisnikService(this);
            adminUpdateKorisnikService.execute(korisnik.getListParams());
            dialogShow();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void successUpdate(){
        dialogHide();
        korisnik.setIme(etIme.getText().toString());
        korisnik.setPrezime(etPreizme.getText().toString());
        korisnik.setEmail(etEmail.getText().toString());
        korisnik.setPassword(etPassword.getText().toString());
        korisnik.setTezina(Integer.parseInt(etTezina.getText().toString()));
        korisnik.setVisina(Integer.parseInt(etVisina.getText().toString()));
        korisnik.setDatumRodjenja(etDatum.getText().toString());
        ListKorisnik.getInstance().setKorisnik(korisnik);
        Toast.makeText(getContext(), "Successful update !", Toast.LENGTH_SHORT).show();
        ((ActivityAdmin)getActivity()).loadFragment(new FragmentListKorisnik(),null);
    }

    public void failedUpdate(){
        dialogHide();
        Toast.makeText(getContext(), "Wrong data, please try again !", Toast.LENGTH_SHORT).show();
    }

    public void removeKorisnikSuccess(){
        dialogHide();
        Toast.makeText(getContext(), "Successfully deleted user !", Toast.LENGTH_SHORT).show();
        ListKorisnik.getInstance().removeKorisnikWithUsername(korisnik.getUsername());
        ((ActivityAdmin)getActivity()).loadFragment(new FragmentListKorisnik(),null);
    }

    public void removeKorisnikFailed(){
        dialogHide();
        Toast.makeText(getContext(), "Server failed, please try again later !", Toast.LENGTH_SHORT).show();
    }

    public void dialogShow(){
        try {
            if (!dialog.isShowing())
                dialog.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dialogHide(){
        try {
            if (dialog.isShowing())
                dialog.hide();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
