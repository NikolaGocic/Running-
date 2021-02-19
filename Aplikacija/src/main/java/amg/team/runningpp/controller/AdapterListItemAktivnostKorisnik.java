package amg.team.runningpp.controller;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import amg.team.runningpp.R;
import amg.team.runningpp.data.ListItemAktivnost;

public class AdapterListItemAktivnostKorisnik extends ArrayAdapter<ListItemAktivnost> {
    Context context;

    public AdapterListItemAktivnostKorisnik(Context context, int resourceId, List<ListItemAktivnost> lista) {
        super(context, resourceId, lista);
        this.context = context;
    }

    private class ViewHolder
    {
        ImageView image;
        TextView text;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterListItemAktivnostKorisnik.ViewHolder holder = null;
        ListItemAktivnost aktivnost = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_item_aktivnost_korisnik, null);
            holder = new AdapterListItemAktivnostKorisnik.ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.imageViewAktivnost);
            holder.text = (TextView) convertView.findViewById(R.id.txtAktivnost);
            convertView.setTag(holder);
        } else
            holder = (AdapterListItemAktivnostKorisnik.ViewHolder) convertView.getTag();

        holder.image.setImageBitmap(aktivnost.getBitmap());
        holder.text.setText(aktivnost.getTekst());

        return convertView;
    }

}
