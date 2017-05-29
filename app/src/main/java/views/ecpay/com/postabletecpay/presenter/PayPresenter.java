package views.ecpay.com.postabletecpay.presenter;

import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter.PayEntityAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.ThanhToan.IPayView;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;

import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.FIRST_PAGE_INDEX;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.PAGE_INCREMENT;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.ROWS_ON_PAGE;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayPresenter implements IPayPresenter {
    private PayModel mPayModel;
    private IPayView mIPayView;
    private List<PayAdapter.PayEntityAdapter> mAdapterList = new ArrayList<>();
    private int mPageIndex;
    private int totalPage;

    public PayPresenter(IPayView mIPayView) {
        this.mIPayView = mIPayView;
        mPayModel = new PayModel(mIPayView.getContextView());
    }

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

        if (pageIndex == PayFragment.FIRST_PAGE_INDEX) {
            mAdapterList.clear();
            mAdapterList = mPayModel.getAllBillFitterBy(mEdong, typeSearch, infoSearch);

            totalPage = mAdapterList.size() / ROWS_ON_PAGE;
            if (totalPage * ROWS_ON_PAGE != mAdapterList.size() || (totalPage == 0))
                totalPage++;

            int index = 0;
            int maxIndex = pageIndex * ROWS_ON_PAGE;
            if (maxIndex > mAdapterList.size())
                maxIndex = mAdapterList.size();

            List<PayAdapter.PayEntityAdapter> fitter = new ArrayList<>();
            for (; index < maxIndex; index++)
                fitter.add(mAdapterList.get(index));

            mIPayView.showPayRecyclerFirstPage(fitter, pageIndex, totalPage);
        } else {
            if (pageIndex > totalPage)
                return;

            int index = ROWS_ON_PAGE * (pageIndex - FIRST_PAGE_INDEX);
            int maxIndex = ROWS_ON_PAGE * (pageIndex);
            if (maxIndex > mAdapterList.size())
                maxIndex = mAdapterList.size();

            List<PayAdapter.PayEntityAdapter> adapterListSearch = new ArrayList<>();
            for (; index < maxIndex; index++) {
                adapterListSearch.add(mAdapterList.get(index));
            }

            mIPayView.showPayRecyclerOtherPage(adapterListSearch, pageIndex, totalPage);
        }
    }

    private void callPayRecyclerAll(String mEdong, int pageIndex) {
        if (pageIndex == PayFragment.FIRST_PAGE_INDEX) {
            mAdapterList.clear();
            mAdapterList = mPayModel.getAllBill(mEdong);

            totalPage = mAdapterList.size() / ROWS_ON_PAGE;
            if (totalPage * ROWS_ON_PAGE != mAdapterList.size() || (totalPage == 0))
                totalPage++;

            int index = 0;
            int maxIndex = pageIndex * ROWS_ON_PAGE;
            if (maxIndex > mAdapterList.size())
                maxIndex = mAdapterList.size();

            List<PayAdapter.PayEntityAdapter> fitter = new ArrayList<>();
            for (; index < maxIndex; index++)
                fitter.add(mAdapterList.get(index));

            mIPayView.showPayRecyclerFirstPage(fitter, pageIndex, totalPage);
        } else {
            if (pageIndex > totalPage)
                return;

            int index = ROWS_ON_PAGE * (pageIndex - FIRST_PAGE_INDEX);
            int maxIndex = ROWS_ON_PAGE * (pageIndex);

            if (maxIndex > mAdapterList.size())
                maxIndex = mAdapterList.size();

            List<PayAdapter.PayEntityAdapter> adapterListSearch = new ArrayList<>();
            for (; index < maxIndex; index++) {
                adapterListSearch.add(mAdapterList.get(index));
            }

            mIPayView.showPayRecyclerOtherPage(adapterListSearch, pageIndex, totalPage);
        }
    }
}
