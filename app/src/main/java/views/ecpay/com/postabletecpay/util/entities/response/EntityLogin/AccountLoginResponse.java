package views.ecpay.com.postabletecpay.util.entities.response.EntityLogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountLoginResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("idNumber")
    @Expose
    private String idNumber;
    @SerializedName("idNumberDate")
    @Expose
    private String idNumberDate;
    @SerializedName("idNumberPlace")
    @Expose
    private String idNumberPlace;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("idAccount")
    @Expose
    private Integer idAccount;
    @SerializedName("edong")
    @Expose
    private String edong;
    @SerializedName("parentId")
    @Expose
    private Integer parentId;
    @SerializedName("parentEdong")
    @Expose
    private String parentEdong;
    @SerializedName("pin")
    @Expose
    private Object pin;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("balance")
    @Expose
    private Long balance;
    @SerializedName("lockMoney")
    @Expose
    private Integer lockMoney;
    @SerializedName("changedPIN")
    @Expose
    private Boolean changedPIN;
    @SerializedName("session")
    @Expose
    private String session;
    @SerializedName("verified")
    @Expose
    private Integer verified;
    @SerializedName("mac")
    @Expose
    private String mac;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("loginTime")
    @Expose
    private Long loginTime;
    @SerializedName("logoutTime")
    @Expose
    private Object logoutTime;
    @SerializedName("strLoginTime")
    @Expose
    private Object strLoginTime;
    @SerializedName("strLogoutTime")
    @Expose
    private Object strLogoutTime;
    @SerializedName("strType")
    @Expose
    private Object strType;

    public AccountLoginResponse(String status, String idNumber, String idNumberDate, String idNumberPlace, String name, String address, String phone, String email, String birthday, Integer idAccount, String edong, Integer parentId, String parentEdong, Object pin, Integer type, Long balance, Integer lockMoney, Boolean changedPIN, String session, Integer verified, String mac, String ip, Long loginTime, Object logoutTime, Object strLoginTime, Object strLogoutTime, Object strType) {

        this.status = status;
        this.idNumber = idNumber;
        this.idNumberDate = idNumberDate;
        this.idNumberPlace = idNumberPlace;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.birthday = birthday;
        this.idAccount = idAccount;
        this.edong = edong;
        this.parentId = parentId;
        this.parentEdong = parentEdong;
        this.pin = pin;
        this.type = type;
        this.balance = balance;
        this.lockMoney = lockMoney;
        this.changedPIN = changedPIN;
        this.session = session;
        this.verified = verified;
        this.mac = mac;
        this.ip = ip;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.strLoginTime = strLoginTime;
        this.strLogoutTime = strLogoutTime;
        this.strType = strType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Integer idAccount) {
        this.idAccount = idAccount;
    }

    public String getEdong() {
        return edong;
    }

    public void setEdong(String edong) {
        this.edong = edong;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentEdong() {
        return parentEdong;
    }

    public void setParentEdong(String parentEdong) {
        this.parentEdong = parentEdong;
    }

    public Object getPin() {
        return pin;
    }

    public void setPin(Object pin) {
        this.pin = pin;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Integer getLockMoney() {
        return lockMoney;
    }

    public void setLockMoney(Integer lockMoney) {
        this.lockMoney = lockMoney;
    }

    public Boolean getChangedPIN() {
        return changedPIN;
    }

    public void setChangedPIN(Boolean changedPIN) {
        this.changedPIN = changedPIN;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Integer getVerified() {
        return verified;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Object getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Object logoutTime) {
        this.logoutTime = logoutTime;
    }

    public Object getStrLoginTime() {
        return strLoginTime;
    }

    public void setStrLoginTime(Object strLoginTime) {
        this.strLoginTime = strLoginTime;
    }

    public Object getStrLogoutTime() {
        return strLogoutTime;
    }

    public void setStrLogoutTime(Object strLogoutTime) {
        this.strLogoutTime = strLogoutTime;
    }

    public Object getStrType() {
        return strType;
    }

    public void setStrType(Object strType) {
        this.strType = strType;
    }
}