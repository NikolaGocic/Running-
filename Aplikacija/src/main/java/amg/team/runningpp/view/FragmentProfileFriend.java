package amg.team.runningpp.view;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import amg.team.runningpp.R;
import amg.team.runningpp.controller.AdapterListItemAktivnostKorisnik;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListItemAktivnost;
import amg.team.runningpp.data.ListKorisnik;
import amg.team.runningpp.entities.Korisnik;
import amg.team.runningpp.services.AddFriendService;
import amg.team.runningpp.services.RemoveFriendService;

public class FragmentProfileFriend extends Fragment {
    Korisnik korisnik;
    TextView txtImePrezime;
    TextView brojAktivnosti;
    TextView kilometraza;
    Button btnProfil;
    Button btnAdd;
    ListView listView;
    SharedPreferences preferences;
    Korisnik me;
    ImageView imageView;
    public float ukupnaKilometraza=0;
    public int brojAktibnosti=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_friend,container,false);

        txtImePrezime = view.findViewById(R.id.txtImePrezime);
        brojAktivnosti = view.findViewById(R.id.txtBrojAktivnosti);
        kilometraza = view.findViewById(R.id.txtTotalnaPretrcanaDistanca);
        btnProfil = view.findViewById(R.id.btnProfil);
        btnAdd = view.findViewById(R.id.btnAdd);
        listView = view.findViewById(R.id.listView);
        imageView =view.findViewById(R.id.imageView);

        preferences  = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);

        korisnik = new Korisnik();

        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityApp)getActivity()).changeFragment(new FragmentProfil(),3);
            }
        });

        try {
            Bundle bundle = ((ActivityApp)getActivity()).getBundle(3);
            korisnik  = ListKorisnik.getInstance().getKorisnikWithUsername(bundle.getString(Constants.BUNDLE_PASSING_USERNAME));
            korisnik.findAktivnosts();
            brojAktibnosti = korisnik.getNumberAktivnost();
            ukupnaKilometraza = korisnik.getKilometers();
            postaviElemente();
            loadListAktivnost();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddClick();
            }
        });

        return view;
    }

    public void postaviElemente() throws Exception
    {
        imageView.setImageResource(korisnik.getResourceForImage());
        txtImePrezime.setText(korisnik.getImePrezime());
        brojAktivnosti.setText("Total Number Of Activities: "+brojAktibnosti);
        kilometraza.setText("Total Running Distace: "+ukupnaKilometraza);
        me = ListKorisnik.getInstance().getKorisnikWithUsername(preferences.getString(Constants.SHARED_PREFERENCES_USERNAME,"guest"));
        if(me.isFrinedWith(korisnik)){
            btnAdd.setText("Delete");
        }
    }

    public void loadListAktivnost(){
        try {
            List<ListItemAktivnost> list = new ArrayList<ListItemAktivnost>();
            for (int i = 0; i < korisnik.getAktivnostList().size(); i++) {
                ListItemAktivnost listItemAktivnost = new ListItemAktivnost();
                if(me.isFrinedWith(korisnik) || (korisnik.getAktivnostList().get(i).getVidljivost() == 0)) {
                    listItemAktivnost.setTekst(korisnik.getAktivnostList().get(i).getFormatedStringWihCalories(korisnik));
                    listItemAktivnost.setBitmap(korisnik.getAktivnostList().get(i).getSlika());
                    list.add(listItemAktivnost);
                }
            }
            AdapterListItemAktivnostKorisnik adapterListItemKorisnik =
                    new AdapterListItemAktivnostKorisnik(getContext(), R.layout.list_view_item_aktivnost_korisnik, list);
            listView.setAdapter(adapterListItemKorisnik);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void  btnAddClick(){
        if(btnAdd.getText().equals("Add")){
            AddFriendService addFriendService = new AddFriendService(this);
            String send_data[] = new String[2];
            send_data[0] = String.valueOf(me.getId());
            send_data[1] = String.valueOf(korisnik.getId());
            addFriendService.execute(send_data);
        }
        else
        {
            RemoveFriendService removeFriendService = new RemoveFriendService(this);
            String send_data[] = new String[2];
            send_data[0] = String.valueOf(me.getId());
            send_data[1] = String.valueOf(korisnik.getId());
            removeFriendService.execute(send_data);
        }
    }

    public void addKorisnikSuccess(){
        Toast.makeText(getContext(), "New friend is added !", Toast.LENGTH_SHORT).show();
        me.addFriend(korisnik.getId());
        btnAdd.setText("Delete");
        loadListAktivnost();

    }
    public void addKorisnikFailed(){
        Toast.makeText(getContext(), "Server failure, please try again later !", Toast.LENGTH_SHORT).show();
    }
    public void removeKorisnikSuccess(){
        Toast.makeText(getContext(), "Friend is removed !", Toast.LENGTH_SHORT).show();
        me.removeFriend(korisnik.getId());
        btnAdd.setText("Add");
        loadListAktivnost();
    }
    public void removeKorisnikFailed(){
        Toast.makeText(getContext(), "Server failure, please try again later !", Toast.LENGTH_SHORT).show();
    }
}
