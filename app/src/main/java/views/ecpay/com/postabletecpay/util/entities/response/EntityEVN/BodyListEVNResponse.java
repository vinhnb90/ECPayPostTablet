package views.ecpay.com.postabletecpay.util.entities.response.EntityEVN;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.ListEvnPCLoginResponse;

public class BodyListEVNResponse {
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
    @SerializedName("list-book-cmis")
    @Expose
    private String listBookCmissResponse;
    @SerializedName("list-pc")
    @Expose
    private String listEvnPCLoginResponse;

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

    public String getListBookCmissResponse() {
        return listBookCmissResponse;
    }

    public void setListBookCmissResponse(String listBookCmissResponse) {
        this.listBookCmissResponse = listBookCmissResponse;
    }

    public String getListEvnPCLoginResponse() {
        return listEvnPCLoginResponse;
    }

    public void setListEvnPCLoginResponse(String listEvnPCLoginResponse) {
        this.listEvnPCLoginResponse = listEvnPCLoginResponse;
    }
}