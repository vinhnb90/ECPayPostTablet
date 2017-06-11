package views.ecpay.com.postabletecpay.util.entities.request.EntityDeleteBillOnline;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyDeleteBillOnlineRequest implements Serializable
{

@SerializedName("amount")
@Expose
private Integer amount;
@SerializedName("audit-number")
@Expose
private Integer auditNumber;
@SerializedName("bill-id")
@Expose
private Integer billId;
@SerializedName("customer-code")
@Expose
private String customerCode;
@SerializedName("billing-date")
@Expose
private String billingDate;
@SerializedName("trace-number")
@Expose
private String traceNumber;
@SerializedName("reason")
@Expose
private String reason;
@SerializedName("disk-drive")
@Expose
private String diskDrive;
@SerializedName("mac")
@Expose
private String mac;
@SerializedName("signature")
@Expose
private String signature;
private final static long serialVersionUID = -7810941760801107607L;

public Integer getAmount() {
return amount;
}

public void setAmount(Integer amount) {
this.amount = amount;
}

public Integer getAuditNumber() {
return auditNumber;
}

public void setAuditNumber(Integer auditNumber) {
this.auditNumber = auditNumber;
}

public Integer getBillId() {
return billId;
}

public void setBillId(Integer billId) {
this.billId = billId;
}

public String getCustomerCode() {
return customerCode;
}

public void setCustomerCode(String customerCode) {
this.customerCode = customerCode;
}

public String getBillingDate() {
return billingDate;
}

public void setBillingDate(String billingDate) {
this.billingDate = billingDate;
}

public String getTraceNumber() {
return traceNumber;
}

public void setTraceNumber(String traceNumber) {
this.traceNumber = traceNumber;
}

public String getReason() {
return reason;
}

public void setReason(String reason) {
this.reason = reason;
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