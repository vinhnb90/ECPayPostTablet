package views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by macbook on 6/10/17.
 */

public class FileGenResponse {
    @SerializedName("customers")
    @Expose
    private ArrayList<ListCustomerResponse> customerResponse;
    @SerializedName("bills")
    @Expose
    private ArrayList<ListBillResponse> billResponse;
    @SerializedName("id_changed")
    @Expose
    private String id_changed;
    @SerializedName("date_changed")
    @Expose
    private String date_changed;

    public ArrayList<ListCustomerResponse> getCustomerResponse() {
        return customerResponse;
    }

    public void setCustomerResponse(ArrayList<ListCustomerResponse> customerResponse) {
        this.customerResponse = customerResponse;
    }

    public ArrayList<ListBillResponse> getBillResponse() {
        return billResponse;
    }

    public void setBillResponse(ArrayList<ListBillResponse> billResponse) {
        this.billResponse = billResponse;
    }

    public String getId_changed() {
        return id_changed;
    }

    public void setId_changed(String id_changed) {
        this.id_changed = id_changed;
    }

    public String getDate_changed() {
        return date_changed;
    }

    public void setDate_changed(String date_changed) {
        this.date_changed = date_changed;
    }
}
