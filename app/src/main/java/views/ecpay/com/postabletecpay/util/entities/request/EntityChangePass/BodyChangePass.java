package views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyChangePass {

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
    @SerializedName("pin-login")
    @Expose
    private String pinLogin;

    @SerializedName("session")
    @Expose
    private String session;
    @SerializedName("new-pin")
    @Expose
    private String newPin;
    @SerializedName("retry-pin")
    @Expose
    private String retryPin;

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

    public String getPinLogin() {
        return pinLogin;
    }

    public void setPinLogin(String pinLogin) {
        this.pinLogin = pinLogin;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }

    public String getRetryPin() {
        return retryPin;
    }

    public void setRetryPin(String retryPin) {
        this.retryPin = retryPin;
    }
}