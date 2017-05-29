package views.ecpay.com.postabletecpay.presenter;

import views.ecpay.com.postabletecpay.util.commons.Common;

/**
 * Created by VinhNB on 5/26/2017.
 */

public interface IPayPresenter {
        void callPayRecycler(String mEdong, int pageIndex);
        void callPayRecycler(String mEdong, int pageIndex, Common.TYPE_SEARCH typeSearch, String infoSearch);

}
