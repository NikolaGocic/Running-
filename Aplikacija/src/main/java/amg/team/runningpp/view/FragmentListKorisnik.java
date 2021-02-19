package amg.team.runningpp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import amg.team.runningpp.R;
import amg.team.runningpp.controller.AdapterListItemKorisnik;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListItemKorisnik;
import amg.team.runningpp.data.ListKorisnik;

public class FragmentListKorisnik extends Fragment {

    ListView listView;
    Button btnLogOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_list_korisnik,container,false);

        listView = view.findViewById(R.id.listView);
        btnLogOut = view.findViewById(R.id.btnLogOut);


        List<ListItemKorisnik> list = new ArrayList<ListItemKorisnik>();
        AdapterListItemKorisnik adapterListItemKorisnik = new AdapterListItemKorisnik(getContext(), R.layout.list_view_item_korisnik, list);
        listView.setAdapter(adapterListItemKorisnik);

        for (int i = 0; i < ListKorisnik.getInstance().getListKorisnik().size(); i++) {
            ListItemKorisnik listItemKorisnik = new ListItemKorisnik();
            listItemKorisnik.setIme(ListKorisnik.getInstance().getListKorisnik().get(i).getIme());
            listItemKorisnik.setPrezime(ListKorisnik.getInstance().getListKorisnik().get(i).getPrezime());
            listItemKorisnik.setUsername(ListKorisnik.getInstance().getListKorisnik().get(i).getUsername());
            list.add(listItemKorisnik);
        }
        adapterListItemKorisnik = new AdapterListItemKorisnik(getContext(), R.layout.list_view_item_korisnik, list);
        listView.setAdapter(adapterListItemKorisnik);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItemKorisnik k = (ListItemKorisnik) parent.getItemAtPosition(position);
                openProfile(k);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogOutClick();
            }
        });

        return view;
    }

    public void btnLogOutClick(){
        Intent intent = new Intent(getActivity(),ActivityStart.class);
        getActivity().startActivity(intent);
        getActivity().onBackPressed();
    }

    private void openProfile(ListItemKorisnik korisnik){
        try {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BUNDLE_PASSING_USERNAME,korisnik.getUsername());
            ((ActivityAdmin) getActivity()).loadFragment(new FragmentKorisnikDeleteUpdate(), bundle);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
