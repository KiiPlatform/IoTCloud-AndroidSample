package com.kii.iotcloudsample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kii.iotcloudsample.R;
import com.kii.iotcloudsample.model.Clause;

import java.util.ArrayList;
import java.util.List;

public class ClauseAdapter extends ArrayAdapter<Clause> {
    private final LayoutInflater inflater;

    public ClauseAdapter(Context context) {
        super(context, R.layout.image_list_item);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageViewHolder holder = null;
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.command_list_item, parent, false);
            holder = new ImageViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.row_icon);
            holder.text = (TextView) convertView.findViewById(R.id.row_text);
            convertView.setTag(holder);
        } else {
            holder = (ImageViewHolder) convertView.getTag();
        }
        Clause item = this.getItem(position);
        holder.text.setText(item.getSummary());
        holder.icon.setImageResource(item.getIcon());
        return convertView;
    }

    public List<Clause> getItems() {
        List<Clause> clauses = new ArrayList<Clause>();
        for (int i = 0; i < this.getCount(); i++) {
            clauses.add(getItem(i));
        }
        return clauses;
    }
}
