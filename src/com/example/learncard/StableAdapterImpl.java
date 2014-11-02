package com.example.learncard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.example.learncard.modifiedlistview.StableArrayAdapter;

import java.util.List;

/**
 * Created by a.demaynyk (demaynik@ukr.net) on 11/1/2014.
 */
public class StableAdapterImpl extends StableArrayAdapter<String> {
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public StableAdapterImpl(Context context, ListView listView, List<String> mList) {
        super(listView, mList);
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            if (getItemViewType(position) == 1) {
                view = mLayoutInflater.inflate(R.layout.list_item, parent, false);
            } else {
                view = mLayoutInflater.inflate(R.layout.list_item2, parent, false);
            }
        }
        ((TextView) view.findViewById(R.id.list_text)).setText(getItem(position));
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.valueOf(getItemId(position) + "") % 2;
    }
}
