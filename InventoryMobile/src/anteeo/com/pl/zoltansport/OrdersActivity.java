package anteeo.com.pl.zoltansport;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class OrdersActivity extends ActionBarActivity {

    ListView ordersListView;
    public ArrayList<Order> orders;
    OrdersListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ((TextView)findViewById(R.id.loggedAsTextView)).setText("Zalogowano jako: "+LoginActivity.username);

        findViewById(R.id.newOrderButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newOrder = new Intent(OrdersActivity.this, AdoptionReleaseStockActivity.class);
                newOrder.putExtra("ACTION",1);
                newOrder.putExtra("NEW",true);
                OrdersActivity.this.startActivity(newOrder);
            }
        });

        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrdersActivity.this.finish();
            }
        });

        ordersListView = (ListView) findViewById(R.id.ordersListView);
        ordersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO sprawdzić status zamowienia i odpowiednio dobrać aktywnosci
                //TODO albo AdoptionReleaseStockActivity albo ReadyOrderDetailsActivity

                //Intent release = new Intent(OrdersActivity.this, AdoptionReleaseStockActivity.class);
                //release.putExtra("ACTION",AdoptionReleaseStockActivity.ACTION_RELEASE);
                //release.putExtra("NEW",false);
                //OrdersActivity.this.startActivity(release)

                Intent details = new Intent(OrdersActivity.this, ReadyOrderDetailsActivity.class);
                OrdersActivity.this.startActivity(details);
            }
        });
        reloadList();
        orders.add(new Order("NOWY","321/ASD"));
    }

    private void reloadList(){
        orders = new ArrayList<Order>();
        adapter = new OrdersListAdapter(this, orders);
        ordersListView.setAdapter(adapter);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_orders, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
