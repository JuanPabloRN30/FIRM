package com.example.juanpablorn30.clases;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.juanpablorn30.firm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * Created by juanpablorn30 on 5/11/17.
 */

public class ListColorAdapter extends ArrayAdapter< List<Integer> > {

    private List<List<Integer>> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtColor;
        ImageButton btnCheck;
    }

    public ListColorAdapter(List<List<Integer>> data, Context context) {
        super(context, R.layout.list_color_alarm, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_color_alarm, parent, false);
            viewHolder.txtColor = (TextView) convertView.findViewById(R.id.txtColor);
            viewHolder.btnCheck = convertView.findViewById(R.id.btnCheck);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        List<Integer> aux = getItem(position);
        viewHolder.txtColor.setBackgroundColor(Color.rgb(aux.get(0),aux.get(1),aux.get(2)));
        viewHolder.btnCheck.setVisibility(View.INVISIBLE);
        return convertView;
    }
}
