package views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tima on 6/19/17.
 */

public class TransactionOffItem {
    @SerializedName("customer-code")
    @Expose
    private String customer_code;
    @SerializedName("provide-code")
    @Expose
    private String provide_code;
    @SerializedName("amount")
    @Expose
    private Long amount;
    @SerializedName("bill-id")
    @Expose
    private Long bill_id;
    @SerializedName("edong")
    @Expose
    private String edong;
    @SerializedName("audit-number")
    @Expose
    private String audit_number;

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getProvide_code() {
        return provide_code;
    }

    public void setProvide_code(String provide_code) {
        this.provide_code = provide_code;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBill_id() {
        return bill_id;
    }

    public void setBill_id(Long bill_id) {
        this.bill_id = bill_id;
    }

    public String getEdong() {
        return edong;
    }

    public void setEdong(String edong) {
        this.edong = edong;
    }

    public String getAudit_number() {
        return audit_number;
    }

    public void setAudit_number(String audit_number) {
        this.audit_number = audit_number;
    }
}
