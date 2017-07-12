package views.ecpay.com.postabletecpay.model;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityDanhSachThu;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonNo;
import views.ecpay.com.postabletecpay.util.entities.EntityHoaDonThu;
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.EntityLichSuThanhToan;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline.BillingOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BillInsideCustomer;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.CustomerInsideBody;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;

import static views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection.ERROR_OCCUR;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayModel extends CommonModel {
//    static List<PayAdapter.DataAdapter> mDataCustomerBill;


    List<PayAdapter.BillEntityAdapter> mListBillSelected;

    public PayModel(Context context) {
        super(context);

//        instance = this;
//        mDataCustomerBill = this.refreshDataPayAdapter();
        mListBillSelected = new ArrayList<>();
    }

//    public List<PayAdapter.DataAdapter> getDataCustomerBill() {
//        return mDataCustomerBill;
//    }
//
//    public void setDataCustomerBill(List<PayAdapter.DataAdapter> mDataCustomerBill) {
//        this.mDataCustomerBill = mDataCustomerBill;
//    }

    public List<PayAdapter.BillEntityAdapter> getListBillSelected() {
        return mListBillSelected;
    }

    public void setListBillSelected(List<PayAdapter.BillEntityAdapter> mListBillSelected) {
        this.mListBillSelected = mListBillSelected;
    }

    public List<PayAdapter.DataAdapter> getInforRowCustomerFitterBy(Common.TYPE_SEARCH typeSearch, String infoSearch) {
//        List<PayAdapter.DataAdapter> result = new ArrayList<>();
//        for (int i = 0; i < mDataCustomerBill.size(); i ++)
//        {
//            switch (typeSearch.getPosition()) {
//                case 0:
//                    return  mDataCustomerBill;
//                case 1:
//                    if(mDataCustomerBill.get(i).getInfoKH().getMA_KHANG().toLowerCase().contains(infoSearch.toLowerCase()))
//                    {
//                        result.add(mDataCustomerBill.get(i));
//                    }
//                    break;
//                case 2:
//                    if(mDataCustomerBill.get(i).getInfoKH().getTEN_KHANG().toLowerCase().contains(infoSearch.toLowerCase()))
//                    {
//                        result.add(mDataCustomerBill.get(i));
//                    }
//                    break;
//                case 3:
//                    if(mDataCustomerBill.get(i).getInfoKH().getSDT_EVN().toLowerCase().contains(infoSearch.toLowerCase()))
//                    {
//                        result.add(mDataCustomerBill.get(i));
//                    }
//                    break;
//                case 4:
//                    if(mDataCustomerBill.get(i).getInfoKH().getDIA_CHI().toLowerCase().contains(infoSearch.toLowerCase()))
//                    {
//                        result.add(mDataCustomerBill.get(i));
//                    }
//                    break;
//                case 5:
//                    if(mDataCustomerBill.get(i).getInfoKH().getSO_GCS().toLowerCase().contains(infoSearch.toLowerCase()))
//                    {
//                        result.add(mDataCustomerBill.get(i));
//                    }
//                    break;
//                case 6:
//                    if(mDataCustomerBill.get(i).getInfoKH().getLO_TRINH().toLowerCase().contains(infoSearch.toLowerCase()))
//                    {
//                        result.add(mDataCustomerBill.get(i));
//                    }
//                    break;
//
//            }
//        }
//        return result;

        List<PayAdapter.DataAdapter> mDataPayAdapter = new ArrayList<>();

        //get List Customer
        List<EntityKhachHang> listCustomer = sqLiteConnection.selectAllCustomerFitterBy(MainActivity.mEdong, typeSearch, infoSearch);

        for(int index = 0; index<listCustomer.size();index++)
        {
            EntityKhachHang customer = listCustomer.get(index);
            String code = customer.getMA_KHANG();
            Pair<List<PayAdapter.BillEntityAdapter>, Long> listBill = selectInfoBillOfCustomerToRecycler(MainActivity.mEdong, code);
            Collections.sort(listBill.first, PayAdapter.BillEntityAdapter.TermComparatorBillEntityAdapter);
            PayAdapter.DataAdapter dataAdapter = new PayAdapter.DataAdapter(customer, listBill.first, listBill.second);
            mDataPayAdapter.add(dataAdapter);
        }
        return  mDataPayAdapter;
    }

    public List<PayAdapter.DataAdapter> refreshDataPayAdapter() {

        List<PayAdapter.DataAdapter> mDataPayAdapter = new ArrayList<>();

        //get List Customer
        List<EntityKhachHang> listCustomer = selectAllCustomer(MainActivity.mEdong);

        for(int index = 0; index<listCustomer.size();index++)
        {
            EntityKhachHang customer = listCustomer.get(index);
            String code = customer.getMA_KHANG();
            Pair<List<PayAdapter.BillEntityAdapter>, Long> listBill = selectInfoBillOfCustomerToRecycler(MainActivity.mEdong, code);
            for(int i = 0, n = listBill.first.size(); i < n; i ++)
            {
                listBill.first.get(i).setTEN_KHACH_HANG(customer.getTEN_KHANG());
            }
            Collections.sort(listBill.first, PayAdapter.BillEntityAdapter.TermComparatorBillEntityAdapter);
            PayAdapter.DataAdapter dataAdapter = new PayAdapter.DataAdapter(customer, listBill.first, listBill.second);
            mDataPayAdapter.add(dataAdapter);
        }
        return  mDataPayAdapter;
    }

    public List<EntityKhachHang> selectAllCustomer(String edong) {
        return sqLiteConnection.selectAllCustomerFitterBy(edong, Common.TYPE_SEARCH.ALL, Common.TEXT_EMPTY);
    }



    public Pair<List<PayAdapter.BillEntityAdapter>, Long> selectInfoBillOfCustomerToRecycler(String edong, String code) {
        return sqLiteConnection.selectInfoBillOfCustomerToRecycler(edong, code);
    }

    public void writeSQLiteCustomerTableFromSearchOnline(String edong, EntityKhachHang customerResponse, List<BillInsideCustomer> lst) {
        sqLiteConnection.insertOrUpdateCustomerFromSearchOnline( customerResponse);
        for (int i = 0, n = lst.size(); i < n; i ++)
        {
            BillInsideCustomer bill = lst.get(i);
            if(bill.getCardNo() == null || bill.getCardNo().length() == 0)
            {
                bill.setCardNo(customerResponse.getMA_THE());
            }
            sqLiteConnection.insertOrUpdateBillSearchOnline(edong, bill);
        }
    }

    public void updateBillIsChecked(String edong, String code, int billId, boolean checked) {

        sqLiteConnection.updateBillOfCustomerIsChecked(edong, code, billId, checked);

    }

    public int countMoneyAllBillsIsChecked(String edong) {
        if (edong == null || edong.trim().isEmpty())
            return ERROR_OCCUR;

        return 0;//sqLiteConnection.countMoneyAllBillIsChecked(edong);
    }

    public int countAllBillsIsChecked(String edong) {
        if (edong == null || edong.trim().isEmpty())
            return ERROR_OCCUR;

        return 0;//sqLiteConnection.countAllBillsIsChecked(edong);
    }

    public int countAllBillsIsCheckedWithStatusPay(String edong, Common.STATUS_BILLING statusBilling) {
        if (edong == null || edong.trim().isEmpty())
            return ERROR_OCCUR;

        return 0;//sqLiteConnection.countAllBillsIsCheckedWithStatusPay(edong, statusBilling);

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

        return sqLiteConnection.selectTraceNumberBill(edong, code, billId);
    }

    public List<String> getPcCodes() {
        return sqLiteConnection.getPcCodes();
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

    public void insertHoaDonThu(EntityHoaDonThu bill)
    {
        sqLiteConnection.insertHoaDonThu(bill);
    }
    public void insertLichSuThanhToan(EntityLichSuThanhToan bill)
    {
        sqLiteConnection.insertLichSuThanhToan(bill);
    }


    public int insertDebtCollection(EntityDanhSachThu entityDanhSachThu) {
        return sqLiteConnection.insertDebtCollection(entityDanhSachThu);
    }

    public EntityDanhSachThu selectDebtColectionForDanhSachThu(int billId) {
        return sqLiteConnection.selectDebtColectionForDanhSachThu(billId);
    }

    public EntityLichSuThanhToan selectDebtColectionForLichSu(int billId) {
        return sqLiteConnection.selectDebtColectionForLichSu(billId);
    }

    public int insertPayLib(EntityLichSuThanhToan entityLichSuThanhToan) {
        return sqLiteConnection.insertPayLib(entityLichSuThanhToan);
    }

    public boolean checkIsHasBillNotPayTermBefore(String edong, String code, int billId) {
        boolean failInput =
                TextUtils.isEmpty(edong) ||
                        TextUtils.isEmpty(code);

        if (failInput)
            return false;

        //lấy term của hóa đơn
        String term = sqLiteConnection.getTermBillOfCustomer(edong, code, billId);
        if (term == null)
            return false;

        return sqLiteConnection.checkIsHasBillNotPayTermBefore(edong, code, term);
    }

    public String getCustomerNameByBillId(String edong, long billId) {
        return sqLiteConnection.getCustomerNameByBillId(edong, billId);
    }

    public String getCustomerCodeByBillId(String edong, long billId) {
        return sqLiteConnection.getCustomerCodeByBillId(edong, billId);
    }

    public int updateBillWith(String edongKey, int billId, int status, String edong) {
        return sqLiteConnection.updateBillWith(edongKey, billId, status, edong);
    }

    public int updateBillDebtWith(
            String edongKey, int billId, String edong, Integer paymentMode, int payStatus, Integer stateOfDebt,
            Integer stateIfCancel, Integer stateOfReturn, Integer suspectedProcessingStatus,
            Integer stateOfPush, String dateOfPush, int countPrintReceipt, Integer statusOfPrintInfo) {

        return sqLiteConnection.updateBillDebtWith(
                edongKey, billId, //where
                edong, paymentMode, payStatus,
                stateOfDebt, stateIfCancel, stateOfReturn,
                suspectedProcessingStatus, stateOfPush, dateOfPush,
                countPrintReceipt, statusOfPrintInfo);
    }

    public int updateBillDebtWithThanhToanBoiNguonKhacOrViKhac(
            String edongKey, int billId, String edong, Integer paymentMode, int payStatus, Integer stateOfDebt,
            Integer stateOfCancel, Integer stateOfReturn, Integer suspectedProcessingStatus,
            String dateOfPush, int countPrintReceipt, Integer statusOfPrintInfo, Integer tradeCode) {

        return sqLiteConnection.updateBillDebtWithThanhToanBoiNguonKhac(
                edongKey, billId, //where
                edong, paymentMode, payStatus,
                stateOfDebt, stateOfCancel, stateOfReturn,
                suspectedProcessingStatus, dateOfPush,
                countPrintReceipt, statusOfPrintInfo, tradeCode);
    }

    public int updateBillHistoryWith(String edongKey, int billId, String dateIncurred, Integer tradingCode) {
        return sqLiteConnection.updateBillHistoryWith(edongKey, billId, dateIncurred, tradingCode);
    }

    public int checkPayStatusDebt(String edong, String code, int billId) {
        return sqLiteConnection.selectPayStatusDebt(edong, code, billId);
    }

    public int updateBillWithWithThanhToanError(String edongKey, int billId, String edong) {
        return sqLiteConnection.updateBillWithWithThanhToanError(edongKey, billId, edong);
    }

    public int updateBillDebtWithThanhToanErrorOrSuccess(
            String edongKey, int billId, //where
            String edong, Integer paymentMode, int payStatus, Integer stateOfDebt, Integer stateOfCancel,
            Integer stateOfReturn, Integer suspectedProcessingStatus, int countPrintReceipt, Integer statusOfPrintInfo) {
        return sqLiteConnection.updateBillDebtWithThanhToanErrorOrSuccess(edongKey, billId,
                edong, paymentMode, payStatus, stateOfDebt, stateOfCancel,
                stateOfReturn, suspectedProcessingStatus, countPrintReceipt, statusOfPrintInfo);
    }

    public int updateBillHistoryWithThanhToanErrorOrSuccess(
            String edongKey, int billId, //where
            String edong, Integer paymentMode, int payStatus, Integer stateOfDebt, Integer stateOfCancel,
            Integer stateOfReturn, Integer suspectedProcessingStatus, int countPrintReceipt, Integer statusOfPrintInfo,
            String dateIncurred, Integer tradingCode) {
        return sqLiteConnection.updateBillHistoryWithThanhToanErrorOrSuccess(edongKey, billId,
                edong, paymentMode, payStatus, stateOfDebt, stateOfCancel,
                stateOfReturn, suspectedProcessingStatus, countPrintReceipt, statusOfPrintInfo, dateIncurred, tradingCode);
    }

    public int updateBillHistoryWithPrintInfo(String edongKey, int billId, Integer code) {
        return sqLiteConnection.updateBillHistoryWithPrintInfo(edongKey, billId, code);
    }

    public int getNotYetPushMoney(String edongKey) {
        return sqLiteConnection.selectNotYetPushMoney(edongKey);
    }

    public void updateSoDuKhaDung(String edong, long balance, long lockMoney)
    {
        sqLiteConnection.updateBalance(edong, balance, lockMoney);
    }


    public int updateAccountWith(String edong, int notYetPushMoney) {
        return sqLiteConnection.updateAccountWith(edong, notYetPushMoney);
    }

    public int updateBillDebtWithSuspectedProcessingStatus(String edong, int billId, Integer suspectedProcessingStatus) {
        return sqLiteConnection.updateBillDebtWithSuspectedProcessingStatus(edong, billId, suspectedProcessingStatus);
    }

    public int updateBillHistoryWithSuspectedProcessingStatus(String edong, int billId, Integer suspectedProcessingStatus) {
        return sqLiteConnection.updateBillHistoryWithSuspectedProcessingStatus(edong, billId, suspectedProcessingStatus);
    }


    public static class AsyncSearchOffline extends AsyncTask<Pair<Common.TYPE_SEARCH, String>, String, Void> {

        private AsyncSoapCallBack callBack;
        private PayModel payModel;
        public AsyncSearchOffline(PayModel payModel, AsyncSoapCallBack callBack) throws Exception {
            this.callBack = callBack;
            this.payModel = payModel;
        }

        @Override
        protected Void doInBackground(Pair<Common.TYPE_SEARCH, String>... strings) {
            if(callBack != null)
            {
                callBack.onPost(this.payModel.getInforRowCustomerFitterBy(strings[0].first, strings[0].second));
            }
            return null;
        }


    }


    public interface AsyncSoapCallBack{
        public void onPost(List<PayAdapter.DataAdapter> result);

    }
}
