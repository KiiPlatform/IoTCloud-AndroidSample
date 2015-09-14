package com.kii.iotcloudsample.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kii.iotcloud.command.Action;
import com.kii.iotcloud.command.ActionResult;
import com.kii.iotcloudsample.R;

import static com.kii.iotcloudsample.R.layout.action_list_item;

public class ActionArrayAdapter extends ArrayAdapter<Pair<Action, ActionResult>> {
    private final LayoutInflater inflater;

    public ActionArrayAdapter(Context context) {
        super(context, action_list_item);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ActionViewHolder holder = null;
        if (convertView == null) {
            convertView = this.inflater.inflate(action_list_item, parent, false);
            holder = new ActionViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.row_icon);
            holder.textCommandID = (TextView) convertView.findViewById(R.id.row_id);
            holder.textResult = (TextView) convertView.findViewById(R.id.row_result);
            convertView.setTag(holder);
        } else {
            holder = (ActionViewHolder) convertView.getTag();
        }
        Pair<Action, ActionResult> item = this.getItem(position);
        holder.textCommandID.setText(item.first.getActionName());
        if (item.second == null) {
            holder.textResult.setText("unfinished");
        } else {
            if (item.second.succeeded) {
                holder.textResult.setText("succeeded");
            } else {
                holder.textResult.setText("failed");
            }
        }
        return convertView;
    }
}
