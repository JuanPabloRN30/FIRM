package com.example.juanpablorn30.clases;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.juanpablorn30.firm.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by juanpablorn30 on 5/11/17.
 */

public class ListAlarmAdapter extends ArrayAdapter< Alarma > {

    private List<Alarma> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtHora;
        TextView txtColor;
    }

    public ListAlarmAdapter(List<Alarma> data, Context context) {
        super(context, R.layout.list_alarm, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Alarma alarma = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_alarm, parent, false);
            viewHolder.txtColor = (TextView) convertView.findViewById(R.id.txtColor);
            viewHolder.txtHora = convertView.findViewById(R.id.txtHora);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        List<Integer> aux = alarma.getColor();
        viewHolder.txtColor.setBackgroundColor(Color.rgb(aux.get(0),aux.get(1),aux.get(2)));
        Date date = new Date(alarma.getHora());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        viewHolder.txtHora.setText(sdf.format(date));
        return convertView;
    }
}
