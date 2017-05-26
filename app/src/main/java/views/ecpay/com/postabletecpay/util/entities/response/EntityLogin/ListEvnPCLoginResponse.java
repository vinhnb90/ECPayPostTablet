package views.ecpay.com.postabletecpay.util.entities.response.EntityLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListEvnPCLoginResponse {

    @SerializedName("parentId")
    @Expose
    private Integer parentId;
    @SerializedName("pcId")
    @Expose
    private Integer pcId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("ext")
    @Expose
    private String ext;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("shortName")
    @Expose
    private String shortName;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("taxCode")
    @Expose
    private Object taxCode;
    @SerializedName("phone1")
    @Expose
    private Object phone1;
    @SerializedName("phone2")
    @Expose
    private Object phone2;
    @SerializedName("fax")
    @Expose
    private Object fax;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("mailTo")
    @Expose
    private Object mailTo;
    @SerializedName("mailCc")
    @Expose
    private Object mailCc;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("idChanged")
    @Expose
    private Integer idChanged;
    @SerializedName("dateChanged")
    @Expose
    private String dateChanged;
    @SerializedName("regionId")
    @Expose
    private Integer regionId;

    public ListEvnPCLoginResponse(Integer parentId, Integer pcId, String code, String ext, String fullName, String shortName, Object address, Object taxCode, Object phone1, Object phone2, Object fax, Integer level, Object mailTo, Object mailCc, Integer status, String dateCreated, Integer idChanged, String dateChanged, Integer regionId) {
        this.parentId = parentId;
        this.pcId = pcId;
        this.code = code;
        this.ext = ext;
        this.fullName = fullName;
        this.shortName = shortName;
        this.address = address;
        this.taxCode = taxCode;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.fax = fax;
        this.level = level;
        this.mailTo = mailTo;
        this.mailCc = mailCc;
        this.status = status;
        this.dateCreated = dateCreated;
        this.idChanged = idChanged;
        this.dateChanged = dateChanged;
        this.regionId = regionId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getPcId() {
        return pcId;
    }

    public void setPcId(Integer pcId) {
        this.pcId = pcId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(Object taxCode) {
        this.taxCode = taxCode;
    }

    public Object getPhone1() {
        return phone1;
    }

    public void setPhone1(Object phone1) {
        this.phone1 = phone1;
    }

    public Object getPhone2() {
        return phone2;
    }

    public void setPhone2(Object phone2) {
        this.phone2 = phone2;
    }

    public Object getFax() {
        return fax;
    }

    public void setFax(Object fax) {
        this.fax = fax;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Object getMailTo() {
        return mailTo;
    }

    public void setMailTo(Object mailTo) {
        this.mailTo = mailTo;
    }

    public Object getMailCc() {
        return mailCc;
    }

    public void setMailCc(Object mailCc) {
        this.mailCc = mailCc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Integer getIdChanged() {
        return idChanged;
    }

    public void setIdChanged(Integer idChanged) {
        this.idChanged = idChanged;
    }

    public String getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(String dateChanged) {
        this.dateChanged = dateChanged;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

}