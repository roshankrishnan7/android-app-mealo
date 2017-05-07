package rkthi3.mealo;

/**
 * Created by roshan on 5/7/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import rkthi3.mealo.models.MenuItem;


/**
 * Created by roshan on 04/05/17.
 * Custom ArrayAdapter to display Item in list view in the search page
 */

public class ItemAdapter extends ArrayAdapter<MenuItem> {
    public ItemAdapter(Context context, ArrayList<MenuItem> menuItem) {
        super(context, 0, menuItem);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MenuItem menuItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvPrice);
        // Populate the data into the template view using the monster object
        tvName.setText(menuItem.getName());
        tvHome.setText(menuItem.getPrice());
        // Return the completed view to render on screen
        return convertView;
    }
}
