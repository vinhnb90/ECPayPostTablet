package views.ecpay.com.postabletecpay.util.entities.response.EntityDataZip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/1/17.
 */

public class BodyListDataZipResponse {
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
    @SerializedName("file-data")
    @Expose
    private String file_data;
    @SerializedName("last-modify")
    @Expose
    private String last_modify;

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

    public String getFile_data() {
        return file_data;
    }

    public void setFile_data(String file_data) {
        this.file_data = file_data;
    }

    public String getLast_modify() {
        return last_modify;
    }

    public void setLast_modify(String last_modify) {
        this.last_modify = last_modify;
    }
}
