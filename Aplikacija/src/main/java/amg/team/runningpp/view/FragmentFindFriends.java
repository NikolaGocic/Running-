package amg.team.runningpp.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import amg.team.runningpp.R;
import amg.team.runningpp.controller.AdapterListItemAktivnost;
import amg.team.runningpp.controller.AdapterListItemKorisnik;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListItemKorisnik;
import amg.team.runningpp.data.ListKorisnik;
import amg.team.runningpp.entities.Korisnik;

public class FragmentFindFriends extends Fragment {
    Button btnBack;
    SearchView searchView;
    ListView listView;
    List<Korisnik> listKorisnik = ListKorisnik.getInstance().getListKorisnik();
    List<Korisnik> filteredList;
    SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_friends,container,false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        btnBack = view.findViewById(R.id.btnBack);
        listView = view.findViewById(R.id.listView);
        searchView = view.findViewById(R.id.searchView);
        filteredList=new ArrayList<>();
        preferences  = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityApp)getActivity()).changeFragment(new FragmentProfil(),3);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.equals("")) {
                    doFilter(newText);
                    try {
                        List<ListItemKorisnik> list = new ArrayList<ListItemKorisnik>();
                        for (int i = 0; i < filteredList.size(); i++) {
                            if(!filteredList.get(i).getUsername().equals(preferences.getString(Constants.SHARED_PREFERENCES_USERNAME,"guest"))) {
                                ListItemKorisnik listItemKorisnik = new ListItemKorisnik();
                                listItemKorisnik.setIme(filteredList.get(i).getIme());
                                listItemKorisnik.setPrezime(filteredList.get(i).getPrezime());
                                listItemKorisnik.setUsername(filteredList.get(i).getUsername());
                                list.add(listItemKorisnik);
                            }
                        }
                        AdapterListItemKorisnik adapterListItemKorisnik = new AdapterListItemKorisnik(getContext(), R.layout.list_view_item_korisnik, list);
                        listView.setAdapter(adapterListItemKorisnik);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    List<ListItemKorisnik> list = new ArrayList<ListItemKorisnik>();
                    AdapterListItemKorisnik adapterListItemKorisnik = new AdapterListItemKorisnik(getContext(), R.layout.list_view_item_korisnik, list);
                    listView.setAdapter(adapterListItemKorisnik);
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItemKorisnik k = (ListItemKorisnik) parent.getItemAtPosition(position);
                openProfile(k);
            }
        });

        return view;
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

    private void doFilter(String s){
        List<Korisnik> pom = new ArrayList<>();
        filteredList.clear();
        filteredList = listKorisnik;
        if(!s.equals("")) {
            for (int i = 0; i < filteredList.size(); i++) {
                if (filteredList.get(i).getIme().toLowerCase().contains(s) ||
                        filteredList.get(i).getPrezime().toLowerCase().contains(s)) {
                    pom.add(filteredList.get(i));
                }
            }
            filteredList = pom;
        }

    }

}
