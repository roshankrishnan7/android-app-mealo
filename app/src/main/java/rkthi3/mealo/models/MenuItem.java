package rkthi3.mealo.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roshan on 5/7/17.
 */

public class MenuItem {

        /*public static final String TABLE_NAME = "monster_table";
        //public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_ATTACK_POWER = "attack_power";
        public static final String COLUMN_HEALTH = "health";
        public static final String COLUMN_SPECIES = "species";
        // Table create statement
        //private long _id;
*/
        private String name;
        private int price;


        public MenuItem(){
            //_id= 0;
            name = "";
            price = 0;

        }

        protected MenuItem(Parcel in){
            //_id=in.readLong();
            name=in.readString();
            price=in.readInt();
        }

        public MenuItem(String name, int price){
            //this._id = id;
            this.name = name;
            this.price = price;
        }


        //public void set_id(long _id) { this._id = _id; }

        public void setName(String name){
            this.name = name;
        }

        public void setPrice(int price){
            this.price = price;
        }

//        public long get_id() { return _id; }

        public String getName(){
            return name;
        }

        public int getPrice(){
            return price;
        }


        public String getMenuItemDetails() {
            return ("Item name : " + name + "\nItem price : " + price );
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
//            parcel.writeLong(_id);
            parcel.writeString(name);
            parcel.writeInt(price);

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
