package be.arno.crud.items;

import java.util.ArrayList;

import be.arno.crud.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class ItemCustomListAdapter extends BaseAdapter {

    private ArrayList<Item> listData;

    private LayoutInflater layoutInflater;

    public ItemCustomListAdapter(Context context, ArrayList<Item> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }
 
    @Override
    public int getCount() {
        return listData.size();
    }
 
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_row_layout, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.listItemRow_txvwName);
            holder.date = (TextView) convertView.findViewById(R.id.listItemRow_txvwDate);
            holder.bool = (TextView) convertView.findViewById(R.id.listItemRow_txvwBool);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        holder.name.setText(listData.get(position).getName());
        holder.date.setText(listData.get(position).getDate());
        holder.bool.setText(listData.get(position).getBool()+"");
 
        return convertView;
    }
 
    static class ViewHolder {
        TextView name;
        TextView date;
        TextView bool;
    }
 
}