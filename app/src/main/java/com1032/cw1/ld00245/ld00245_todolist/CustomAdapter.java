package com1032.cw1.ld00245.ld00245_todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PunLife on 26/02/2016.
 */
public class CustomAdapter extends ArrayAdapter<String> {
    public CustomAdapter(Context context, List<String> values) {
        super(context, R.layout.row_layout2, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.row_layout2, parent, false);

        String text = getItem(position);
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(text);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);

        return view;
    }
}
