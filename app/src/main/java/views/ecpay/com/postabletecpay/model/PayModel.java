package views.ecpay.com.postabletecpay.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayModel extends CommonModel {
    public PayModel(Context context) {
        super(context);
    }

    public List<PayAdapter.PayEntityAdapter> getAllBill(String mEdong) {
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

//        return sqLiteConnection.selectAllBill(mEdong);
    }

    public List<PayAdapter.PayEntityAdapter> getAllBillFitterBy(String mEdong, Common.TYPE_SEARCH typeSearch, String infoSearch) {
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
}
