package views.ecpay.com.postabletecpay.model;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayBillsDialogAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BillInsideCustomer;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.CustomerInsideBody;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;

import static views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection.ERROR_OCCUR;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayModel extends CommonModel {
    public PayModel(Context context) {
        super(context);
    }

    /* public List<PayAdapter.PayEntityAdapter> getAllBill(String mEdong) {
         if (mEdong == null)
             return null;
         PayAdapter.PayEntityAdapter a1 = new PayAdapter.PayEntityAdapter("billID 1", "tenKH 1", "diaChi1", "loTrinh 1", "ma KH 1", 100000, 1);
         PayAdapter.PayEntityAdapter a2 = new PayAdapter.PayEntityAdapter("billID 2", "tenKH 2", "diaChi1", "loTrinh 2", "ma KH 2", 100000, 2);
         PayAdapter.PayEntityAdapter a3 = new PayAdapter.PayEntityAdapter("billID 3", "tenKH 3", "diaChi1", "loTrinh 3", "ma KH 3", 100000, 3);
         PayAdapter.PayEntityAdapter a4 = new PayAdapter.PayEntityAdapter("billID 4", "tenKH 4", "diaChi1", "loTrinh 4", "ma KH 4", 100000, 4);
         PayAdapter.PayEntityAdapter a5 = new PayAdapter.PayEntityAdapter("billID 1", "tenKH 1", "diaChi1", "loTrinh 1", "ma KH 1", 100000, 1);
         PayAdapter.PayEntityAdapter a6 = new PayAdapter.PayEntityAdapter("billID 2", "tenKH 2", "diaChi1", "loTrinh 2", "ma KH 2", 100000, 2);
         PayAdapter.PayEntityAdapter a7 = new PayAdapter.PayEntityAdapter("billID 3", "tenKH 3", "diaChi1", "loTrinh 3", "ma KH 3", 100000, 3);
         PayAdapter.PayEntityAdapter a8 = new PayAdapter.PayEntityAdapter("billID 4", "tenKH 4", "diaChi1", "loTrinh 4", "ma KH 4", 100000, 4);
         PayAdapter.PayEntityAdapter a9 = new PayAdapter.PayEntityAdapter("billID 3", "tenKH 3", "diaChi1", "loTrinh 3", "ma KH 3", 100000, 3);
         PayAdapter.PayEntityAdapter a10 = new PayAdapter.PayEntityAdapter("billID 4", "tenKH 4", "diaChi1", "loTrinh 4", "ma KH 4", 100000, 4);

         List<PayAdapter.PayEntityAdapter> v = new ArrayList<>();
         v.add(a1);
         v.add(a2);
         v.add(a3);
         v.add(a4);
         v.add(a5);
         v.add(a6);
         v.add(a7);
         v.add(a8);
         v.add(a9);
         v.add(a10);

         return v;

         return null;
 //        return sqLiteConnection.selectAllBill(mEdong);
     }
 */
    /* public List<PayAdapter.PayEntityAdapter> getAllBillFitterBy(String mEdong, Common.TYPE_SEARCH typeSearch, String infoSearch) {
         if (mEdong == null)
             return null;
         PayAdapter.PayEntityAdapter a1 = new PayAdapter.PayEntityAdapter("billID 12", "tenKH 123", "diaChi1", "loTrinh 1", "ma KH 1", 100000, 1);
         PayAdapter.PayEntityAdapter a2 = new PayAdapter.PayEntityAdapter("billID 23", "tenKH 2321", "diaChi1", "loTrinh 2", "ma KH 2", 100000, 2);
         PayAdapter.PayEntityAdapter a3 = new PayAdapter.PayEntityAdapter("billID 33", "tenKH 33213", "diaChi1", "loTrinh 3", "ma KH 3", 100000, 3);
         PayAdapter.PayEntityAdapter a4 = new PayAdapter.PayEntityAdapter("billID 44", "tenKH 4321", "diaChi1", "loTrinh 4", "ma KH 4", 100000, 4);
         PayAdapter.PayEntityAdapter a5 = new PayAdapter.PayEntityAdapter("billID 122", "tenKH 123a", "diaChi1", "loTrinh 1", "ma KH 1", 100000, 1);
         PayAdapter.PayEntityAdapter a6 = new PayAdapter.PayEntityAdapter("billID 232", "tenKH 2a321", "diaChi1", "loTrinh 2", "ma KH 2", 100000, 2);
         PayAdapter.PayEntityAdapter a7 = new PayAdapter.PayEntityAdapter("billID 332", "tenKH 3a3", "diaChi1", "loTrinh 3", "ma KH 3", 100000, 3);
         PayAdapter.PayEntityAdapter a8 = new PayAdapter.PayEntityAdapter("billID 442", "tenKH 4â", "diaChi1", "loTrinh 4", "ma KH 4", 100000, 4);
         List<PayAdapter.PayEntityAdapter> v = new ArrayList<>();
         v.add(a1);
         v.add(a2);
         v.add(a3);
         v.add(a4);
         v.add(a5);
         v.add(a6);
         v.add(a7);
         v.add(a8);
         return v;
 //        return sqLiteConnection.selectAllBillBy(mEdong, typeSearch, infoSearch);
     }
 */
    public List<PayAdapter.PayEntityAdapter> getAllCustomer(String mEdong) {
        List<PayAdapter.PayEntityAdapter> listPay = new ArrayList<>();

        //get List Customer
        List<Customer> listCustomer = new ArrayList<>();
        listCustomer = sqLiteConnection.selectAllCustomer(mEdong);

        //with every one
        int index = 0;
        int maxIndex = listCustomer.size();
        for (; index < maxIndex; index++) {
            Customer customer = listCustomer.get(index);

            PayAdapter.PayEntityAdapter pay = new PayAdapter.PayEntityAdapter();
            pay.setEdong(mEdong);
            pay.setTenKH(customer.getName());
            pay.setDiaChi(customer.getAddress());
            pay.setMaKH(customer.getCode());

            //get loTrinh
            String road = sqLiteConnection.selectRoadFirstInBill(mEdong, customer.getCode());
            pay.setLoTrinh(road);

            //get totalMoney
            long totalMoney = sqLiteConnection.countMoneyAllBillOfCustomer(mEdong, customer.getCode());
            pay.setTongTien(totalMoney);

            //check status pay
            boolean isPayed = sqLiteConnection.checkStatusPayedOfCustormer(mEdong, customer.getCode());

            listPay.add(pay);
        }
        return listPay;
    }

    public List<PayAdapter.PayEntityAdapter> getInforRowCustomer(String edong) {
        List<PayAdapter.PayEntityAdapter> listPay = new ArrayList<>();

        //get List Customer
        List<Customer> listCustomer = new ArrayList<>();
        listCustomer = sqLiteConnection.selectAllCustomer(edong);

        //with every one
        int index = 0;
        int maxIndex = listCustomer.size();
        for (; index < maxIndex; index++) {
            Customer customer = listCustomer.get(index);

            PayAdapter.PayEntityAdapter pay = new PayAdapter.PayEntityAdapter();
            pay.setEdong(edong);
            pay.setTenKH(customer.getName());
            pay.setDiaChi(customer.getAddress());
            //get loTrinh
            String road = sqLiteConnection.selectRoadFirstInBill(edong, customer.getCode());
            pay.setLoTrinh(road);
            pay.setMaKH(customer.getCode());
            //get totalMoney
            long totalMoney = sqLiteConnection.countMoneyAllBillOfCustomer(edong, customer.getCode());
            pay.setTongTien(totalMoney);
            //check status pay
            boolean isPayed = sqLiteConnection.checkStatusPayedOfCustormer(edong, customer.getCode());
            pay.setPayed(isPayed);
            pay.setShowBill(customer.isShowBill());

            listPay.add(pay);
        }
        return listPay;
    }

    public List<PayAdapter.PayEntityAdapter> getInforRowCustomerFitterBy(String mEdong, Common.TYPE_SEARCH typeSearch, String infoSearch) {
        List<PayAdapter.PayEntityAdapter> listPay = new ArrayList<>();

        //get List Customer
        List<Customer> listCustomer = new ArrayList<>();
        listCustomer = sqLiteConnection.selectAllCustomerFitterBy(mEdong, typeSearch, infoSearch);

        //with every one
        int index = 0;
        int maxIndex = listCustomer.size();
        for (; index < maxIndex; index++) {
            Customer customer = listCustomer.get(index);

            PayAdapter.PayEntityAdapter pay = new PayAdapter.PayEntityAdapter();
            pay.setEdong(mEdong);
            pay.setTenKH(customer.getName());
            pay.setDiaChi(customer.getAddress());
            pay.setMaKH(customer.getCode());

            //get loTrinh
            String road = sqLiteConnection.selectRoadFirstInBill(mEdong, customer.getCode());
            pay.setLoTrinh(road);

            //get totalMoney
            long totalMoney = sqLiteConnection.countMoneyAllBillOfCustomer(mEdong, customer.getCode());
            pay.setTongTien(totalMoney);

            //check status pay
            //true = payed
            boolean isPayed = sqLiteConnection.checkStatusPayedOfCustormer(mEdong, customer.getCode());
            pay.setPayed(isPayed);
            listPay.add(pay);
        }

        return listPay;

    }

    public List<PayAdapter.BillEntityAdapter> getAllBillOfCustomer(String edong, String code) {
        return sqLiteConnection.selectInfoBillOfCustomerToRecycler(edong, code);
    }

    public int writeSQLiteCustomerTableFromSearchOnline(String edong, CustomerInsideBody customerResponse) {
        if (edong == null || edong.trim().isEmpty() || customerResponse == null)
            return ERROR_OCCUR;

        return sqLiteConnection.insertNotUpdateCustomerFromSearchOnline(edong, customerResponse);
    }

    public int writeSQliteBillTableOfCustomer(String edong, BillInsideCustomer billInsideCustomer) {
        if (edong == null || edong.trim().isEmpty() || billInsideCustomer == null)
            return ERROR_OCCUR;

        return sqLiteConnection.insertNotUpdateBillOfCustomer(edong, billInsideCustomer);
    }

    public void updateBillIsChecked(String edong, String code, int billId, boolean checked) {

        sqLiteConnection.updateBillOfCustomerIsChecked(edong, code, billId, checked);

    }

    public int countMoneyAllBillsIsChecked(String edong) {
        if (edong == null || edong.trim().isEmpty())
            return ERROR_OCCUR;

        return sqLiteConnection.countMoneyAllBillIsChecked(edong);
    }

    public int countAllBillsIsChecked(String edong) {
        if (edong == null || edong.trim().isEmpty())
            return ERROR_OCCUR;

        return sqLiteConnection.countAllBillsIsChecked(edong);
    }

    public int countAllBillsIsCheckedWithStatusPay(String edong, Common.STATUS_BILLING statusBilling) {
        if (edong == null || edong.trim().isEmpty())
            return ERROR_OCCUR;

        return sqLiteConnection.countAllBillsIsCheckedWithStatusPay(edong, statusBilling);

    }

    public List<PayBillsDialogAdapter.Entity> getAllBillOfCustomerChecked(String edong) {
        if (edong == null || edong.trim().isEmpty())
            return null;

        return sqLiteConnection.selectAllBillsOfAllCustomerChecked(edong);
    }

    public List<PayBillsDialogAdapter.Entity> getAllBillOfCustomerCheckedWithStatusPay(String edong, Common.STATUS_BILLING statusBilling) {
        if (edong == null || edong.trim().isEmpty())
            return null;

        return sqLiteConnection.selectAllBillsOfAllCustomerCheckedWithStatus(edong, statusBilling);
    }


    public void updateCustomerIsShowBill(String edong, String code, boolean checked) {
        sqLiteConnection.updateCustomerIsShowBill(edong, code, checked);
    }

    public String getSession(String edong) {
        if (TextUtils.isEmpty(edong))
            return null;

        return sqLiteConnection.selectSessionAccount(edong);
    }

    public int updateBillStatus(String edong, String customerCode, Long billId, Common.STATUS_BILLING status) {
        boolean failInput =
                TextUtils.isEmpty(edong) ||
                        TextUtils.isEmpty(customerCode);
        if (failInput)
            return ERROR_OCCUR;

        return sqLiteConnection.updateBillStatusPay(edong, customerCode, billId, status);
    }

    public long countMoneyAllBillsIsCheckedWithStatusPay(String edong, Common.STATUS_BILLING statusBilling) {
        if (edong == null || edong.trim().isEmpty())
            return ERROR_OCCUR;

        return sqLiteConnection.countMoneyAllBillIsCheckedWithStatusPay(edong, statusBilling);
    }

    public void updateBillRequestDateBill(String edong, String customerCode, Long billId, String dateNow, Long traceNumber) {
        boolean failInput =
                TextUtils.isEmpty(edong) ||
                        TextUtils.isEmpty(customerCode) ||
                        TextUtils.isEmpty(dateNow);
        if (failInput)
            return;

        sqLiteConnection.countMoneyAllBillIsCheckedWithStatusPay(edong, customerCode, billId, dateNow, traceNumber);
    }

    public Long getTraceNumberBill(String edong, String code, Long billId) {
        boolean failInput =
                TextUtils.isEmpty(edong) ||
                        TextUtils.isEmpty(code);

        if (failInput)
            return null;

        return sqLiteConnection.SelectTraceNumberBill(edong, code, billId);
    }

    public String getPcCode() {
        return sqLiteConnection.getPcCode();
    }

    public void updateBillReasonDelete(String edong, String code, Long billId, String reasonDeleteBill, Common.STATUS_BILLING statusBilling) {
        boolean failInput =
                TextUtils.isEmpty(edong) ||
                        TextUtils.isEmpty(code) ||
                        TextUtils.isEmpty(reasonDeleteBill) ||
                        statusBilling == null;

        if (failInput)
            return;

        sqLiteConnection.updateBillReasonDelete(edong, code, billId, reasonDeleteBill, statusBilling);
    }
}
