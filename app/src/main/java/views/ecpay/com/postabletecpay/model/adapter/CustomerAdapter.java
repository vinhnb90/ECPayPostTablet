package views.ecpay.com.postabletecpay.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;

/**
 * Created by duydatpham on 6/22/17.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    List<Customer> Customers;


    public CustomerAdapter(List<Customer> lst) {
        this.Customers = lst;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_recycler, parent, false);
        ButterKnife.bind(this, view);

        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return Customers.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder
    {
        public View ItemView;

        public CustomerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.ItemView = itemView;
        }
    }
}
