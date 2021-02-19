package amg.team.runningpp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import amg.team.runningpp.R;

public class ActivityAdmin extends AppCompatActivity {
    FrameLayout frameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        frameLayout = findViewById(R.id.fragmentLayout);

        loadFragment(new FragmentListKorisnik(),null);

    }


    protected void loadFragment(Fragment fragment, Bundle bundle){
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment.setArguments(bundle);
            ft.replace(R.id.fragmentLayout, fragment).addToBackStack("tag").commit();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
