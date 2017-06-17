package views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillInsideCustomer implements Serializable {
    @SerializedName("customerCode")
    @Expose
    private String customerCode;
    @SerializedName("customerPayCode")
    @Expose
    private String customerPayCode;
    @SerializedName("billId")
    @Expose
    private Integer billId;
    @SerializedName("term")
    @Expose
    private String term;
    @SerializedName("strTerm")
    @Expose
    private String strTerm;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("period")
    @Expose
    private String period;
    @SerializedName("issueDate")
    @Expose
    private String issueDate;
    @SerializedName("strIssueDate")
    @Expose
    private String strIssueDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("seri")
    @Expose
    private String seri;
    @SerializedName("pcCode")
    @Expose
    private String pcCode;
    @SerializedName("handoverCode")
    @Expose
    private String handoverCode;
    @SerializedName("cashierCode")
    @Expose
    private String cashierCode;
    @SerializedName("bookCmis")
    @Expose
    private String bookCmis;
    @SerializedName("fromDate")
    @Expose
    private String fromDate;
    @SerializedName("toDate")
    @Expose
    private String toDate;
    @SerializedName("strFromDate")
    @Expose
    private String strFromDate;
    @SerializedName("strToDate")
    @Expose
    private String strToDate;
    @SerializedName("home")
    @Expose
    private String home;
    @SerializedName("tax")
    @Expose
    private Double tax;
    @SerializedName("billNum")
    @Expose
    private String billNum;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("priceDetails")
    @Expose
    private String priceDetails;
    @SerializedName("numeDetails")
    @Expose
    private String numeDetails;
    @SerializedName("amountDetails")
    @Expose
    private String amountDetails;
    @SerializedName("oldIndex")
    @Expose
    private String oldIndex;
    @SerializedName("newIndex")
    @Expose
    private String newIndex;
    @SerializedName("nume")
    @Expose
    private String nume;
    @SerializedName("amountNotTax")
    @Expose
    private Integer amountNotTax;
    @SerializedName("amountTax")
    @Expose
    private Integer amountTax;
    @SerializedName("multiple")
    @Expose
    private Integer multiple;
    @SerializedName("billType")
    @Expose
    private String billType;
    @SerializedName("typeIndex")
    @Expose
    private String typeIndex;
    @SerializedName("groupTypeIndex")
    @Expose
    private String groupTypeIndex;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("idChanged")
    @Expose
    private Long idChanged;
    @SerializedName("dateChanged")
    @Expose
    private String dateChanged;
    @SerializedName("edong")
    @Expose
    private String edong;
    @SerializedName("pcCodeExt")
    @Expose
    private String pcCodeExt;
    @SerializedName("code")
    @Expose
    private String code;
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
    @SerializedName("lastQuery")
    @Expose
    private String lastQuery;
    @SerializedName("groupType")
    @Expose
    private String groupType;
    @SerializedName("billingChannel")
    @Expose
    private String billingChannel;
    @SerializedName("billingType")
    @Expose
    private String billingType;
    @SerializedName("billingBy")
    @Expose
    private String billingBy;
    @SerializedName("cashierPay")
    @Expose
    private String cashierPay;
    @SerializedName("responseCode")
    @Expose
    private String responseCode;
    @SerializedName("description")
    @Expose
    private String description;
    private final static long serialVersionUID = -3897189969590582750L;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerPayCode() {
        return customerPayCode;
    }

    public void setCustomerPayCode(String customerPayCode) {
        this.customerPayCode = customerPayCode;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getStrTerm() {
        return strTerm;
    }

    public void setStrTerm(String strTerm) {
        this.strTerm = strTerm;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getStrIssueDate() {
        return strIssueDate;
    }

    public void setStrIssueDate(String strIssueDate) {
        this.strIssueDate = strIssueDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSeri() {
        return seri;
    }

    public void setSeri(String seri) {
        this.seri = seri;
    }

    public String getPcCode() {
        return pcCode;
    }

    public void setPcCode(String pcCode) {
        this.pcCode = pcCode;
    }

    public String getHandoverCode() {
        return handoverCode;
    }

    public void setHandoverCode(String handoverCode) {
        this.handoverCode = handoverCode;
    }

    public String getCashierCode() {
        return cashierCode;
    }

    public void setCashierCode(String cashierCode) {
        this.cashierCode = cashierCode;
    }

    public String getBookCmis() {
        return bookCmis;
    }

    public void setBookCmis(String bookCmis) {
        this.bookCmis = bookCmis;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getStrFromDate() {
        return strFromDate;
    }

    public void setStrFromDate(String strFromDate) {
        this.strFromDate = strFromDate;
    }

    public String getStrToDate() {
        return strToDate;
    }

    public void setStrToDate(String strToDate) {
        this.strToDate = strToDate;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getBillNum() {
        return billNum;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(String priceDetails) {
        this.priceDetails = priceDetails;
    }

    public String getNumeDetails() {
        return numeDetails;
    }

    public void setNumeDetails(String numeDetails) {
        this.numeDetails = numeDetails;
    }

    public String getAmountDetails() {
        return amountDetails;
    }

    public void setAmountDetails(String amountDetails) {
        this.amountDetails = amountDetails;
    }

    public String getOldIndex() {
        return oldIndex;
    }

    public void setOldIndex(String oldIndex) {
        this.oldIndex = oldIndex;
    }

    public String getNewIndex() {
        return newIndex;
    }

    public void setNewIndex(String newIndex) {
        this.newIndex = newIndex;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Integer getAmountNotTax() {
        return amountNotTax;
    }

    public void setAmountNotTax(Integer amountNotTax) {
        this.amountNotTax = amountNotTax;
    }

    public Integer getAmountTax() {
        return amountTax;
    }

    public void setAmountTax(Integer amountTax) {
        this.amountTax = amountTax;
    }

    public Integer getMultiple() {
        return multiple;
    }

    public void setMultiple(Integer multiple) {
        this.multiple = multiple;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getTypeIndex() {
        return typeIndex;
    }

    public void setTypeIndex(String typeIndex) {
        this.typeIndex = typeIndex;
    }

    public String getGroupTypeIndex() {
        return groupTypeIndex;
    }

    public void setGroupTypeIndex(String groupTypeIndex) {
        this.groupTypeIndex = groupTypeIndex;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Long getIdChanged() {
        return idChanged;
    }

    public void setIdChanged(Long idChanged) {
        this.idChanged = idChanged;
    }

    public String getDateChanged() {
        return dateChanged;
    }

    public void setDateChanged(String dateChanged) {
        this.dateChanged = dateChanged;
    }

    public String getEdong() {
        return edong;
    }

    public void setEdong(String edong) {
        this.edong = edong;
    }

    public String getPcCodeExt() {
        return pcCodeExt;
    }

    public void setPcCodeExt(String pcCodeExt) {
        this.pcCodeExt = pcCodeExt;
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

    public String getLastQuery() {
        return lastQuery;
    }

    public void setLastQuery(String lastQuery) {
        this.lastQuery = lastQuery;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getBillingChannel() {
        return billingChannel;
    }

    public void setBillingChannel(String billingChannel) {
        this.billingChannel = billingChannel;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getBillingBy() {
        return billingBy;
    }

    public void setBillingBy(String billingBy) {
        this.billingBy = billingBy;
    }

    public String getCashierPay() {
        return cashierPay;
    }

    public void setCashierPay(String cashierPay) {
        this.cashierPay = cashierPay;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}