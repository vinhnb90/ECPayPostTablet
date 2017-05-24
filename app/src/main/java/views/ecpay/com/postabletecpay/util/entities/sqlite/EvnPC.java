package views.ecpay.com.postabletecpay.util.entities.sqlite;

/**
 * Created by VinhNB on 5/22/2017.
 */

public class EvnPC {
    private int pcId;
    private int parentId;
    private String code;
    private String ext;
    private String fullName;
    private String shortName;
    private String address;
    private String taxCode;
    private String phone1;
    private String phone2;
    private String fax;
    private int level;

    public EvnPC() {
    }

    public EvnPC(int pcId, int parentId, String code, String ext, String fullName, String shortName, String address, String taxCode, String phone1, String phone2, String fax, int level) {
        this.pcId = pcId;
        this.parentId = parentId;
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

}
