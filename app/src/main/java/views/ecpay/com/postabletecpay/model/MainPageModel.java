package views.ecpay.com.postabletecpay.model;

import android.content.Context;
import android.database.Cursor;

import views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.CustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEvnPCResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListCustomerResponse;

/**
 * Created by VinhNB on 5/23/2017.
 */

public class MainPageModel extends CommonModel {

    public MainPageModel(Context context) {
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

    public  long checkEvnPCExist(int pcId) {
        return sqLiteConnection.checkEvnPCExist(pcId);
    }

    public long insertBookCmis(ListBookCmisResponse listBookCmisResponse) {
        return sqLiteConnection.insertBookCmis(listBookCmisResponse);
    }

    public String getPcCode(){
        return sqLiteConnection.getPcCode();
    }

    public long deleteAllBookCmis() {
        return sqLiteConnection.deleteAllBookCmis();
    }

    public  long checkBookCmisExist(String bookCmis) {
        return sqLiteConnection.checkBookCmisExist(bookCmis);
    }

    public Cursor getAllBookCmis() {
        return sqLiteConnection.getAllBookCmis();
    }


    public  long checkCustomerExist(String code) {
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

    public  long checkBillExist(String billId) {
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
    //endregion
}

