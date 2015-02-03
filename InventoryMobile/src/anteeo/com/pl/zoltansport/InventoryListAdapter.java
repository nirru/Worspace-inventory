package anteeo.com.pl.zoltansport;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by user on 29.12.14.
 */
public class InventoryListAdapter extends ArrayAdapter<Product> implements View.OnClickListener{

    ArrayList<Product> products;
    Context context;

    public InventoryListAdapter(Context context, ArrayList<Product> products) {
        super(context, R.layout.inventory_list_item_layout, products);
        this.products = products;
        this.context = context;
    }

    public class ProductHolder{
        TextView snTextView;
        TextView productNameTextView;
        EditText countEditText;
        ImageButton deleteItemImageButton;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Product getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductHolder holder = null;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.inventory_list_item_layout, null);
            holder = new ProductHolder();
            holder.snTextView = (TextView) convertView.findViewById(R.id.snTextView);
            holder.productNameTextView = (TextView) convertView.findViewById(R.id.productNameTextView);
            holder.countEditText = (EditText) convertView.findViewById(R.id.countEditText);
            holder.deleteItemImageButton = (ImageButton) convertView.findViewById(R.id.deleteItemImageButton);
            holder.deleteItemImageButton.setOnClickListener(this);
            convertView.setTag(holder);
        }else{
            holder = (ProductHolder) convertView.getTag();
        }

        Product product = products.get(position);
        holder.snTextView.setText((position+1)+".");
        holder.productNameTextView.setText(product.name);
        holder.countEditText.setText(product.count+"");
        holder.deleteItemImageButton.setTag(product);
//        return super.getView(position, convertView, parent);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.remove_product_ensure)
                .setTitle(R.string.warning)
                .setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO
                    }
                })
                .setNegativeButton(R.string.cancel_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
