package views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CustomerInsideBody implements Serializable
{
    @SerializedName("regionId")
    @Expose
    private Integer regionId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("pcCode")
    @Expose
    private String pcCode;
    @SerializedName("pcCodeExt")
    @Expose
    private String pcCodeExt;
    @SerializedName("cardNo")
    @Expose
    private String cardNo;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nameNosign")
    @Expose
    private String nameNosign;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("addressNosign")
    @Expose
    private String addressNosign;
    @SerializedName("phoneByevn")
    @Expose
    private String phoneByevn;
    @SerializedName("phoneByecp")
    @Expose
    private String phoneByecp;
    @SerializedName("bookCmis")
    @Expose
    private String bookCmis;
    @SerializedName("electricityMeter")
    @Expose
    private String electricityMeter;
    @SerializedName("inning")
    @Expose
    private String inning;
    @SerializedName("road")
    @Expose
    private String road;
    @SerializedName("station")
    @Expose
    private String station;
    @SerializedName("taxCode")
    @Expose
    private String taxCode;
    @SerializedName("trade")
    @Expose
    private String trade;
    @SerializedName("countPeriod")
    @Expose
    private Integer countPeriod;
    @SerializedName("team")
    @Expose
    private String team;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("lastQuery")
    @Expose
    private String lastQuery;
    @SerializedName("idChanged")
    @Expose
    private Integer idChanged;
    @SerializedName("dateChanged")
    @Expose
    private Integer dateChanged;
    @SerializedName("bankAccount")
    @Expose
    private String bankAccount;
    @SerializedName("idNumber")
    @Expose
    private String idNumber;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("checkBillEvn")
    @Expose
    private Integer checkBillEvn;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("listBill")
    @Expose
    private List<BillInsideCustomer> listBill = null;
    @SerializedName("countBill")
    @Expose
    private Integer countBill;
    private final static long serialVersionUID = 683361497457651581L;

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameNosign() {
        return nameNosign;
    }

    public void setNameNosign(String nameNosign) {
        this.nameNosign = nameNosign;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressNosign() {
        return addressNosign;
    }

    public void setAddressNosign(String addressNosign) {
        this.addressNosign = addressNosign;
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

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getTrade() {
        return trade;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public Integer getCountPeriod() {
        return countPeriod;
    }

    public void setCountPeriod(Integer countPeriod) {
        this.countPeriod = countPeriod;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastQuery() {
        return lastQuery;
    }

    public void setLastQuery(String lastQuery) {
        this.lastQuery = lastQuery;
    }

    public Integer getIdChanged() {
        return idChanged;
    }

    public void setIdChanged(Integer idChanged) {
        this.idChanged = idChanged;
    }

    public Integer getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(Integer dateChanged) {
        this.dateChanged = dateChanged;
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

    public Integer getCheckBillEvn() {
        return checkBillEvn;
    }

    public void setCheckBillEvn(Integer checkBillEvn) {
        this.checkBillEvn = checkBillEvn;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<BillInsideCustomer> getListBill() {
        return listBill;
    }

    public void setListBill(List<BillInsideCustomer> listBill) {
        this.listBill = listBill;
    }

    public Integer getCountBill() {
        return countBill;
    }

    public void setCountBill(Integer countBill) {
        this.countBill = countBill;
    }

}