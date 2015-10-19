package com.kii.thingifsample.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kii.thingif.command.Action;
import com.kii.thingif.command.ActionResult;
import com.kii.thingifsample.R;
import com.kii.thingifsample.smart_light_demo.SetBrightness;
import com.kii.thingifsample.smart_light_demo.SetColor;
import com.kii.thingifsample.smart_light_demo.SetColorTemperature;
import com.kii.thingifsample.smart_light_demo.TurnPower;
import com.kii.thingifsample.utils.Utils;

import static com.kii.thingifsample.R.layout.action_list_item;

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
            holder.textActionName = (TextView) convertView.findViewById(R.id.row_action_name);
            holder.textAction = (TextView) convertView.findViewById(R.id.row_action);
            holder.textResult = (TextView) convertView.findViewById(R.id.row_result);
            convertView.setTag(holder);
        } else {
            holder = (ActionViewHolder) convertView.getTag();
        }
        Pair<Action, ActionResult> item = this.getItem(position);
        holder.textActionName.setText(item.first.getActionName());
        if (item.second == null) {
            holder.textResult.setText("unfinished");
        } else {
            if (item.second.succeeded) {
                holder.textResult.setText("succeeded");
            } else {
                holder.textResult.setText("failed");
            }
        }
        if (item.first instanceof TurnPower) {
            holder.textAction.setText(((TurnPower)item.first).power ? "ON" : "OFF");
        } else if (item.first instanceof SetBrightness) {
            holder.textAction.setText(String.valueOf(((SetBrightness)item.first).brightness));
        } else if (item.first instanceof SetColor) {
            holder.textAction.setText(Utils.toColorString(((SetColor)item.first).color));
        } else if (item.first instanceof SetColorTemperature) {
            holder.textAction.setText(String.valueOf(((SetColorTemperature)item.first).colorTemperature));
        }
        return convertView;
    }
}
