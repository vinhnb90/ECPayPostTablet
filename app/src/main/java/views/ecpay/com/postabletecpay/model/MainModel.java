package views.ecpay.com.postabletecpay.model;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.CustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEvnPCResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;

/**
 * Created by VinhNB on 5/23/2017.
 */

public class MainModel extends CommonModel {

    public MainModel(Context context) {
        super(context);
    }

    //region call SQLite
    public int getTotalBill(String edong) {
        return sqLiteConnection.countBill(edong);
    }

    public int getTotalMoney(String edong) {
        return sqLiteConnection.countMoneyAllBill(edong);
    }

    public long insertEvnPC(ListEvnPCResponse listEvnPCResponse) {
        return sqLiteConnection.insertEvnPC(listEvnPCResponse);
    }

    public long deleteAllPC() {
        return sqLiteConnection.deleteAllPC();
    }

    public long checkEvnPCExist(int pcId) {
        return sqLiteConnection.checkEvnPCExist(pcId);
    }

    public long insertBookCmis(ListBookCmisResponse listBookCmisResponse) {
        return sqLiteConnection.insertBookCmis(listBookCmisResponse);
    }

    public String getPcCode() {
        return sqLiteConnection.getPcCode();
    }

    public long deleteAllBookCmis() {
        return sqLiteConnection.deleteAllBookCmis();
    }

    public long checkBookCmisExist(String bookCmis) {
        return sqLiteConnection.checkBookCmisExist(bookCmis);
    }

    public Cursor getAllBookCmis() {
        return sqLiteConnection.getAllBookCmis();
    }

    public Cursor selectOfflineBill() {
        return sqLiteConnection.selectOfflineBill();
    }

    public long checkCustomerExist(String code) {
        return sqLiteConnection.checkCustomerExist(code);
    }

    public long insertCustomer(ListCustomerResponse listCustomerResponse) {
        return sqLiteConnection.insertCustomer(listCustomerResponse);
    }

    public long insertCustomer(CustomerResponse customerResponse) {
        return sqLiteConnection.insertCustomer(customerResponse);
    }

    public long updateCustomer(CustomerResponse customerResponse) {
        return sqLiteConnection.updateCustomer(customerResponse);
    }

    public long checkBillExist(String billId) {
        return sqLiteConnection.checkBillExist(billId);
    }

    public long insertBill(ListBillResponse listBillResponse) {
        return sqLiteConnection.insertBill(listBillResponse);
    }

    public long insertBill(BillResponse listBillResponse) {
        return sqLiteConnection.insertBill(listBillResponse);
    }

    public long updateBill(BillResponse listBillResponse) {
        return sqLiteConnection.updateBill(listBillResponse);
    }

    public long getMaxIdChanged() {
        return sqLiteConnection.getMaxIdChanged();
    }

    public String getMaxDateChanged() {
        return sqLiteConnection.getMaxDateChanged();
    }

    public List<Customer> selectAllCustomer(String edong) {
        return sqLiteConnection.selectAllCustomerFitterBy(edong, Common.TYPE_SEARCH.ALL, Common.TEXT_EMPTY);
    }

    public String selectRoadFirstInBill(String edong, String code) {
        return sqLiteConnection.selectRoadFirstInBill(edong, code);
    }

    public long countMoneyAllBillOfCustomer(String edong, String code) {
        return  sqLiteConnection.countMoneyAllBillOfCustomer(edong, code);
    }

    public boolean checkStatusPayedOfCustormer(String edong, String code) {
        return sqLiteConnection.checkStatusPayedOfCustormer(edong, code);
    }

    public List<PayAdapter.BillEntityAdapter> selectInfoBillOfCustomerToRecycler(String edong, String code) {
        return sqLiteConnection.selectInfoBillOfCustomerToRecycler(edong, code);
    }
    //endregion
}

