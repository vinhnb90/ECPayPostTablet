package views.ecpay.com.postabletecpay.util.entities.request.EntityDataZip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/1/17.
 */

public class BodyListDataZipRequest {

    @SerializedName("audit-number")
    @Expose
    private long auditNumber;
    @SerializedName("mac")
    @Expose
    private String mac;
    @SerializedName("disk-drive")
    @Expose
    private String diskDrive;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("pc-code")
    @Expose
    private String pcCode;
    @SerializedName("book-cmis")
    @Expose
    private String bookCmis;
    @SerializedName("from-id-changed")
    @Expose
    private long fromIdChanged;
    @SerializedName("from-date-changed")
    @Expose
    private long fromDateChanged;

    public long getAuditNumber() {
        return auditNumber;
    }

    public void setAuditNumber(long auditNumber) {
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

    public String getPcCode() {
        return pcCode;
    }

    public void setPcCode(String pcCode) {
        this.pcCode = pcCode;
    }

    public String getBookCmis() {
        return bookCmis;
    }

    public void setBookCmis(String bookCmis) {
        this.bookCmis = bookCmis;
    }

    public long getFromIdChanged() {
        return fromIdChanged;
    }

    public void setFromIdChanged(long fromIdChanged) {
        this.fromIdChanged = fromIdChanged;
    }

    public long getFromDateChanged() {
        return fromDateChanged;
    }

    public void setFromDateChanged(long fromDateChanged) {
        this.fromDateChanged = fromDateChanged;
    }
}
