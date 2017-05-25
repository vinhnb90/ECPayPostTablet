package views.ecpay.com.postabletecpay.util.entities.response.EntityLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyLoginResponse {
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
    @SerializedName("version-app")
    @Expose
    private String versionApp;
    @SerializedName("response-login")
    @Expose
//    private String responseLoginResponse;
    private ResponseLoginResponse responseLoginResponse;

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

    public String getVersionApp() {
        return versionApp;
    }

    public void setVersionApp(String versionApp) {
        this.versionApp = versionApp;
    }

    public ResponseLoginResponse getResponseLoginResponse() {
        return responseLoginResponse;
    }

    public void setResponseLoginResponse(ResponseLoginResponse responseLoginResponse) {
        this.responseLoginResponse = responseLoginResponse;
    }

   /* public String getResponseLoginResponse() {
        return responseLoginResponse;
    }

    public void setResponseLoginResponse(String responseLoginResponse) {
        this.responseLoginResponse = responseLoginResponse;
    }*/
}