package views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/10/17.
 */

public class FooterCustomerResponse {
    @SerializedName("idChanged")
    @Expose
    private int idChanged;
    @SerializedName("dateChanged")
    @Expose
    private String dateChanged;

    public int getIdChanged() {
        return idChanged;
    }

    public void setIdChanged(int idChanged) {
        this.idChanged = idChanged;
    }

    public String getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(String dateChanged) {
        this.dateChanged = dateChanged;
    }
}
