package views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tima on 6/19/17.
 */

public class ListPostBill {
    @SerializedName("customerCode")
    @Expose
    private String customerCode;
    @SerializedName("provideCode")
    @Expose
    private String provideCode;
    @SerializedName("strAmount")
    @Expose
    private String strAmount;
    @SerializedName("strBillId")
    @Expose
    private String strBillId;
    @SerializedName("edong")
    @Expose
    private String edong;
    @SerializedName("responseCode")
    @Expose
    private long responseCode;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("pcCode")
    @Expose
    private String pcCode;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getProvideCode() {
        return provideCode;
    }

    public void setProvideCode(String provideCode) {
        this.provideCode = provideCode;
    }

    public String getStrAmount() {
        return strAmount;
    }

    public void setStrAmount(String strAmount) {
        this.strAmount = strAmount;
    }

    public String getStrBillId() {
        return strBillId;
    }

    public void setStrBillId(String strBillId) {
        this.strBillId = strBillId;
    }

    public String getEdong() {
        return edong;
    }

    public void setEdong(String edong) {
        this.edong = edong;
    }

    public long getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(long responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPcCode() {
        return pcCode;
    }

    public void setPcCode(String pcCode) {
        this.pcCode = pcCode;
    }
}
