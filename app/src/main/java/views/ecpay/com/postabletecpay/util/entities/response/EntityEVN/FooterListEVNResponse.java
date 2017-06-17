package views.ecpay.com.postabletecpay.util.entities.response.EntityEVN;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FooterListEVNResponse {

    @SerializedName("account-idt")
    @Expose
    private String accountIdt;
    @SerializedName("response-code")
    @Expose
    private String responseCode;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("source-address")
    @Expose
    private String sourceAddress;

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

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }
}
