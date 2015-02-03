package anteeo.com.pl.zoltansport;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by user on 28.12.14.
 */
public class OrdersListAdapter extends ArrayAdapter<Order> {

    private ArrayList<Order> orders;
    private Context context;


    public OrdersListAdapter(Context context, ArrayList<Order> orders) {
        super(context, R.layout.orders_list_item_layout, orders);
        this.orders = orders;
        this.context = context;
    }

    public class OrderHolder{
        ImageView statusImage;
        TextView orderNumber;
    }

    @Override
    public Order getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderHolder holder = null;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.orders_list_item_layout, null);
            holder = new OrderHolder();
            holder.orderNumber = (TextView) convertView.findViewById(R.id.orderNumber);
            holder.statusImage = (ImageView) convertView.findViewById(R.id.statusImage);
            convertView.setTag(holder);
        }else{
            holder = (OrderHolder) convertView.getTag();
        }

        Order order = orders.get(position);
        holder.orderNumber.setText(order.number);
//        holder.statusImage.setImageResource(R.drawable.india);
//        return super.getView(position, convertView, parent);
        return convertView;
    }

}
