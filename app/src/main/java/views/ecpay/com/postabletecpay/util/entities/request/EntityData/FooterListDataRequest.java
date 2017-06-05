package views.ecpay.com.postabletecpay.util.entities.request.EntityData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/1/17.
 */

public class FooterListDataRequest {
    @SerializedName("account-idt")
    @Expose
    private String accountIdt;

    public String getAccountIdt() {
        return accountIdt;
    }

    public void setAccountIdt(String accountIdt) {
        this.accountIdt = accountIdt;
    }
}
