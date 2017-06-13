package views.ecpay.com.postabletecpay.util.entities.response.EntityDeleteBillOnline;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyDeleteBillOnlineRespone implements Serializable
{

@SerializedName("audit-number")
@Expose
private Long auditNumber;
@SerializedName("disk-drive")
@Expose
private String diskDrive;
@SerializedName("mac")
@Expose
private String mac;
@SerializedName("signature")
@Expose
private String signature;
private final static long serialVersionUID = 7037756095110182669L;

public Long getAuditNumber() {
return auditNumber;
}

public void setAuditNumber(Long auditNumber) {
this.auditNumber = auditNumber;
}

public String getDiskDrive() {
return diskDrive;
}

public void setDiskDrive(String diskDrive) {
this.diskDrive = diskDrive;
}

public String getMac() {
return mac;
}

public void setMac(String mac) {
this.mac = mac;
}

public String getSignature() {
return signature;
}

public void setSignature(String signature) {
this.signature = signature;
}

}