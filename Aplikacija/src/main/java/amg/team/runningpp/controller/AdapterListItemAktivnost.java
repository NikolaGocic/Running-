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

public class AdapterListItemAktivnost extends ArrayAdapter<ListItemAktivnost> {

    Context context;

    public AdapterListItemAktivnost(Context context, int resourceId, List<ListItemAktivnost> lista) {
        super(context, resourceId, lista);
        this.context = context;
    }

    private class ViewHolder
    {
        ImageView icon;
        TextView imePrezime;
        TextView date;
        ImageView image;
        TextView text;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterListItemAktivnost.ViewHolder holder = null;
        ListItemAktivnost aktivnost = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_item, null);
            holder = new ViewHolder();
            holder.imePrezime = (TextView) convertView.findViewById(R.id.txtImePrezime);
            holder.image = (ImageView) convertView.findViewById(R.id.imageViewAktivnost);
            holder.text = (TextView) convertView.findViewById(R.id.txtAktivnost);
            holder.icon = (ImageView)convertView.findViewById(R.id.imagelogo);
            holder.date = (TextView) convertView.findViewById(R.id.txtDatum);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.imePrezime.setText(aktivnost.getImePrezime());
        holder.image.setImageBitmap(aktivnost.getBitmap());
        holder.text.setText(aktivnost.getTekst());
        holder.date.setText(aktivnost.getDatum());
        holder.icon.setImageResource(aktivnost.getIcon());

        return convertView;
    }
}
