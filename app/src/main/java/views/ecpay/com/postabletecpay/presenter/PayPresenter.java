package views.ecpay.com.postabletecpay.presenter;

import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter.PayEntityAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.ThanhToan.IPayView;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;

import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.ROWS_ON_PAGE;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayPresenter implements IPayPresenter {
    private PayModel mPayModel;
    private IPayView mIPayView;
    private List<PayAdapter.PayEntityAdapter> mAdapterList = new ArrayList<>();
    private List<PayAdapter.PayEntityAdapter> mAdapterListSearch = new ArrayList<>();
    private int mPageIndex;

    public PayPresenter(IPayView mIPayView) {
        this.mIPayView = mIPayView;
        mPayModel = new PayModel(mIPayView.getContextView());
    }

    /*@Override
    public void callPayRecycler(String mEdong, int pageIndex) {
        if (mEdong == null)
            return;

        if (pageIndex == PayFragment.FIRST_PAGE_INDEX) {
            mAdapterList.clear();
            mAdapterList = mPayModel.getAllBill(mEdong);
            mIPayView.showPayRecyclerFirstPage(adapterList);
        } else {
            mIPayView.showPayRecyclerOtherPage(pageIndex);
        }
    }*/

    @Override
    public void callPayRecycler(String mEdong, int pageIndex, Common.TYPE_SEARCH typeSearch, String infoSearch) {
        if (mEdong == null)
            return;
        if (infoSearch == null)
            return;
        if (typeSearch == null)
            return;

        if (typeSearch == Common.TYPE_SEARCH.ALL) {
            callPayRecyclerAll(mEdong, pageIndex);
        } else {
            callPayRecyclerSearch(mEdong, pageIndex, typeSearch, infoSearch);
        }
    }

    private void callPayRecyclerSearch(String mEdong, int pageIndex, Common.TYPE_SEARCH typeSearch, String infoSearch) {
    }

    private void callPayRecyclerAll(String mEdong, int pageIndex) {
        if (pageIndex == PayFragment.FIRST_PAGE_INDEX) {
            mAdapterList.clear();
            mAdapterList = mPayModel.getAllBill(mEdong);


            mAdapterListSearch = new ArrayList<>();
            int index = 0;
            int max = ROWS_ON_PAGE;

            for (; index < max; index++) {
                mAdapterListSearch.add(mAdapterList.get(index));
            }

            int totalPage = mAdapterList.size() / ROWS_ON_PAGE;

            if (totalPage * ROWS_ON_PAGE != mAdapterList.size() || totalPage == 0)
                totalPage++;

            mIPayView.showPayRecyclerFirstPage(mAdapterListSearch, pageIndex, totalPage);
        } else {
            mIPayView.showPayRecyclerOtherPage(pageIndex);
        }
    }
}
