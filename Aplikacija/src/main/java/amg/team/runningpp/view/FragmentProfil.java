package amg.team.runningpp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import amg.team.runningpp.R;
import amg.team.runningpp.controller.AdapterListItemAktivnostKorisnik;
import amg.team.runningpp.controller.AdapterListItemKorisnik;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListItemAktivnost;
import amg.team.runningpp.data.ListItemKorisnik;
import amg.team.runningpp.data.ListKorisnik;
import amg.team.runningpp.entities.Aktivnost;
import amg.team.runningpp.entities.Korisnik;

public class FragmentProfil extends Fragment {
    TextView txtView;
    public String username = "";
    public int ID = 0;
    public Korisnik korisnik = null;
    TextView txtImePrezime;
    TextView brojAktivnosti;
    TextView kilometraza;
    Button btnEdit;
    Button btnFind;
    Button btnList;
    ImageView btnInfo;
    ImageView imageView;
    ListView listView;
    List<Aktivnost> celaLista;
    ImageView btnLogout;
    boolean showingListUsers = false;
    List<Aktivnost> listaAktivnostiZaKorisnika;
    public float ukupnaKilometraza=0;
    public int brojAktibnosti=0;

    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil,container,false);
        try {
            celaLista = new ArrayList<>();
            listaAktivnostiZaKorisnika = new ArrayList<>();

            imageView = view.findViewById(R.id.imageView);

            txtImePrezime = view.findViewById(R.id.txtImePrezime);
            brojAktivnosti = view.findViewById(R.id.txtBrojAktivnosti);
            kilometraza = view.findViewById(R.id.txtTotalnaPretrcanaDistanca);
            btnEdit = view.findViewById(R.id.btnAdd);
            btnFind = view.findViewById(R.id.btnFind);
            listView = view.findViewById(R.id.listView);
            btnLogout = (ImageView)view.findViewById(R.id.btnLogout);
            btnList = view.findViewById(R.id.btnList);
            btnInfo = view.findViewById(R.id.btnInfo);

            preferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);

            if(preferences.getString(Constants.SHARED_PREFERENCES_USERNAME, "guest").equals("guest")) {
                txtImePrezime.setEnabled(false);
                brojAktivnosti.setEnabled(false);
                btnEdit.setEnabled(false);
                btnFind.setEnabled(false);
                kilometraza.setEnabled(false);
                listView.setEnabled(false);
                btnList.setEnabled(false);
            }
            else {
                korisnik = ListKorisnik.getInstance().getKorisnikWithUsername(preferences.getString(Constants.SHARED_PREFERENCES_USERNAME, ""));
                korisnik.findAktivnosts();
                brojAktibnosti = korisnik.getNumberAktivnost();
                ukupnaKilometraza = korisnik.getKilometers();

                postaviElemente();
                loadListAktivnost();
            }

            btnList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnListClick();
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.BUNDLE_PASSING_USERNAME,korisnik.getUsername());
                    ((ActivityApp) getActivity()).setBundle(3, bundle);
                    ((ActivityApp) getActivity()).changeFragment(new FragmentEditProfile(), 3);
                }
            });

            btnFind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ActivityApp)getActivity()).changeFragment(new FragmentFindFriends(),3);
                }
            });

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnLogoutClick();
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(showingListUsers) {
                        ListItemKorisnik k = (ListItemKorisnik) parent.getItemAtPosition(position);
                        openProfile(k);
                    }
                }
            });

            btnInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnInfoClick();
                }
            });

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    public void btnInfoClick(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Running++");
        alertDialogBuilder.setMessage("This is Sport application with one purpose to track your activity and make you satisfied with results !\n\n" +
                "Authors : \nAleksandar Stoiljkovic\nNikola Gocic\nLazar Stoiljkovic\n");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void btnLogoutClick(){
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(Constants.SHARED_PREFERENCES_LOGGED, false);
        edit.putString(Constants.SHARED_PREFERENCES_USERNAME, "");
        edit.commit();
        Intent i = new Intent(getActivity(),ActivityStart.class);
        getActivity().startActivity(i);
    }

    public void btnListClick(){
        if(btnList.getText().toString().equals("List Friends")){
            btnList.setText("List Activities");
            loadListFrineds();
            showingListUsers = true;
        }
        else {
            btnList.setText("List Friends");
            loadListAktivnost();
            showingListUsers = false;
        }
    }

    private void openProfile(ListItemKorisnik korisnik){
        try {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BUNDLE_PASSING_USERNAME,korisnik.getUsername());
            ((ActivityApp) getActivity()).setBundle(3, bundle);
            ((ActivityApp) getActivity()).changeFragment(new FragmentProfileFriend(), 3);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void postaviElemente() throws Exception
    {
        imageView.setImageResource(korisnik.getResourceForImage());
        txtImePrezime.setText(korisnik.getImePrezime());
        brojAktivnosti.setText("Total Number Of Activities: "+brojAktibnosti);
        kilometraza.setText("Total Running Distace: "+ukupnaKilometraza);
    }

    public void loadListAktivnost(){
        try {
            List<ListItemAktivnost> list = new ArrayList<ListItemAktivnost>();
            for (int i = 0; i < korisnik.getAktivnostList().size(); i++) {
                ListItemAktivnost listItemAktivnost = new ListItemAktivnost();
                listItemAktivnost.setTekst(korisnik.getAktivnostList().get(i).getFormatedStringWihCalories(korisnik));
                listItemAktivnost.setBitmap(korisnik.getAktivnostList().get(i).getSlika());
                list.add(listItemAktivnost);
            }
            AdapterListItemAktivnostKorisnik adapterListItemKorisnik =
                    new AdapterListItemAktivnostKorisnik(getContext(), R.layout.list_view_item_aktivnost_korisnik, list);
            listView.setAdapter(adapterListItemKorisnik);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadListFrineds(){
        try {
            List<ListItemKorisnik> list = new ArrayList<ListItemKorisnik>();
            for (int i = 0; i < korisnik.getFriendList().size(); i++) {
                ListItemKorisnik listItemKorisnik = new ListItemKorisnik();
                listItemKorisnik.setIme(ListKorisnik.getInstance().getKorisnikWithID(korisnik.getFriendList().get(i)).getIme());
                listItemKorisnik.setPrezime(ListKorisnik.getInstance().getKorisnikWithID(korisnik.getFriendList().get(i)).getPrezime());
                listItemKorisnik.setUsername(ListKorisnik.getInstance().getKorisnikWithID(korisnik.getFriendList().get(i)).getUsername());
                list.add(listItemKorisnik);
            }
            AdapterListItemKorisnik adapterListItemKorisnik =
                    new AdapterListItemKorisnik(getContext(), R.layout.list_view_item_korisnik, list);
            listView.setAdapter(adapterListItemKorisnik);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}