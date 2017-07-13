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
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
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


    public void removeBillSelect(long billId)
    {
        for (int i = 0, n = mListBillSelected.size(); i < n; i ++)
        {
            if(mListBillSelected.get(i).getBillId() == billId)
            {
                mListBillSelected.remove(i);
                return;
            }
        }
    }

    public boolean containBillInSelected(long billId) {
        for (int i = 0, n = mListBillSelected.size(); i < n; i++) {
            if (mListBillSelected.get(i).getBillId() == billId) {
                return true;
            }
        }
        return false;
    }

    public void selectBill(PayAdapter.BillEntityAdapter bill, boolean isSelect)
    {
        if(isSelect)
        {
            for (int i = 0, n = mListBillSelected.size(); i < n; i ++)
            {
                if(mListBillSelected.get(i).getBillId() == bill.getBillId())
                {
                    return;
                }
            }
            mListBillSelected.add(bill);
        }else
        {
            for (int i = 0, n = mListBillSelected.size(); i < n; i ++)
            {
                if(mListBillSelected.get(i).getBillId() == bill.getBillId())
                {
                    mListBillSelected.remove(i);
                    return;
                }
            }
        }
    }


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



    public String getSession(String edong) {
        if (TextUtils.isEmpty(edong))
            return null;

        return sqLiteConnection.selectSessionAccount(edong);
    }


    public List<String> getPcCodes() {
        return sqLiteConnection.getPcCodes();
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

    public void updateBillHuy(Long billId, Common.TRANG_THAI_HUY TRANG_THAI_HUY, String lyDo)
    {
        sqLiteConnection.updateHoaDonThu(String.valueOf(billId), TRANG_THAI_HUY, lyDo);
        sqLiteConnection.insertLichSuThanhToan(String.valueOf(billId), TRANG_THAI_HUY, Common.MA_GIAO_DICH.GUI_YEU_CAU_HUY.getCode(), lyDo);
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
