package views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/15/17.
 */

public class BodyCustomerResponse {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("pcCode")
    @Expose
    private String pcCode;
    @SerializedName("pcCodeExt")
    @Expose
    private String pcCodeExt;
    @SerializedName("customerCode")
    @Expose
    private String customerCode;
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
    @SerializedName("phoneByEVN")
    @Expose
    private String phoneByEVN;
    @SerializedName("phoneByECP")
    @Expose
    private String phoneByECP;
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
    private String countPeriod;
    @SerializedName("team")
    @Expose
    private String team;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
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

    public String getPhoneByEVN() {
        return phoneByEVN;
    }

    public void setPhoneByEVN(String phoneByEVN) {
        this.phoneByEVN = phoneByEVN;
    }

    public String getPhoneByECP() {
        return phoneByECP;
    }

    public void setPhoneByECP(String phoneByECP) {
        this.phoneByECP = phoneByECP;
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

    public String getCountPeriod() {
        return countPeriod;
    }

    public void setCountPeriod(String countPeriod) {
        this.countPeriod = countPeriod;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
