package views.ecpay.com.postabletecpay.util.entities.response.EntityEVN;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 5/30/17.
 */

public class ListBookCmisResponse {

    @SerializedName("bookCmis")
    @Expose
    private String bookCmis;

    @SerializedName("pcCode")
    @Expose
    private String pcCode;

    @SerializedName("pcCodeExt")
    @Expose
    private String pcCodeExt;

    @SerializedName("inningDate")
    @Expose
    private String inningDate;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("strStatus")
    @Expose
    private String strStatus;

    @SerializedName("strCreateDate")
    @Expose
    private String strCreateDate;

    @SerializedName("strChangeDate")
    @Expose
    private String strChangeDate;

    @SerializedName("idChanged")
    @Expose
    private int idChanged;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("parentPcCode")
    @Expose
    private String parentPcCode;

    @SerializedName("countBill")
    @Expose
    private int countBill;

    @SerializedName("countBillPaid")
    @Expose
    private int countBillPaid;

    @SerializedName("countCustomer")
    @Expose
    private int countCustomer;

    @SerializedName("listCustomer")
    @Expose
    private String listCustomer;

    @SerializedName("listBillUnpaid")
    @Expose
    private String listBillUnpaid;

    @SerializedName("listBillPaid")
    @Expose
    private String listBillPaid;

    public String getBookCmis() {
        return bookCmis;
    }

    public void setBookCmis(String bookCmis) {
        this.bookCmis = bookCmis;
    }

    public String getPcCodeExt() {
        return pcCodeExt;
    }

    public void setPcCodeExt(String pcCodeExt) {
        this.pcCodeExt = pcCodeExt;
    }

    public String getPcCode() {
        return pcCode;
    }

    public void setPcCode(String pcCode) {
        this.pcCode = pcCode;
    }

    public String getInningDate() {
        return inningDate;
    }

    public void setInningDate(String inningDate) {
        this.inningDate = inningDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrCreateDate() {
        return strCreateDate;
    }

    public void setStrCreateDate(String strCreateDate) {
        this.strCreateDate = strCreateDate;
    }

    public String getStrChangeDate() {
        return strChangeDate;
    }

    public void setStrChangeDate(String strChangeDate) {
        this.strChangeDate = strChangeDate;
    }

    public int getIdChanged() {
        return idChanged;
    }

    public void setIdChanged(int idChanged) {
        this.idChanged = idChanged;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParentPcCode() {
        return parentPcCode;
    }

    public void setParentPcCode(String parentPcCode) {
        this.parentPcCode = parentPcCode;
    }

    public int getCountBill() {
        return countBill;
    }

    public void setCountBill(int countBill) {
        this.countBill = countBill;
    }

    public int getCountBillPaid() {
        return countBillPaid;
    }

    public void setCountBillPaid(int countBillPaid) {
        this.countBillPaid = countBillPaid;
    }

    public int getCountCustomer() {
        return countCustomer;
    }

    public void setCountCustomer(int countCustomer) {
        this.countCustomer = countCustomer;
    }

    public String getListCustomer() {
        return listCustomer;
    }

    public void setListCustomer(String listCustomer) {
        this.listCustomer = listCustomer;
    }

    public String getListBillUnpaid() {
        return listBillUnpaid;
    }

    public void setListBillUnpaid(String listBillUnpaid) {
        this.listBillUnpaid = listBillUnpaid;
    }

    public String getListBillPaid() {
        return listBillPaid;
    }

    public void setListBillPaid(String listBillPaid) {
        this.listBillPaid = listBillPaid;
    }
}
