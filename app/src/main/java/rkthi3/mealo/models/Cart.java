package rkthi3.mealo.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by roshan on 5/7/17.
 */

public class Cart implements Parcelable{

    private ArrayList<MenuItem> menuItemList;
    private MenuItem item;


    public Cart(){
        item = new MenuItem();
        menuItemList = new ArrayList<>();
    }

    protected Cart(Parcel in){
        //_id=in.readLong();
        //item
    }

    public void addItem(MenuItem item, int qty){
        for (int i=0;i<qty; i++)
            menuItemList.add(item);
    }

    public void removeItem(MenuItem item){
        menuItemList.remove(item);
    }


    public int getItemCount(MenuItem item){
        return Collections.frequency(this.menuItemList, item);
    }

    public int getTotal(){
        int total = 0;
        for (MenuItem item : this.menuItemList){
            total += item.getPrice();
        }
        return total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<MenuItem> CREATOR = new Parcelable.Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel parcel) {
            return new MenuItem(parcel);
        }

        @Override
        public MenuItem[] newArray(int i) {
            return new MenuItem[i];
        }
    };
}
