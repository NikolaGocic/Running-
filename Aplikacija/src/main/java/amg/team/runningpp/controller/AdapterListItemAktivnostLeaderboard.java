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

public class AdapterListItemAktivnostLeaderboard extends ArrayAdapter<ListItemAktivnost> {

    Context context;

    public AdapterListItemAktivnostLeaderboard(Context context, int resourceId, List<ListItemAktivnost> lista) {
        super(context, resourceId, lista);
        this.context = context;
    }

    private class ViewHolder
    {
        TextView imePrezime;
        TextView text;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterListItemAktivnostLeaderboard.ViewHolder holder = null;
        ListItemAktivnost aktivnost = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_view_item_aktivnost_leaderboard, null);
            holder = new AdapterListItemAktivnostLeaderboard.ViewHolder();
            holder.imePrezime = (TextView) convertView.findViewById(R.id.textImePrezime);
            holder.text = (TextView) convertView.findViewById(R.id.textText);
            convertView.setTag(holder);
        } else
            holder = (AdapterListItemAktivnostLeaderboard.ViewHolder) convertView.getTag();

        holder.imePrezime.setText(aktivnost.getImePrezime());
        holder.text.setText(aktivnost.getTekst());

        return convertView;
    }
}
