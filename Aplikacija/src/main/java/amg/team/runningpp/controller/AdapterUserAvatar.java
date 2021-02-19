package amg.team.runningpp.controller;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import amg.team.runningpp.R;
import amg.team.runningpp.data.UserAvatar;

public class AdapterUserAvatar extends ArrayAdapter<UserAvatar> {
    Context context;


    public AdapterUserAvatar(Context context, int resourceId, List<UserAvatar> lista) {
        super(context, resourceId, lista);
        this.context = context;

    }

    @Override
    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
        UserAvatar userAvatar = getItem(position);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.avatar,null);
        ImageView imageView=row.findViewById(R.id.userAvatar);

        imageView.setImageResource(userAvatar.getUserImageId());


        return row;
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        UserAvatar userAvatar = getItem(position);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.avatar,null);
        ImageView imageView=row.findViewById(R.id.userAvatar);

        imageView.setImageResource(userAvatar.getUserImageId());


        return row;
    }
}