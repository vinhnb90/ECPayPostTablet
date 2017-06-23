package views.ecpay.com.postabletecpay.util.entities.request.EntityMapCustomerCard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;

/**
 * Created by duydatpham on 6/23/17.
 */

public class BodyMapCustomerCardRequest extends BodyRequest {
    @SerializedName("ecard")
    @Expose
    private String ecard;
    @SerializedName("customer-code")
    @Expose
    private String customerCode;
    @SerializedName("status")
    @Expose
    private Long status;
    @SerializedName("customer-phone-ecpay")
    @Expose
    private String customerPhoneEcpay;
    @SerializedName("bank-account-number")
    @Expose
    private String bankAccountNumber;
    @SerializedName("id-number")
    @Expose
    private String idNumber;
    @SerializedName("bank-name")
    @Expose
    private String bankName;

    public String getEcard() {
        return ecard;
    }

    public void setEcard(String ecard) {
        this.ecard = ecard;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getCustomerPhoneEcpay() {
        return customerPhoneEcpay;
    }

    public void setCustomerPhoneEcpay(String customerPhoneEcpay) {
        this.customerPhoneEcpay = customerPhoneEcpay;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
