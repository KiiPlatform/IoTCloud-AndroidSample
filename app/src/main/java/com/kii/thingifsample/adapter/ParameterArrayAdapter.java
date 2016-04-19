package com.kii.thingifsample.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kii.thingifsample.R;

public class ParameterArrayAdapter extends ArrayAdapter<Pair<String, Object>> {
    private final LayoutInflater inflater;

    public ParameterArrayAdapter(Context context) {
        super(context, R.layout.simple_list_item);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleViewHolder holder = null;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.simple_list_item, parent, false);
            holder = new SimpleViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.row_text);
            convertView.setTag(holder);
        } else {
            holder = (SimpleViewHolder) convertView.getTag();
        }
        Pair<String, Object> item = getItem(position);
        holder.text.setText(item.first + " = " + item.second.toString());
        return convertView;
    }
}
