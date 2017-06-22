package views.ecpay.com.postabletecpay.util.entities.request.EntitySearchCustomer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.request.Base.BodyRequest;

/**
 * Created by MyPC on 22/06/2017.
 */

public class BodySearchCustomerRequest extends BodyRequest {
    @SerializedName("customer-code")
    @Expose
    private String customerCode;
    @SerializedName("customer-name")
    @Expose
    private String customerName;
    @SerializedName("customer-phone")
    @Expose
    private String customerPhone;
    @SerializedName("customer-address")
    @Expose
    private String customerAddress;
    @SerializedName("book-cmis")
    @Expose
    private String bookCmis;
    @SerializedName("pc-code")
    @Expose
    private String pcCode;
    @SerializedName("direct-evn")
    @Expose
    private int directEvn;


    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getBookCmis() {
        return bookCmis;
    }

    public void setBookCmis(String bookCmis) {
        this.bookCmis = bookCmis;
    }

    public String getPcCode() {
        return pcCode;
    }

    public void setPcCode(String pcCode) {
        this.pcCode = pcCode;
    }

    public int getDirectEvn() {
        return directEvn;
    }

    public void setDirectEvn(int directEvn) {
        this.directEvn = directEvn;
    }
}
