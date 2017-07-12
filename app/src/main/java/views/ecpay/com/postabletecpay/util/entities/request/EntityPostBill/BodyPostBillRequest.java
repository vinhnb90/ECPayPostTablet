package views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;

/**
 * Created by tima on 6/19/17.
 */

public class BodyPostBillRequest extends BodyRequest {

    @SerializedName("list-transaction-off")
    @Expose
    private ArrayList<TransactionOffItem> list_transaction_off;

    public ArrayList<TransactionOffItem> getList_transaction_off() {
        return list_transaction_off;
    }

    public void setList_transaction_off(ArrayList<TransactionOffItem> list_transaction_off) {
        this.list_transaction_off = list_transaction_off;
    }
}
