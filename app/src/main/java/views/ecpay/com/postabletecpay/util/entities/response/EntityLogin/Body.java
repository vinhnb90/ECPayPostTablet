package views.ecpay.com.postabletecpay.util.entities.response.EntityLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {

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
@SerializedName("pin-login")
@Expose
private String pinLogin;
@SerializedName("response-login")
@Expose
private String responseLogin;

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

public String getPinLogin() {
return pinLogin;
}

public void setPinLogin(String pinLogin) {
this.pinLogin = pinLogin;
}

public String getResponseLogin() {
return responseLogin;
}

public void setResponseLogin(String responseLogin) {
this.responseLogin = responseLogin;
}

}