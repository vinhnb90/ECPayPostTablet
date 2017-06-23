package views.ecpay.com.postabletecpay.util.entities.request.EntityUpdateAccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tima on 6/22/17.
 */

public class BodyUpdateAccountRequest {
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
    @SerializedName("edong")
    @Expose
    private String edong;
    @SerializedName("edong-name")
    @Expose
    private String edongName;
    @SerializedName("edong-address")
    @Expose
    private String edongAddress;
    @SerializedName("id-sex")
    @Expose
    private int idSex;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("id-number-type")
    @Expose
    private int idNumberType;
    @SerializedName("id-number")
    @Expose
    private String idNumber;
    @SerializedName("id-number-date")
    @Expose
    private String idNumberDate;
    @SerializedName("id-number-place")
    @Expose
    private String idNumberPlace;
    @SerializedName("province-id")
    @Expose
    private int provinceId;
    @SerializedName("district-id")
    @Expose
    private int districtId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("session")
    @Expose
    private String session;
    @SerializedName("partner-code")
    @Expose
    private String partnerCode;

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

    public String getEdong() {
        return edong;
    }

    public void setEdong(String edong) {
        this.edong = edong;
    }

    public String getEdongName() {
        return edongName;
    }

    public void setEdongName(String edongName) {
        this.edongName = edongName;
    }

    public String getEdongAddress() {
        return edongAddress;
    }

    public void setEdongAddress(String edongAddress) {
        this.edongAddress = edongAddress;
    }

    public int getIdSex() {
        return idSex;
    }

    public void setIdSex(int idSex) {
        this.idSex = idSex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getIdNumberType() {
        return idNumberType;
    }

    public void setIdNumberType(int idNumberType) {
        this.idNumberType = idNumberType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdNumberDate() {
        return idNumberDate;
    }

    public void setIdNumberDate(String idNumberDate) {
        this.idNumberDate = idNumberDate;
    }

    public String getIdNumberPlace() {
        return idNumberPlace;
    }

    public void setIdNumberPlace(String idNumberPlace) {
        this.idNumberPlace = idNumberPlace;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }
}
