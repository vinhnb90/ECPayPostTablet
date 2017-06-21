package views.ecpay.com.postabletecpay.util.entities.request.Base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duydatpham on 6/17/17.
 */

public class FooterRequest implements Serializable
{

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
