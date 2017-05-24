package views.ecpay.com.postabletecpay.view.TrangChu;

import views.ecpay.com.postabletecpay.view.ICommonView;

/**
 * Created by VinhNB on 5/23/2017.
 */

public interface IMainPageView extends ICommonView{
    void showMainPageInfo(String userName, long balance, int totalBills, int totalMoney);
}
