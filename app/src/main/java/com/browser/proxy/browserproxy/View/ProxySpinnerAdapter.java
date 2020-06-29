package com.browser.proxy.browserproxy.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.browser.proxy.browserproxy.R;
import com.browser.proxy.browserproxy.utils.ProxyItem;

public class ProxySpinnerAdapter extends ArrayAdapter<ProxyItem> {

    private int id;
    private List<ProxyItem> proxies;
    private LayoutInflater inflater;
    private Context context;

    public ProxySpinnerAdapter(Context context, int id, List<ProxyItem> proxies) {
        super(context, id, proxies);
        this.context = context;
        this.proxies = proxies;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.id = id;
    }

    public void initProxyList(ArrayList<ProxyItem> arrayList) {
        addAll(arrayList);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
        @SuppressLint("ViewHolder")
        View itemView = inflater.inflate(id, viewGroup, false);
//        TextView textView = (TextView) super.getView(i, view, viewGroup);

        ProxyItem proxyItem = (ProxyItem) proxies.get(i);

        TextView textView = itemView.findViewById(R.id.tvCountryName);
        ImageView ivCountryFlag = itemView.findViewById(R.id.ivCountryFlag);
//        textView.setText(proxies.get(i).getName().toUpperCase());

        int reourceId = context.getResources().getIdentifier("flag_" + proxies.get(i).getCountryCode().toLowerCase(), "drawable",
                context.getPackageName());
        ivCountryFlag.setImageDrawable(context.getResources().getDrawable(reourceId));
        ivCountryFlag.setImageResource(reourceId);

        textView.setText(proxyItem.getCountryName());
//        textView.setCompoundDrawables(context.getResources().getDrawable(reourceId), null, null, null);
//        textView.setCompoundDrawablesWithIntrinsicBounds(reourceId, 0, 0, 0);
        textView.setCompoundDrawablePadding(16);
        return itemView;
    }

    //    @Override
//    public View getDropDownView(int i, View view, @NonNull ViewGroup viewGroup) {
//        TextView textView = (TextView) super.getDropDownView(i, view, viewGroup);
//        ProxyItem ProxyItem = (ProxyItem) getItem(i);
//        assert ProxyItem != null;
//        textView.setText(ProxyItem.getCountryName());
//        textView.setCompoundDrawablesWithIntrinsicBounds(ProxyItem.getCountryFlag(), 0, 0, 0);
//        textView.setCompoundDrawablePadding(10);
//        return textView;
//    }
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}