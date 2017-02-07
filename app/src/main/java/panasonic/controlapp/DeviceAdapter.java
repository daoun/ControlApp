package panasonic.controlapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Christine on 2/7/17.
 */
public class DeviceAdapter extends ArrayAdapter<Device> {

    // declaring our ArrayList of items
    private ArrayList<Device> objects;

    /* here we must override the constructor for ArrayAdapter
    * the only variable we care about now is ArrayList<Item> objects,
    * because it is the list of objects we want to display.
    */
    public DeviceAdapter(Context context, int textViewResourceId, ArrayList<Device> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
    }

    /*
     * we are overriding the getView method here - this is what defines how each
     * list item will look.
     */
    public View getView(int position, View convertView, ViewGroup parent){

        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.simple_list_item_1, null);
        }

        Device device = objects.get(position);

        if (device != null) {

            TextView hostname = (TextView) v.findViewById(R.id.hostname);
            TextView ip_address = (TextView) v.findViewById(R.id.ip_address);

            if (hostname != null){
                hostname.setText(device.getHostname());
            }
            if (ip_address != null){
                ip_address.setText(device.getIp());
            }
        }

        // the view must be returned to our activity
        return v;

    }

}