package views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tima on 6/19/17.
 */

public class BodyPostBillRequest {
    @SerializedName("audit-number")
    @Expose
    private long audit_number;
    @SerializedName("mac")
    @Expose
    private String mac;
    @SerializedName("disk-drive")
    @Expose
    private String disk_drive;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("list-transaction-off")
    @Expose
    private ArrayList<ListTransactionOff> list_transaction_off;

    public long getAudit_number() {
        return audit_number;
    }

    public void setAudit_number(long audit_number) {
        this.audit_number = audit_number;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDisk_drive() {
        return disk_drive;
    }

    public void setDisk_drive(String disk_drive) {
        this.disk_drive = disk_drive;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public ArrayList<ListTransactionOff> getList_transaction_off() {
        return list_transaction_off;
    }

    public void setList_transaction_off(ArrayList<ListTransactionOff> list_transaction_off) {
        this.list_transaction_off = list_transaction_off;
    }
}
