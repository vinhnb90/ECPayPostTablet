package views.ecpay.com.postabletecpay.util.entities.request.EntityCashTranfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by duydatpham on 6/17/17.
 */

public class BodyCashTranferRequest implements Serializable
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

    @SerializedName("send-phone")
    @Expose
    private String sendPhone;

    @SerializedName("otp")
    @Expose
    private String otp = "";

    @SerializedName("received-phone")
    @Expose
    private String receivedPhone;

    @SerializedName("amount")
    @Expose
    private Long amount;

    @SerializedName("description")
    @Expose
    private String description;

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    @SerializedName("partner-code")
    @Expose
    private String partnerCode = "DT0605";



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

    public String getSendPhone() {
        return sendPhone;
    }

    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getReceivedPhone() {
        return receivedPhone;
    }

    public void setReceivedPhone(String receivedPhone) {
        this.receivedPhone = receivedPhone;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
