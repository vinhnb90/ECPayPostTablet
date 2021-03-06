package views.ecpay.com.postabletecpay.util.entities.response.EntityDataZip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/1/17.
 */

public class FooterListDataZipResponse {
    @SerializedName("account-idt")
    @Expose
    private String account_idt;
    @SerializedName("response-code")
    @Expose
    private String response_code;
    @SerializedName("description")
    @Expose
    private String description;

    public String getAccount_idt() {
        return account_idt;
    }

    public void setAccount_idt(String account_idt) {
        this.account_idt = account_idt;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
