package views.ecpay.com.postabletecpay.util.entities.request.EntityDeleteBillOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;

public class BodyDeleteBillOnlineRequest extends BodyRequest{

    @SerializedName("amount")
    @Expose
    private Long amount;
    @SerializedName("bill-id")
    @Expose
    private Long billId;
    @SerializedName("customer-code")
    @Expose
    private String customerCode;
    @SerializedName("billing-date")
    @Expose
    private String billingDate;
//    @SerializedName("trace-number")
//    @Expose
//    private Long traceNumber;
    @SerializedName("reason")
    @Expose
    private String reason;
    private final static long serialVersionUID = -7810941760801107607L;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(String billingDate) {
        this.billingDate = billingDate;
    }

//    public Long getTraceNumber() {
//        return traceNumber;
//    }
//
//    public void setTraceNumber(Long traceNumber) {
//        this.traceNumber = traceNumber;
//    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


}