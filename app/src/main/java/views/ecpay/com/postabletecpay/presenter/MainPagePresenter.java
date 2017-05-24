package views.ecpay.com.postabletecpay.presenter;

import views.ecpay.com.postabletecpay.model.MainPageModel;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.view.TrangChu.IMainPageView;

/**
 * Created by VinhNB on 5/23/2017.
 */

public class MainPagePresenter implements IMainPagePresenter{
    private IMainPageView iMainPageView;
    private MainPageModel mainPageModel;

    public MainPagePresenter(IMainPageView iMainPageView) {
        this.iMainPageView = iMainPageView;
        mainPageModel = new MainPageModel(iMainPageView.getContextView());
    }

    //region IMainPagePresenter
    @Override
    public void getInfoMain(String edong) {
        Account account = mainPageModel.getAccountInfo(edong);
        int countTotalBill = mainPageModel.getTotalBill(edong);
        int countTotalMoney = mainPageModel.getTotalMoney(edong);

        iMainPageView.showMainPageInfo(account.getName(), account.getBalance(), countTotalBill, countTotalMoney);
    }
    //endregion
}

interface IMainPagePresenter{
    void getInfoMain(String edong);
}