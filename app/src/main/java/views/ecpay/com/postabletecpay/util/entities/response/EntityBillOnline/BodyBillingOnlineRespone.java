package views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.Base.BodyRespone;

public class BodyBillingOnlineRespone extends BodyRespone{

    @SerializedName("payment-edong")
    @Expose
    private String paymentEdong;
    @SerializedName("customer-code")
    @Expose
    private String customerCode;
    @SerializedName("session")
    @Expose
    private String session;
    @SerializedName("bill-id")
    @Expose
    private Long billId;
    @SerializedName("amount")
    @Expose
    private Long amount;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("provider-code")
    @Expose
    private String providerCode;
    @SerializedName("partner-code")
    @Expose
    private String partnerCode;
    @SerializedName("billing-type")
    @Expose
    private String billingType;
    @SerializedName("trace-number")
    @Expose
    private Long traceNumber;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public Long getTraceNumber() {
        return traceNumber;
    }

    public void setTraceNumber(Long traceNumber) {
        this.traceNumber = traceNumber;
    }

    public String getPaymentEdong() {
        return paymentEdong;
    }

    public void setPaymentEdong(String paymentEdong) {
        this.paymentEdong = paymentEdong;
    }
}