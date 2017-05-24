package views.ecpay.com.postabletecpay.presenter;

import views.ecpay.com.postabletecpay.model.UserInfoModel;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Account;
import views.ecpay.com.postabletecpay.view.TaiKhoan.IUserInfoView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by VinhNB on 5/23/2017.
 */

public class UserInfoPresenter implements IUserInfoPresenter {
    private UserInfoModel userInfoModel;
    private IUserInfoView iUserInfoView;

    public UserInfoPresenter(IUserInfoView iUserInfoView) {
        this.iUserInfoView = iUserInfoView;
        userInfoModel = new UserInfoModel(iUserInfoView.getContextView());
    }

    @Override
    public void getInfoUser(String edong) {
        Account account = userInfoModel.getAccountInfo(edong);
        String phone = "?";

        iUserInfoView.showInfoUser(account.getEdong(), account.getBirthday(), account.getName(), account.getIdNumber(), phone, account.getEmail(), account.getAddress(), account.getEdong(), account.getBalance(),account.getType());
    }
}

interface IUserInfoPresenter {
    void getInfoUser(String edong);
}
