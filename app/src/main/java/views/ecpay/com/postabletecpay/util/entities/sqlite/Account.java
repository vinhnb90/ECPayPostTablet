package views.ecpay.com.postabletecpay.util.entities.sqlite;

/**
 * Created by VinhNB on 5/22/2017.
 */

public class Account {
    private String edong;
    private String name;
    private String address;
    private String email;
    private String birthday;
    private String session;
    private long balance;
    private int lockMoney;
    private boolean changePIN;
    private int verified;

    private String mac;
    private String ip;
    private String strLoginTime;
    private String strLogoutTime;
    private int type;
    private String status;
    private String idNumber;
    private String idNumberDate;
    private String idNumberPlace;
    private String parentEdong;

    public Account(String edong, String name, String address, String email, String birthday, String session, long balance, int lockMoney, boolean changePIN, int verified, String mac, String ip, String strLoginTime, String strLogoutTime, int type, String status, String idNumber, String idNumberDate, String idNumberPlace, String parentEdong) {
        this.edong = edong;
        this.name = name;
        this.address = address;
        this.email = email;
        this.birthday = birthday;
        this.session = session;
        this.balance = balance;
        this.lockMoney = lockMoney;
        this.changePIN = changePIN;
        this.verified = verified;
        this.mac = mac;
        this.ip = ip;
        this.strLoginTime = strLoginTime;
        this.strLogoutTime = strLogoutTime;
        this.type = type;
        this.status = status;
        this.idNumber = idNumber;
        this.idNumberDate = idNumberDate;
        this.idNumberPlace = idNumberPlace;
        this.parentEdong = parentEdong;
    }

    public String getEdong() {
        return edong;
    }

    public void setEdong(String edong) {
        this.edong = edong;
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

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public int getLockMoney() {
        return lockMoney;
    }

    public void setLockMoney(int lockMoney) {
        this.lockMoney = lockMoney;
    }

    public boolean isChangePIN() {
        return changePIN;
    }

    public void setChangePIN(boolean changePIN) {
        this.changePIN = changePIN;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
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

    public String getStrLoginTime() {
        return strLoginTime;
    }

    public void setStrLoginTime(String strLoginTime) {
        this.strLoginTime = strLoginTime;
    }

    public String getStrLogoutTime() {
        return strLogoutTime;
    }

    public void setStrLogoutTime(String strLogoutTime) {
        this.strLogoutTime = strLogoutTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getParentEdong() {
        return parentEdong;
    }

    public void setParentEdong(String parentEdong) {
        this.parentEdong = parentEdong;
    }
}
