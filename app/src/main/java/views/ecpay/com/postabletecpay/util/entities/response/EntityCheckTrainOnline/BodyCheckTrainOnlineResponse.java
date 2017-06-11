package views.ecpay.com.postabletecpay.util.entities.response.EntityCheckTrainOnline;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyCheckTrainOnlineResponse implements Serializable
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
@SerializedName("edong")
@Expose
private String edong;
@SerializedName("customer-code")
@Expose
private String customerCode;
@SerializedName("bill-id")
@Expose
private Long billId;
@SerializedName("amount")
@Expose
private Long amount;
@SerializedName("request-date")
@Expose
private String requestDate;
private final static long serialVersionUID = 5223677393456336585L;

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

public String getEdong() {
return edong;
}

public void setEdong(String edong) {
this.edong = edong;
}

public String getCustomerCode() {
return customerCode;
}

public void setCustomerCode(String customerCode) {
this.customerCode = customerCode;
}

public Long getBillId() {
return billId;
}

public void setBillId(Long billId) {
this.billId = billId;
}

public Long getAmount() {
return amount;
}

public void setAmount(Long amount) {
this.amount = amount;
}

public String getRequestDate() {
return requestDate;
}

public void setRequestDate(String requestDate) {
this.requestDate = requestDate;
}

}