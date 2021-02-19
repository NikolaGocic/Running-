package amg.team.runningpp.view;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.Executor;

import amg.team.runningpp.R;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListKorisnik;
import amg.team.runningpp.entities.Korisnik;
import amg.team.runningpp.services.CheckEmailService;
import amg.team.runningpp.services.LoginKorisnikService;

public class FragmentLogin extends Fragment {
    Button btnLogin;
    EditText etUsername;
    EditText etPassword;
    Fragment fragment = this;

    SharedPreferences preferences;

    CallbackManager callbackManager;
    LoginButton loginButton;
    AccessToken accessToken;


    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    ProgressDialog dialog;

    private static final int RC_SIGN_IN = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);

        btnLogin = (Button)view.findViewById(R.id.btnLogin);
        etUsername = (EditText)view.findViewById(R.id.etUsername);
        etPassword = (EditText)view.findViewById(R.id.etPassword);
        signInButton = (SignInButton)view.findViewById(R.id.googleSignin);
        loginButton = (LoginButton) view.findViewById(R.id.fbLogin);
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));


        callbackManager = CallbackManager.Factory.create();

        try{
            dialog =new ProgressDialog(getContext());
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.setInverseBackgroundForced(false);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        preferences  = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(getContext());
        if(acc!= null)
            signOut();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoginClick(v);
            }
        });

        accessToken= AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn) {
            try {
                LoginManager.getInstance().logOut();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);//callback za FB
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
//        dialogShow();
    }
    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken == null){
            }
            else
                loadUserProfile(currentAccessToken);
        }
    };

    private void loadUserProfile(AccessToken accessToken){
        try {
            GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        String first_name = object.getString("first_name");
                        String last_name = object.getString("last_name");
                        String email = object.getString("email");
                        String id = object.getString("id");
                        dialogShow();
                        Korisnik korisnik = new Korisnik();
                        korisnik.setEmail(email);
                        korisnik.setUsername(id);
                        korisnik.setPassword(ListKorisnik.getInstance().getHashedPassword(id));
                        korisnik.setIme(first_name);
                        korisnik.setPrezime(last_name);
                        CheckEmailService checkEmailService = new CheckEmailService(fragment, korisnik);
                        checkEmailService.execute(email);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "first_name,last_name,email,id");
            graphRequest.setParameters(parameters);
            graphRequest.executeAsync();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void btnLoginClick(View v){
        LoginKorisnikService loginKorisnikServices = new LoginKorisnikService(this,"2");
        loginKorisnikServices.execute(etUsername.getText().toString(),ListKorisnik.getInstance().getHashedPassword(etPassword.getText().toString()));
        dialogShow();
    }

    public void successLogIn(){
        dialogHide();
        if(etUsername.getText().toString().equals("admin")){
            Intent intent = new Intent(getActivity(),ActivityAdmin.class);
            getActivity().startActivity(intent);
        }
        else {
            Intent i = new Intent(getActivity(), ActivityApp.class);
            i.putExtra(Constants.INTENT_PASSING_USERNAME, etUsername.getText().toString());
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean(Constants.SHARED_PREFERENCES_LOGGED, true);
            edit.putString(Constants.SHARED_PREFERENCES_USERNAME, etUsername.getText().toString());
            edit.putString(Constants.SHARED_PREFERENCES_PASSWORD, ListKorisnik.getInstance().getHashedPassword(etPassword.getText().toString()));
            edit.commit();
            getContext().startActivity(i);
        }
    }

    public void failedLogin(){
        dialogHide();
        Toast.makeText(getActivity(), "Wrong data, please try again !", Toast.LENGTH_SHORT).show();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            dialogShow();
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Korisnik korisnik = new Korisnik();
            korisnik.setEmail(account.getEmail());
            korisnik.setUsername(account.getId());
            korisnik.setPassword(ListKorisnik.getInstance().getHashedPassword(account.getId()));
            korisnik.setIme(account.getGivenName());
            korisnik.setPrezime(account.getFamilyName());
            CheckEmailService checkEmailService = new CheckEmailService(this,korisnik);
            checkEmailService.execute(account.getEmail());
        } catch (ApiException e) {
            dialogHide();
        }
    }

    public void emailCheckSuccess(Korisnik korisnik){
        dialogHide();
        Intent i = new Intent(getActivity(), ActivityApp.class);
        i.putExtra(Constants.INTENT_PASSING_USERNAME, korisnik.getUsername());
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(Constants.SHARED_PREFERENCES_LOGGED, true);
        edit.putString(Constants.SHARED_PREFERENCES_USERNAME, korisnik.getUsername());
        edit.putString(Constants.SHARED_PREFERENCES_PASSWORD, korisnik.getPassword());
        edit.commit();
        getContext().startActivity(i);
        ((ActivityStart)getContext()).superBackPressed();
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
        try{
        if(dialog.isShowing())
            dialog.hide();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void emailCheckFailed(Korisnik korisnik){
        dialogHide();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_PASSING_USERNAME,korisnik.getUsername());
        bundle.putString(Constants.BUNDLE_PASSING_PASSWORD,korisnik.getPassword());
        bundle.putString(Constants.BUNDLE_PASSING_EMAIL,korisnik.getEmail());
        bundle.putString(Constants.BUNDLE_PASSING_IME,korisnik.getIme());
        bundle.putString(Constants.BUNDLE_PASSING_PREZIME,korisnik.getPrezime());
        ((ActivityStart)getActivity()).loadFragment(new FragmentRegister(),bundle,0);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        try {
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
