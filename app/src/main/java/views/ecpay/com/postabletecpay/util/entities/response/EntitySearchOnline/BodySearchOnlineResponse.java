package views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodySearchOnlineResponse implements Serializable
{
    @SerializedName("audit-number")
    @Expose
    private Integer auditNumber;
    @SerializedName("mac")
    @Expose
    private String mac;
    @SerializedName("disk-drive")
    @Expose
    private String diskDrive;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("direct-evn")
    @Expose
    private String directEvn;
    @SerializedName("customer-code")
    @Expose
    private String customerCode;
    @SerializedName("pc-code")
    @Expose
    private String pcCode;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("customer")
    @Expose
    private String customer;
    private final static long serialVersionUID = -2620834768200595838L;

    public Integer getAuditNumber() {
        return auditNumber;
    }

    public void setAuditNumber(Integer auditNumber) {
        this.auditNumber = auditNumber;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDiskDrive() {
        return diskDrive;
    }

    public void setDiskDrive(String diskDrive) {
        this.diskDrive = diskDrive;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDirectEvn() {
        return directEvn;
    }

    public void setDirectEvn(String directEvn) {
        this.directEvn = directEvn;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getPcCode() {
        return pcCode;
    }

    public void setPcCode(String pcCode) {
        this.pcCode = pcCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

}