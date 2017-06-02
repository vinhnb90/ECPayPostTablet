package views.ecpay.com.postabletecpay.util.entities.sqlite;

/**
 * Created by VinhNB on 5/22/2017.
 */

public class Customer {
    private String code;
    private String name;
    private String address;
    private String pcCode;
    private String pcCodeExt;
    private String phoneByevn;
    private String phoneByecp;
    private String bookCmis;
    private String electricityMeter;
    private String inning;
    private String status;
    private String bankAccount;
    private String idNumber;
    private String bankName;
    private String edongKey;
    private boolean isChecked;

    public Customer(String code, String name, String address, String pcCode, String pcCodeExt, String phoneByevn, String phoneByecp, String bookCmis, String electricityMeter, String inning, String status, String bankAccount, String idNumber, String bankName, String edongKey, boolean isChecked) {
        this.code = code;
        this.name = name;
        this.address = address;
        this.pcCode = pcCode;
        this.pcCodeExt = pcCodeExt;
        this.phoneByevn = phoneByevn;
        this.phoneByecp = phoneByecp;
        this.bookCmis = bookCmis;
        this.electricityMeter = electricityMeter;
        this.inning = inning;
        this.status = status;
        this.bankAccount = bankAccount;
        this.idNumber = idNumber;
        this.bankName = bankName;
        this.edongKey = edongKey;
        this.isChecked = isChecked;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getPcCode() {
        return pcCode;
    }

    public void setPcCode(String pcCode) {
        this.pcCode = pcCode;
    }

    public String getPcCodeExt() {
        return pcCodeExt;
    }

    public void setPcCodeExt(String pcCodeExt) {
        this.pcCodeExt = pcCodeExt;
    }

    public String getPhoneByevn() {
        return phoneByevn;
    }

    public void setPhoneByevn(String phoneByevn) {
        this.phoneByevn = phoneByevn;
    }

    public String getPhoneByecp() {
        return phoneByecp;
    }

    public void setPhoneByecp(String phoneByecp) {
        this.phoneByecp = phoneByecp;
    }

    public String getBookCmis() {
        return bookCmis;
    }

    public void setBookCmis(String bookCmis) {
        this.bookCmis = bookCmis;
    }

    public String getElectricityMeter() {
        return electricityMeter;
    }

    public void setElectricityMeter(String electricityMeter) {
        this.electricityMeter = electricityMeter;
    }

    public String getInning() {
        return inning;
    }

    public void setInning(String inning) {
        this.inning = inning;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getEdongKey() {
        return edongKey;
    }

    public void setEdongKey(String edongKey) {
        this.edongKey = edongKey;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
