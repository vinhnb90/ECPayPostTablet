package views.ecpay.com.postabletecpay.util.entities;

import views.ecpay.com.postabletecpay.util.commons.Common;

import static views.ecpay.com.postabletecpay.util.commons.Common.DATE_TIME_TYPE.yyyyMMdd;
import static views.ecpay.com.postabletecpay.util.commons.Common.DATE_TIME_TYPE.yyyyMMddHHmmssSSS;

/**
 * Created by tima on 6/21/17.
 */

public class EntityDanhSachThu {
    private String customerCode;
    private String customerPayCode;
    private int billId;
    private String term;
    private int amount;
    private String period;
    private String issueDate;
    private String strIssueDate;
    private int status;
    private String seri;
    private String pcCode;
    private String handoverCode;
    private String cashierCode;
    private String bookCmis;
    private String fromDate;
    private String toDate;
    private String strFromDate;
    private String strToDate;
    private String home;
    private float tax;
    private String billNum;
    private String currency;
    private String priceDetails;
    private String numeDetails;
    private String amountDetails;
    private String oldIndex;
    private String newIndex;
    private String nume;
    private int amountNotTax;
    private String amountTax;
    private String multiple;
    private String billType;
    private String typeIndex;
    private String groupTypeIndex;
    private String createdDate;
    private int idChanged;
    private String dateChanged;
    private String edong;
    private String pcCodeExt;
    private String code;
    private String name;
    private String nameNosign;
    private String phoneByevn;
    private String phoneByecp;
    private String electricityMeter;
    private String inning;
    private String road;
    private String station;
    private String taxCode;
    private String trade;
    private String countPeriod;
    private String team;
    private int type;
    private String lastQuery;
    private int groupType;
    private String billingChannel;
    private String billingType;
    private String billingBy;
    private String cashierPay;
    private String edongKey;
    private String payments;
    private String payStatus;
    private String stateOfDebt;
    private String stateOfCancel;
    private String stateOfReturn;
    private String suspectedProcessingStatus;
    private String stateOfPush;
    private String dateOfPush;
    private String countPrintReceipt;
    private String printInfo;

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

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
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

    public int getAmountNotTax() {
        return amountNotTax;
    }

    public void setAmountNotTax(int amountNotTax) {
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

    public int getIdChanged() {
        return idChanged;
    }

    public void setIdChanged(int idChanged) {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLastQuery() {
        return lastQuery;
    }

    public void setLastQuery(String lastQuery) {
        this.lastQuery = lastQuery;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
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

    public String getEdongKey() {
        return edongKey;
    }

    public void setEdongKey(String edongKey) {
        this.edongKey = edongKey;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getStateOfDebt() {
        return stateOfDebt;
    }

    public void setStateOfDebt(String stateOfDebt) {
        this.stateOfDebt = stateOfDebt;
    }

    public String getStateOfCancel() {
        return stateOfCancel;
    }

    public void setStateOfCancel(String stateOfCancel) {
        this.stateOfCancel = stateOfCancel;
    }

    public String getStateOfReturn() {
        return stateOfReturn;
    }

    public void setStateOfReturn(String stateOfReturn) {
        this.stateOfReturn = stateOfReturn;
    }

    public String getSuspectedProcessingStatus() {
        return suspectedProcessingStatus;
    }

    public void setSuspectedProcessingStatus(String suspectedProcessingStatus) {
        this.suspectedProcessingStatus = suspectedProcessingStatus;
    }

    public String getStateOfPush() {
        return stateOfPush;
    }

    public void setStateOfPush(String stateOfPush) {
        this.stateOfPush = stateOfPush;
    }

    public String getDateOfPush() {
        return dateOfPush;
    }

    public void setDateOfPush(String dateOfPush) {
        this.dateOfPush = dateOfPush;
    }

    public String getCountPrintReceipt() {
        return countPrintReceipt;
    }

    public void setCountPrintReceipt(String countPrintReceipt) {
        this.countPrintReceipt = countPrintReceipt;
    }

    public String getPrintInfo() {
        return printInfo;
    }

    public void setPrintInfo(String printInfo) {
        this.printInfo = printInfo;
    }
}
