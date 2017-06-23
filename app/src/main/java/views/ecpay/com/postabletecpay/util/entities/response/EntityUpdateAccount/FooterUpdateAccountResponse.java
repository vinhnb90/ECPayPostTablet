package views.ecpay.com.postabletecpay.util.entities.response.EntityUpdateAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tima on 6/19/17.
 */

public class FooterUpdateAccountResponse {
    @SerializedName("account-idt")
    @Expose
    private String accountIdt;
    @SerializedName("response-code")
    @Expose
    private String responseCode;
    @SerializedName("description")
    @Expose
    private String description;

    public String getAccountIdt() {
        return accountIdt;
    }

    public void setAccountIdt(String accountIdt) {
        this.accountIdt = accountIdt;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
