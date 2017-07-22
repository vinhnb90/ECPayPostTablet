package views.ecpay.com.postabletecpay.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonNo;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonThu;
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.EntityLichSuThanhToan;
import views.ecpay.com.postabletecpay.util.entities.request.EntityPostBill.TransactionOffItem;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBill.BillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCustomer.CustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListBookCmisResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityEVN.ListEvnPCResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListBillResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.ListCustomerResponse;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
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


    public String getSession(String edong) {
        if (TextUtils.isEmpty(edong))
            return null;

        return sqLiteConnection.selectSessionAccount(edong);
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

    public List<PayAdapter.BillEntityAdapter> selectOfflineBill() {
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



    public long updateHoaDonNo(long billID, String status, String edong) {
        return sqLiteConnection.updateHoaDonNo(billID, status, edong);
    }
    public long updateHoaDonNo(long billID, String edong) {
        return sqLiteConnection.updateHoaDonNo(billID, edong);
    }

    public EntityHoaDonNo getHoaDonNo(long billID)
    {
        return sqLiteConnection.getHoaDonNo(billID);
    }

    public EntityHoaDonThu getHoaDonThu(long billID)
    {
        return sqLiteConnection.getHoaDonThu(billID);
    }

    public void insertHoaDonThu(EntityHoaDonThu bill)
    {
        sqLiteConnection.insertHoaDonThu(bill);
    }
    public void insertLichSuThanhToan(EntityLichSuThanhToan bill)
    {
        sqLiteConnection.insertLichSuThanhToan(bill);
    }



    public void updateSoDuKhaDung(String edong, long balance, long lockMoney)
    {
        sqLiteConnection.updateBalance(edong, balance, lockMoney);
    }

    public void truTienTrongVi(String edong, long amount)
    {
        Account account = getAccountInfo(edong);
        if(account != null)
        {
            sqLiteConnection.updateBalance(edong, account.getBalance() - amount, account.getLockMoney());
        }
    }

    public void updateHoaDonThu(String MA_HOA_DON, String VI_TTOAN, Common.TRANG_THAI_TTOAN TRANG_THAI_TTOAN,
                                String TRANG_THAI_CHAM_NO, String TRANG_THAI_DAY_CHAM_NO, String NGAY_DAY, String TRANG_THAI_HOAN_TRA)
    {
        sqLiteConnection.updateHoaDonThu( MA_HOA_DON,  VI_TTOAN, TRANG_THAI_TTOAN,
                 TRANG_THAI_CHAM_NO,  TRANG_THAI_DAY_CHAM_NO,  NGAY_DAY,  TRANG_THAI_HOAN_TRA);
    }

    public Cursor getCursorEvnPc(String edong) {
        return sqLiteConnection.getCursorEvnPc(edong);
    }
    //endregion
}

