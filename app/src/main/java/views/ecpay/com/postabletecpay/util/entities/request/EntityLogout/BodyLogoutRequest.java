package views.ecpay.com.postabletecpay.util.entities.request.EntityLogout;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyLogoutRequest implements Serializable
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
@SerializedName("session")
@Expose
private String session;
private final static long serialVersionUID = 5993237558803111072L;

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

public String getSession() {
return session;
}

public void setSession(String session) {
this.session = session;
}

}