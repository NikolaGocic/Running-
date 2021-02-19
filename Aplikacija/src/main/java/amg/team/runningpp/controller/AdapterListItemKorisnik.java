package amg.team.runningpp.controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import amg.team.runningpp.R;
import amg.team.runningpp.data.Constants;
import amg.team.runningpp.data.ListItemKorisnik;

public class AdapterListItemKorisnik extends ArrayAdapter<ListItemKorisnik> {
    Context context;

    public AdapterListItemKorisnik (Context context, int resourceId, List<ListItemKorisnik> lista) {
        super(context, resourceId, lista);
        this.context = context;
    }

    private class ViewHolder {
        TextView txtTitle;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterListItemKorisnik.ViewHolder holder = null;
        ListItemKorisnik korisnik = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_item_korisnik, null);
            holder = new AdapterListItemKorisnik.ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else
            holder = (AdapterListItemKorisnik.ViewHolder) convertView.getTag();

        holder.txtTitle.setText(korisnik.getIme()+" "+korisnik.getPrezime());

        return convertView;
    }
}
