package com.browser.proxy.browserproxy.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.browser.proxy.browserproxy.R;

/**
 * @author alessandro.balocco
 */
public class SimpleAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private boolean isGrid;

    public SimpleAdapter(Context context, boolean isGrid) {
        layoutInflater = LayoutInflater.from(context);
        this.isGrid = isGrid;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            if (isGrid) {
                view = layoutInflater.inflate(R.layout.simple_grid_item, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.simple_list_item, parent, false);
            }

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
        switch (position) {
            case 0:
                viewHolder.textView.setText(context.getString(R.string.gototop));
                viewHolder.imageView.setImageResource(R.drawable.ic_gotop);
                break;
            case 1:
                viewHolder.textView.setText(context.getString(R.string.addhome));
                viewHolder.imageView.setImageResource(R.drawable.ic_addhome);
                break;
            case 2:
                viewHolder.textView.setText(context.getString(R.string.findinpage));
                viewHolder.imageView.setImageResource(R.drawable.ic_find);
                break;
            case 3:
                viewHolder.textView.setText(context.getString(R.string.ss));
                viewHolder.imageView.setImageResource(R.drawable.ic_screenshot);
                break;
            case 4:
                viewHolder.textView.setText(context.getString(R.string.share));
                viewHolder.imageView.setImageResource(R.drawable.ic_share);
                break;
            case 5:
                viewHolder.textView.setText(context.getString(R.string.exit));
                viewHolder.imageView.setImageResource(R.drawable.ic_exit);
                break;
            default:
                viewHolder.textView.setText(context.getString(R.string.gototop));
                viewHolder.imageView.setImageResource(R.drawable.ic_gotop);
                break;
        }

        return view;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
