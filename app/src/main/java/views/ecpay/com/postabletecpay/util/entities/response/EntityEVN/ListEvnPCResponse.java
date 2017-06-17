package views.ecpay.com.postabletecpay.util.entities.response.EntityEVN;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListEvnPCResponse {

    @SerializedName("parentId")
    @Expose
    private Integer parentId;
    @SerializedName("strParentId")
    @Expose
    private String strParentId;
    @SerializedName("pcId")
    @Expose
    private Integer pcId;
    @SerializedName("strPcId")
    @Expose
    private Integer strPcId;
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
    private String address;
    @SerializedName("taxCode")
    @Expose
    private String taxCode;
    @SerializedName("phone1")
    @Expose
    private String phone1;
    @SerializedName("phone2")
    @Expose
    private String phone2;
    @SerializedName("fax")
    @Expose
    private String fax;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("strLevel")
    @Expose
    private String strLevel;
    @SerializedName("mailTo")
    @Expose
    private String mailTo;
    @SerializedName("mailCc")
    @Expose
    private String mailCc;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("strStatus")
    @Expose
    private String strStatus;
    @SerializedName("dateCreated")
    @Expose
    private String dateCreated;
    @SerializedName("strDateCreated")
    @Expose
    private String strDateCreated;
    @SerializedName("idChanged")
    @Expose
    private Integer idChanged;
    @SerializedName("dateChanged")
    @Expose
    private String dateChanged;
    @SerializedName("strDateChanged")
    @Expose
    private String strDateChanged;
    @SerializedName("regionId")
    @Expose
    private Integer regionId;
    @SerializedName("parentPcCode")
    @Expose
    private String parentPcCode;
    @SerializedName("cardPrefix")
    @Expose
    private Integer cardPrefix;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailCc() {
        return mailCc;
    }

    public void setMailCc(String mailCc) {
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

    public String getParentPcCode() {
        return parentPcCode;
    }

    public void setParentPcCode(String parentPcCode) {
        this.parentPcCode = parentPcCode;
    }

    public Integer getCardPrefix() {
        return cardPrefix;
    }

    public void setCardPrefix(Integer cardPrefix) {
        this.cardPrefix = cardPrefix;
    }

    public String getStrParentId() {
        return strParentId;
    }

    public void setStrParentId(String strParentId) {
        this.strParentId = strParentId;
    }

    public Integer getStrPcId() {
        return strPcId;
    }

    public void setStrPcId(Integer strPcId) {
        this.strPcId = strPcId;
    }

    public String getStrLevel() {
        return strLevel;
    }

    public void setStrLevel(String strLevel) {
        this.strLevel = strLevel;
    }

    public String getStrStatus() {
        return strStatus;
    }

    public void setStrStatus(String strStatus) {
        this.strStatus = strStatus;
    }

    public String getStrDateCreated() {
        return strDateCreated;
    }

    public void setStrDateCreated(String strDateCreated) {
        this.strDateCreated = strDateCreated;
    }

    public String getStrDateChanged() {
        return strDateChanged;
    }

    public void setStrDateChanged(String strDateChanged) {
        this.strDateChanged = strDateChanged;
    }
}