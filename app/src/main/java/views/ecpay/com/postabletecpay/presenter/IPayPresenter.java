package views.ecpay.com.postabletecpay.presenter;

import android.support.v7.widget.RecyclerView;

import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;

/**
 * Created by VinhNB on 5/26/2017.
 */

public interface IPayPresenter {
    void callPayRecycler(String mEdong, int pageIndex, Common.TYPE_SEARCH typeSearch, String infoSearch, boolean isSeachOnline);

    void callSearchOnline(String mEdong, String infoSearch, boolean isReseach);

    void cancelSeachOnline();

    void reseachOnline(String edong);

    void callProcessDataBillChecked(String edong, String code, PayAdapter.BillEntityAdapter bill);
}
