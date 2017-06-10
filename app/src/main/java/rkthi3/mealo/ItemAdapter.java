package rkthi3.mealo;

/**
 * Created by roshan on 5/7/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import rkthi3.mealo.models.MenuItem;


/**
 * Created by roshan on 04/05/17.
 * Custom ArrayAdapter to display Item in list view in the search page
 */

public class ItemAdapter extends ArrayAdapter<MenuItem> {
    boolean[] animationStates;
    public ItemAdapter(Context context, ArrayList<MenuItem> menuItem) {
        super(context, 0, menuItem);
        animationStates = new boolean[menuItem.size()];
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
        TextView tvPrc = (TextView) convertView.findViewById(R.id.tvPrice);
        RoundedImageView itemImage = (RoundedImageView) convertView.findViewById(R.id.item_image);
        // Populate the data into the template view using the monster object
        tvName.setText(menuItem.getName());
        tvPrc.setText("Price: $"+String.valueOf(menuItem.getPrice()));

        //Animate the loading of items in list
        Animation animation = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_in);
        animation.setStartOffset(position * 150);
        convertView.startAnimation(animation);

        //itemImage.image

        
        // Return the completed view to render on screen
        return convertView;
    }
}