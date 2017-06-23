package views.ecpay.com.postabletecpay.util.entities.response.EntityPostBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tima on 6/19/17.
 */

public class BodyPostBillReponse {
    @SerializedName("audit-number")
    @Expose
    private String audit_number;
    @SerializedName("mac")
    @Expose
    private String mac;
    @SerializedName("disk-drive")
    @Expose
    private String disk_drive;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("List")
    @Expose
    private ArrayList<ListPostBill> listPostBill;

    public String getAudit_number() {
        return audit_number;
    }

    public void setAudit_number(String audit_number) {
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

    public ArrayList<ListPostBill> getListPostBill() {
        return listPostBill;
    }

    public void setListPostBill(ArrayList<ListPostBill> listPostBill) {
        this.listPostBill = listPostBill;
    }
}
