package views.ecpay.com.postabletecpay.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.view.TrangChu.CashTranferFragment;
import views.ecpay.com.postabletecpay.view.TrangChu.ISearchCustomerView;

/**
 * Created by duydatpham on 6/22/17.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    List<Customer> Customers;
    ISearchCustomerView customerView;

    public CustomerAdapter( ISearchCustomerView _customerView) {
        this.customerView = _customerView;
        this.Customers = new ArrayList<>();
    }

    public List<Customer> getCustomers() {
        return Customers;
    }

    public void setCustomers(List<Customer> customers) {
        Customers = customers;
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_recycler, parent, false);

        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {

        final Customer customer = Customers.get(position);
        holder.fill(customer);
        holder.btnUpdateRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerView.showCustomerInfo(customer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Customers.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {
        public View ItemView;


        public TextView TxtName;
        public TextView TxtCode;
        public TextView TxtPhone;
        public TextView TxtBookCmis;
        public TextView TxtAddress;
        public Button btnUpdateRegis;


        public CustomerViewHolder(View itemView) {
            super(itemView);
            this.ItemView = itemView;

            TxtName = (TextView) itemView.findViewById(R.id.txt_row_customer_name);
            TxtCode = (TextView) itemView.findViewById(R.id.tv_row_customer_maKhachHang);
            TxtPhone = (TextView) itemView.findViewById(R.id.tv_row_customer_phone);
            TxtBookCmis = (TextView) itemView.findViewById(R.id.tv_row_customer_bookCmis);
            TxtAddress = (TextView) itemView.findViewById(R.id.tv_row_customer_diaChi);
            btnUpdateRegis = (Button) itemView.findViewById(R.id.btn_row_customer_update);
        }

        public void fill(Customer customer) {
            TxtName.setText(customer.getName());
            TxtCode.setText(customer.getCode() + (customer.getCardNo() != null && customer.getCardNo().length() > 0 ? ("/" + customer.getCardNo()) : ""));
            TxtPhone.setText(customer.getPhoneByevn() != null ? customer.getPhoneByevn() : "");
            TxtBookCmis.setText(customer.getBookCmis() != null ? customer.getBookCmis() : "");
            TxtAddress.setText(customer.getAddress() != null ? customer.getAddress() : "");

            if(customer.getCardNo() == null || customer.getCardNo().length() == 0)
            {
                btnUpdateRegis.setText("Đăng Ký");
            }else
            {
                btnUpdateRegis.setText("Cập Nhật");
            }
        }
    }
}
