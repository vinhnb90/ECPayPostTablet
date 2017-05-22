package views.ecpay.com.postabletecpay.util.entities.sqlite;

/**
 * Created by VinhNB on 5/22/2017.
 */

public class Account {
    private int id_account;
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

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
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

    public static Account setAccount(String edong,
                                     String name,
                                     String address,
                                     String email,
                                     String birthday,
                                     String session,
                                     long balance,
                                     int lockMoney,
                                     boolean changePIN,
                                     int verified,
                                     String mac,
                                     String ip,
                                     String strLoginTime,
                                     String strLogoutTime,
                                     int type,
                                     String status,
                                     String idNumber,
                                     String idNumberDate,
                                     String idNumberPlace,
                                     String parentEdong) {
        Account account = new Account();

        account.setEdong(edong);
        account.setName(name);
        account.setAddress(address);
        account.setEmail(email);
        account.setBirthday(birthday);
        account.setSession(session);
        account.setBalance(balance);
        account.setLockMoney(lockMoney);
        account.setChangePIN(changePIN);
        account.setVerified(verified);
        account.setMac(mac);
        account.setIp(ip);
        account.setStrLoginTime(strLoginTime);
        account.setStrLogoutTime(strLogoutTime);
        account.setType(type);
        account.setStatus(status);
        account.setIdNumber(idNumber);
        account.setIdNumberDate(idNumberDate);
        account.setIdNumberPlace(idNumberPlace);
        account.setEdong(parentEdong);

        return account;
    }
}
