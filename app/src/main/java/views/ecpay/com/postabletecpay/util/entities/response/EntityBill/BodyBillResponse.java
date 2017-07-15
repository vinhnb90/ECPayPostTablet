package views.ecpay.com.postabletecpay.util.entities.response.EntityBill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by macbook on 6/15/17.
 */

public class BodyBillResponse {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("customerCode")
    @Expose
    private String customerCode;
    @SerializedName("billId")
    @Expose
    private String billId;
    @SerializedName("term")
    @Expose
    private String term;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("String")
    @Expose
    private String period;
    @SerializedName("issueDate")
    @Expose
    private String issueDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("seri")
    @Expose
    private String seri;
    @SerializedName("pcCode")
    @Expose
    private String pcCode;
    @SerializedName("handOverCode")
    @Expose
    private String handOverCode;
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
    @SerializedName("home")
    @Expose
    private String home;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("billNum")
    @Expose
    private String billNum;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("priceDetail")
    @Expose
    private String priceDetail;
    @SerializedName("numeDetail")
    @Expose
    private String numeDetail;
    @SerializedName("amountDetail")
    @Expose
    private String amountDetail;
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
    private String amountNotTax;
    @SerializedName("amountTax")
    @Expose
    private String amountTax;
    @SerializedName("multiple")
    @Expose
    private String multiple;
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
    @SerializedName("edong")
    @Expose
    private String edong;
    @SerializedName("billingType")
    @Expose
    private String billingType;
    @SerializedName("billingBy")
    @Expose
    private String billingBy;

    @SerializedName("electricityMeter")
    @Expose
    private String electricityMeter;
    @SerializedName("phoneByevn")
    @Expose
    private String phoneByevn;
    @SerializedName("phoneByecp")
    @Expose
    private String phoneByecp;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cardNo")
    @Expose
    private String cardNo;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("requestDate")
    @Expose
    private String requestDate;


    public String getBillingBy() {
        return billingBy;
    }

    public void setBillingBy(String billingBy) {
        this.billingBy = billingBy;
    }

    public String getElectricityMeter() {
        return electricityMeter;
    }

    public void setElectricityMeter(String electricityMeter) {
        this.electricityMeter = electricityMeter;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getHandOverCode() {
        return handOverCode;
    }

    public void setHandOverCode(String handOverCode) {
        this.handOverCode = handOverCode;
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

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
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

    public String getPriceDetail() {
        return priceDetail;
    }

    public void setPriceDetail(String priceDetail) {
        this.priceDetail = priceDetail;
    }

    public String getNumeDetail() {
        return numeDetail;
    }

    public void setNumeDetail(String numeDetail) {
        this.numeDetail = numeDetail;
    }

    public String getAmountDetail() {
        return amountDetail;
    }

    public void setAmountDetail(String amountDetail) {
        this.amountDetail = amountDetail;
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

    public String getAmountNotTax() {
        return amountNotTax;
    }

    public void setAmountNotTax(String amountNotTax) {
        this.amountNotTax = amountNotTax;
    }

    public String getAmountTax() {
        return amountTax;
    }

    public void setAmountTax(String amountTax) {
        this.amountTax = amountTax;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
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

    public String getEdong() {
        return edong;
    }

    public void setEdong(String edong) {
        this.edong = edong;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }
}
