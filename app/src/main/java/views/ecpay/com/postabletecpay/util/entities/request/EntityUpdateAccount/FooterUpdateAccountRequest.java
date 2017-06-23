package views.ecpay.com.postabletecpay.util.entities.request.EntityUpdateAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tima on 6/19/17.
 */

public class FooterUpdateAccountRequest {
    @SerializedName("account-idt")
    @Expose
    private String account_idt;

    public String getAccount_idt() {
        return account_idt;
    }

    public void setAccount_idt(String account_idt) {
        this.account_idt = account_idt;
    }
}
