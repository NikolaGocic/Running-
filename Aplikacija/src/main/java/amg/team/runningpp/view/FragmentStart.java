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
import android.widget.Button;

import amg.team.runningpp.R;
import amg.team.runningpp.data.Constants;

public class FragmentStart extends Fragment {
    Button btnLogIn;
    Button btnRegister;
    Button btnGuest;
    SharedPreferences preferences;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start,container,false);

        preferences  = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);

        btnLogIn = (Button)view.findViewById(R.id.btnLogin);
        btnRegister = (Button)view.findViewById(R.id.btnUpdate);
        btnGuest = (Button)view.findViewById(R.id.btnGuest);



        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGuestClick(v);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegisterClick(v);
            }
        });
        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogInClick(v);
            }
        });

        return  view;
    }

    private void btnLogInClick(View view){
        ((ActivityStart)getActivity()).loadFragment(new FragmentLogin(), null,0);
    }

    private void btnRegisterClick(View view){
        ((ActivityStart)getActivity()).loadFragment(new FragmentRegister(), null,0);
    }

    private void btnGuestClick(View view){
        try {
            Intent i = new Intent(getActivity(), ActivityApp.class);
            i.putExtra(Constants.INTENT_PASSING_USERNAME, "guest");
            //Da bi svaki sledeci put skakao na ActivityApp
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean(Constants.SHARED_PREFERENCES_LOGGED, true);
            edit.putString(Constants.SHARED_PREFERENCES_USERNAME, "guest");
            edit.commit();
            getActivity().startActivity(i);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
