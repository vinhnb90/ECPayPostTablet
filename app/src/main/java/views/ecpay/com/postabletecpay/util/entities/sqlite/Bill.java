package views.ecpay.com.postabletecpay.util.entities.sqlite;

/**
 * Created by VinhNB on 5/22/2017.
 */

public class Bill {
    private String customerCode;
    private String customerPayCode;
    private String billId;
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
    private String tax;
    private String billNum;
    private String currency;
    private String priceDetails;
    private String numeDetails;
    private String amountDetails;
    private String oldIndex;
    private String newIndex;
    private String nume;
    private int amountNotTax;
    private int amountTax;
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

    public Bill(String customerCode, String customerPayCode, String billId, String term, int amount, String period, String issueDate, String strIssueDate, int status, String seri, String pcCode, String handoverCode, String cashierCode, String bookCmis, String fromDate, String toDate, String strFromDate, String strToDate, String home, String tax, String billNum, String currency, String priceDetails, String numeDetails, String amountDetails, String oldIndex, String newIndex, String nume, int amountNotTax, int amountTax, String multiple, String billType, String typeIndex, String groupTypeIndex, String createdDate, int idChanged, String dateChanged, String edong, String pcCodeExt, String code, String name, String nameNosign, String phoneByevn, String phoneByecp, String electricityMeter, String inning, String road, String station, String taxCode, String trade, String countPeriod, String team, int type, String lastQuery, int groupType, String billingChannel, String billingType, String billingBy, String cashierPay, String edongKey) {
        this.customerCode = customerCode;
        this.customerPayCode = customerPayCode;
        this.billId = billId;
        this.term = term;
        this.amount = amount;
        this.period = period;
        this.issueDate = issueDate;
        this.strIssueDate = strIssueDate;
        this.status = status;
        this.seri = seri;
        this.pcCode = pcCode;
        this.handoverCode = handoverCode;
        this.cashierCode = cashierCode;
        this.bookCmis = bookCmis;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.strFromDate = strFromDate;
        this.strToDate = strToDate;
        this.home = home;
        this.tax = tax;
        this.billNum = billNum;
        this.currency = currency;
        this.priceDetails = priceDetails;
        this.numeDetails = numeDetails;
        this.amountDetails = amountDetails;
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
        this.nume = nume;
        this.amountNotTax = amountNotTax;
        this.amountTax = amountTax;
        this.multiple = multiple;
        this.billType = billType;
        this.typeIndex = typeIndex;
        this.groupTypeIndex = groupTypeIndex;
        this.createdDate = createdDate;
        this.idChanged = idChanged;
        this.dateChanged = dateChanged;
        this.edong = edong;
        this.pcCodeExt = pcCodeExt;
        this.code = code;
        this.name = name;
        this.nameNosign = nameNosign;
        this.phoneByevn = phoneByevn;
        this.phoneByecp = phoneByecp;
        this.electricityMeter = electricityMeter;
        this.inning = inning;
        this.road = road;
        this.station = station;
        this.taxCode = taxCode;
        this.trade = trade;
        this.countPeriod = countPeriod;
        this.team = team;
        this.type = type;
        this.lastQuery = lastQuery;
        this.groupType = groupType;
        this.billingChannel = billingChannel;
        this.billingType = billingType;
        this.billingBy = billingBy;
        this.cashierPay = cashierPay;
        this.edongKey = edongKey;
    }

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

    public int getAmountTax() {
        return amountTax;
    }

    public void setAmountTax(int amountTax) {
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
}
