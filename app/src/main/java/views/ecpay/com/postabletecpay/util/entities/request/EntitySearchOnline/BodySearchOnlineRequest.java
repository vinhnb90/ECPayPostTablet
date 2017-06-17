package views.ecpay.com.postabletecpay.util.entities.request.EntitySearchOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodySearchOnlineRequest implements Serializable {
    @SerializedName("audit-number")
    @Expose
    private Long auditNumber;
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
    private final static long serialVersionUID = -171392060791603258L;

    public Long getAuditNumber() {
        return auditNumber;
    }

    public void setAuditNumber(Long auditNumber) {
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

}
