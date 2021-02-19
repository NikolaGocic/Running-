package amg.team.runningpp.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import amg.team.runningpp.R;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListAktivnost;
import amg.team.runningpp.data.ListKorisnik;
import amg.team.runningpp.entities.Aktivnost;
import amg.team.runningpp.services.AddAktivnostService;
import amg.team.runningpp.services.AddImageService;

public class FragmentSaveActivity extends Fragment {

    ImageView imageView;
    Switch switchVidljivost;
    TextView textView;
    Button btnSave;
    Button btnCancle;
    Bitmap slika;
    EditText editTextNaziv;
    String distanca = "";
    String maxBrzina = "";
    String prosecnaBrzina = "";
    String pocetakVreme = "";
    String zavrsetakVreme = "";
    SharedPreferences preferences;
    boolean endServices = false;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save_activity,container,false);

        imageView = view.findViewById(R.id.imageView);
        switchVidljivost = view.findViewById(R.id.switchVidljivost);
        textView = view.findViewById(R.id.textView);
        btnCancle = view.findViewById(R.id.btnCancle);
        btnSave = view.findViewById(R.id.btnSave);
        editTextNaziv = view.findViewById(R.id.editTextNaziv);

        dialog =new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);

        preferences  = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);

        try {
            Bundle bundle = ((ActivityApp)getActivity()).getBundle(1);
            slika = BitmapFactory.
                    decodeByteArray(bundle.getByteArray(Constants.BUNDLE_PASSING_SLIKA),
                            0,bundle.getByteArray(Constants.BUNDLE_PASSING_SLIKA).length);

            distanca = bundle.getString(Constants.BUNDLE_PASSING_DISTANCA);
            maxBrzina = bundle.getString(Constants.BUNDLE_PASSING_MAX_BRZINA);
            prosecnaBrzina = bundle.getString(Constants.BUNDLE_PASSING_PROSECNA_BRZINA);
            pocetakVreme = bundle.getString(Constants.BUNDLE_PASSING_POCETNO_VREME);
            zavrsetakVreme = bundle.getString(Constants.BUNDLE_PASSING_ZAVRSNO_VREME);
            setData();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCancleClikc();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSaveClick();
            }
        });
        return  view;
    }

    private void btnCancleClikc(){
        ((ActivityApp)getActivity()).changeCurrentFragment(new FragmentActivity(),1,1);
    }

    private void btnSaveClick(){
        AddImageService addImageService = new AddImageService(this,slika);
        addImageService.execute(pocetakVreme);
        String setVidljivost = "1";
        if(switchVidljivost.isChecked())
            setVidljivost = "0";
        AddAktivnostService addAktivnostService = new AddAktivnostService(this);
        addAktivnostService.execute(pocetakVreme,zavrsetakVreme,editTextNaziv.getText().toString(),distanca,
                prosecnaBrzina,maxBrzina,setVidljivost,pocetakVreme+".jpg",
                String.valueOf(ListKorisnik.getInstance().getKorisnikWithUsername
                        (preferences.getString(Constants.SHARED_PREFERENCES_USERNAME,"guest")).getId()));

        dialogShow();
        Aktivnost a = new Aktivnost();
        a.setSlika(slika);
        a.setNaziv(editTextNaziv.getText().toString());
        a.setIdKorisnika(ListKorisnik.getInstance().getKorisnikWithUsername
                (preferences.getString(Constants.SHARED_PREFERENCES_USERNAME,"guest")).getId());
        a.setKilometraza(Float.parseFloat(distanca));
        a.setMaxBrzina(Float.parseFloat(maxBrzina));
        a.setProsecnaBrzina(Float.parseFloat(prosecnaBrzina));
        a.setUrlSlika(pocetakVreme+".jpg");
        a.setVidljivost(Integer.parseInt(setVidljivost));
        a.setVremePocetka(pocetakVreme);
        a.setVremeZavrsetka(zavrsetakVreme);
        ListAktivnost.getInstance().addAktivnost(a);
    }

    public void uploadActivitySuccess(){
        if(!endServices)
            endServices = true;
        else {
            dialogHide();
            ((ActivityApp)getActivity()).changeCurrentFragment(new FragmentActivity(),1,1);
        }
    }

    public void uploadActivityFailed(){
        if(!endServices)
            endServices = true;
        else {
            dialogHide();
            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void uploadImageSuccess(){
        if(!endServices)
            endServices = true;
        else {
            dialogHide();
            ((ActivityApp)getActivity()).changeCurrentFragment(new FragmentActivity(),1,1);
        }
    }

    public void uploadImageFailed(){
        if(!endServices)
            endServices = true;
        else {
            dialogHide();
            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(){
        imageView.setImageBitmap(slika);
        textView.setText("Distace : "+String.valueOf(distanca)+"\nAverage Speed : "+String.valueOf(prosecnaBrzina)+
                "\nMaximum Speed : "+String.valueOf(maxBrzina)+"\nStart Time : "+pocetakVreme+
                "\nEnd Time : "+zavrsetakVreme);
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
