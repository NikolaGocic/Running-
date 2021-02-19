package amg.team.runningpp.view;

import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import amg.team.runningpp.R;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListAktivnost;
import amg.team.runningpp.data.ListKorisnik;
import amg.team.runningpp.entities.Aktivnost;
import amg.team.runningpp.services.AktivnostsServices;
import amg.team.runningpp.services.GetImageService;
import amg.team.runningpp.services.KorisniksServices;
import amg.team.runningpp.services.LoginKorisnikService;

public class ActivityStart extends AppCompatActivity {

    SharedPreferences preferences;
    ProgressDialog dialog;
    boolean isVisible =false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        preferences  = this.getSharedPreferences(Constants.SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);

        dialog =new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);

        isVisible = true;

        startServices();
        checkSharedPreferences();
        loadFragment(new FragmentStart(),null,1);
    }

    //Proverava da li je korisnik vec prijavljen
    private void checkSharedPreferences(){
        if(preferences.getBoolean(Constants.SHARED_PREFERENCES_LOGGED,false)) {
            if (preferences.getString(Constants.SHARED_PREFERENCES_USERNAME, "guest").equals("guest")) {
                Intent i = new Intent(this, ActivityApp.class);
                i.putExtra(Constants.INTENT_PASSING_USERNAME, preferences.getString(Constants.SHARED_PREFERENCES_USERNAME, "guest"));
                i.putExtra(Constants.INTENT_PASSING_PASSWORD, preferences.getString(Constants.SHARED_PREFERENCES_PASSWORD, ""));
                this.startActivity(i);
                super.onBackPressed();
            } else if(preferences.getString(Constants.SHARED_PREFERENCES_USERNAME, "guest").equals("admin")) {
                //ne radi nista !
            }
            else {
                LoginKorisnikService loginKorisnikServices = new LoginKorisnikService(this, "1");
                loginKorisnikServices.execute(preferences.getString(Constants.SHARED_PREFERENCES_USERNAME, "guest"),
                        preferences.getString(Constants.SHARED_PREFERENCES_PASSWORD, ""));
                dialogShow();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final FragmentLogin fragment = (FragmentLogin) getSupportFragmentManager().findFragmentById(R.id.fragmentLayout);
        if (fragment != null) {
            dialogHide();
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Menja FrameLayout na prosledjeni fragment
    protected void loadFragment(Fragment fragment,Bundle bundle,int tag){
        try {
            if(tag==0) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                fragment.setArguments(bundle);
                ft.replace(R.id.fragmentLayout, fragment).addToBackStack("tag").commit();
                dialogHide();
            }
            else if(tag == 1){
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                fragment.setArguments(bundle);
                ft.replace(R.id.fragmentLayout, fragment).addToBackStack("tag").commit();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    //Pokrece sve servise i mapira bazu
    private void startServices(){
        try {
            ListAktivnost.getInstance().clearList();
            ListKorisnik.getInstance().clearList();
            dialogShow();
            KorisniksServices korisniksServices = new KorisniksServices();
            korisniksServices.execute();
            AktivnostsServices aktivnostsServices = new AktivnostsServices(this);
            aktivnostsServices.execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dialogShow(){
        try {
            if (!dialog.isShowing() && isVisible)
                dialog.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dialogHide(){
        try {
            if (dialog.isShowing() && isVisible)
                dialog.hide();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void superBackPressed(){
        super.onBackPressed();
    }

    public void successLogIn(){
        dialogHide();
        Intent i = new Intent(this,ActivityApp.class);
        i.putExtra(Constants.INTENT_PASSING_USERNAME,preferences.getString(Constants.SHARED_PREFERENCES_USERNAME,"guest"));
        this.startActivity(i);
        super.onBackPressed();
    }

    public void failedLogin(){
        dialogHide();
    }



}
