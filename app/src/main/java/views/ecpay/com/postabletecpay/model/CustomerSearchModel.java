package views.ecpay.com.postabletecpay.model;

import android.content.Context;

import java.util.List;

import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;

/**
 * Created by MyPC on 23/06/2017.
 */

public class CustomerSearchModel extends CommonModel {
    public CustomerSearchModel(Context context) {
        super(context);
    }


    public List<Customer> getListCustomer(String maKH, String tenKH, String dcKH, String phoneKH, String gtKH)
    {
        return  sqLiteConnection.selectAllCustomerFitter(maKH, tenKH, dcKH, phoneKH, gtKH);
    }
}
