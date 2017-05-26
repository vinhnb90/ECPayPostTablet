package views.ecpay.com.postabletecpay.model;

import android.content.Context;

import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayModel extends CommonModel {
    public PayModel(Context context) {
        super(context);
    }

    public List<PayAdapter.PayEntityAdapter> getAllBill(String mEdong) {
        if (mEdong == null)
            return null;

        return sqLiteConnection.selectAllBill(mEdong);
    }
}
