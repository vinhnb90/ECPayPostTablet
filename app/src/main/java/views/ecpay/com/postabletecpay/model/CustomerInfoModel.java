package views.ecpay.com.postabletecpay.model;

import android.content.Context;

import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;

/**
 * Created by MyPC on 23/06/2017.
 */

public class CustomerInfoModel extends CommonModel {
    public CustomerInfoModel(Context context) {
        super(context);
    }

    public  Long UpdateCustomer(Customer customer)
    {
        return  sqLiteConnection.updateCustomer(customer);
    }
}
