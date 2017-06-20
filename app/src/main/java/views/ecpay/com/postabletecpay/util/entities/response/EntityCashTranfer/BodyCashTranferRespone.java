package views.ecpay.com.postabletecpay.util.entities.response.EntityCashTranfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by duydatpham on 6/17/17.
 */


public class BodyCashTranferRespone {


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

    @SerializedName("response-settlement")
    @Expose
    private String responseSettlement;


    @SerializedName("amount")
    @Expose
    private Long amount;



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

    public String getResponseSettlement() {
        return responseSettlement;
    }

    public void setResponseSettlement(String responseSettlement) {
        this.responseSettlement = responseSettlement;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public static class ResponseSettlement
    {
        @SerializedName("edong")
        @Expose
        private String edong;


        @SerializedName("traceNumber")
        @Expose
        private String traceNumber;

        public String getEdong() {
            return edong;
        }

        public void setEdong(String edong) {
            this.edong = edong;
        }

        public String getTraceNumber() {
            return traceNumber;
        }

        public void setTraceNumber(String traceNumber) {
            this.traceNumber = traceNumber;
        }
    }
}
