package views.ecpay.com.postabletecpay.util.entities.sqlite;

/**
 * Created by VinhNB on 5/22/2017.
 */

public class EvnPC {
    private int pcId;
    private int parentId;
    private String code;
    private String strParentId;
    private String ext;
    private String fullName;
    private String shortName;
    private String address;
    private String taxCode;
    private String phone1;
    private String phone2;
    private String fax;
    private int level;
    private String strLevel;
    private String mailTo;
    private String mailCc;
    private int status;
    private String strStatus;
    private String dateCreated;
    private String strDateCreated;
    private int idChanged;
    private String dateChanged;
    private String strDateChanged;
    private int regionId;
    private String parentPcCode;
    private String cardPrefix;

    public EvnPC() {
    }

    public EvnPC(int pcId, int parentId, String code, String strParentId, String ext, String fullName, String shortName, String address, String taxCode, String phone1, String phone2, String fax, int level, String strLevel, String mailTo, String mailCc, int status, String strStatus, String dateCreated, String strDateCreated, int idChanged, String dateChanged, String strDateChanged, int regionId, String parentPcCode, String cardPrefix) {
        this.pcId = pcId;
        this.parentId = parentId;
        this.code = code;
        this.strParentId = strParentId;
        this.ext = ext;
        this.fullName = fullName;
        this.shortName = shortName;
        this.address = address;
        this.taxCode = taxCode;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.fax = fax;
        this.level = level;
        this.strLevel = strLevel;
        this.mailTo = mailTo;
        this.mailCc = mailCc;
        this.status = status;
        this.strStatus = strStatus;
        this.dateCreated = dateCreated;
        this.strDateCreated = strDateCreated;
        this.idChanged = idChanged;
        this.dateChanged = dateChanged;
        this.strDateChanged = strDateChanged;
        this.regionId = regionId;
        this.parentPcCode = parentPcCode;
        this.cardPrefix = cardPrefix;
    }

    public int getPcId() {
        return pcId;
    }

    public void setPcId(int pcId) {
        this.pcId = pcId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStrParentId() {
        return strParentId;
    }

    public void setStrParentId(String strParentId) {
        this.strParentId = strParentId;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getStrLevel() {
        return strLevel;
    }

    public void setStrLevel(String strLevel) {
        this.strLevel = strLevel;
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getStrDateCreated() {
        return strDateCreated;
    }

    public void setStrDateCreated(String strDateCreated) {
        this.strDateCreated = strDateCreated;
    }

    public int getIdChanged() {
        return idChanged;
    }

    public void setIdChanged(int idChanged) {
        this.idChanged = idChanged;
    }

    public String getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(String dateChanged) {
        this.dateChanged = dateChanged;
    }

    public String getStrDateChanged() {
        return strDateChanged;
    }

    public void setStrDateChanged(String strDateChanged) {
        this.strDateChanged = strDateChanged;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getParentPcCode() {
        return parentPcCode;
    }

    public void setParentPcCode(String parentPcCode) {
        this.parentPcCode = parentPcCode;
    }

    public String getCardPrefix() {
        return cardPrefix;
    }

    public void setCardPrefix(String cardPrefix) {
        this.cardPrefix = cardPrefix;
    }
}
