package views.ecpay.com.postabletecpay.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill.TransactionOffItem;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.CustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEvnPCResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;

import static android.content.Context.MODE_PRIVATE;

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

    public long insertEvnPC(ListEvnPCResponse listEvnPCResponse, String edong) {
        return sqLiteConnection.insertEvnPC(listEvnPCResponse, edong);
    }

    public long deleteAllPC(String edong) {
        return sqLiteConnection.deleteAllPC(edong);
    }

    public long checkEvnPCExist(int pcId, String edong) {
        return sqLiteConnection.checkEvnPCExist(pcId, edong);
    }

    public long insertBookCmis(ListBookCmisResponse listBookCmisResponse) {
        return sqLiteConnection.insertBookCmis(listBookCmisResponse);
    }

    public String getPcCode(String edong) {
        return sqLiteConnection.getPcCode(edong);
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

    public List<TransactionOffItem> selectOfflineBill() {
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

    public long getMaxIdChanged(String edong, String boockCms) {
        return getManagerSharedPref().getSharePref(Common.SHARE_REF_CHANGED_GEN_FILE, MODE_PRIVATE)
                .getLong(Common.SHARE_REF_CHANGED_GEN_FILE_ID_ + edong + "_" + boockCms, 0);
    }

    public String getMaxDateChanged(String edong, String boockCms) {
        return getManagerSharedPref().getSharePref(Common.SHARE_REF_CHANGED_GEN_FILE, MODE_PRIVATE).
                getString(Common.SHARE_REF_CHANGED_GEN_FILE_DATE + edong + "_" + boockCms, "");
    }

    public void setChangedGenFile(String edong, String boockCms, Long idChanged, String dateChange) {
        sharePrefManager.getSharePref(Common.SHARE_REF_CHANGED_GEN_FILE, MODE_PRIVATE).edit().
                putLong(Common.SHARE_REF_CHANGED_GEN_FILE_ID_ + edong + "_" + boockCms, idChanged)
                .putString(Common.SHARE_REF_CHANGED_GEN_FILE_DATE + edong + "_" + boockCms, dateChange).commit();
    }

    //endregion
}

