package views.ecpay.com.postabletecpay.util.entities.request.Base;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duydatpham on 6/17/17.
 */

public class BodyRequest implements Serializable
{

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

}
