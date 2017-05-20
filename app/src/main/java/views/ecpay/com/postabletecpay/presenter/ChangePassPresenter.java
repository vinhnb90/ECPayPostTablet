package views.ecpay.com.postabletecpay.presenter;

import views.ecpay.com.postabletecpay.model.ChangePassModel;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.DoiMatKhau.IChangePassView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by VinhNB on 5/20/2017.
 */

public class ChangePassPresenter implements IChangePassPresenter {
    public ChangePassPresenter(IChangePassView mIChangePassView) {
        this.mIChangePassView = mIChangePassView;
        mChangePassModel = new ChangePassModel();
    }

    @Override
    public void validateInput(String passOld, String passNew, String passRetype) {
        if (passOld == null || passOld.isEmpty() || passOld.trim().equals("")) {
            mIChangePassView.showText(Common.MESSAGE_NOTIFY.CHANGE_PASS_ERR_PASS_OLD.toString());
            return;
        }
        if (passNew == null || passNew.isEmpty() || passNew.trim().equals("")) {
            mIChangePassView.showText(Common.MESSAGE_NOTIFY.CHANGE_PASS_ERR_PASS_NEW.toString());
            return;
        }
        if (passNew.equals(passOld)) {
            mIChangePassView.showText(Common.MESSAGE_NOTIFY.CHANGE_PASS_ERR_PASS_NEW_NOT_EQUAL_PASS_OLD.toString());
            return;
        }
        if (!passRetype.equals(passNew)) {
            mIChangePassView.showText(Common.MESSAGE_NOTIFY.CHANGE_PASS_ERR_PASS_RETYPE_NOT_EQUAL_PASS_RETYPE.toString());
            return;
        }

        String userName = mChangePassModel.initialManagerSharedPref(mIChangePassView.getContextView())
                .getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                .getString(Common.SHARE_REF_FILE_LOGIN_USER_NAME, "");

        String pass = mChangePassModel.initialManagerSharedPref(mIChangePassView.getContextView())
                .getSharePref(Common.SHARE_REF_FILE_LOGIN, MODE_PRIVATE)
                .getString(Common.SHARE_REF_FILE_LOGIN_PASS, "");

        ConfigInfo configInfo = null;
        try {
            configInfo = Common.setupInfoRequest(mIChangePassView.getContextView(), userName, pass, Common.COMMAND_ID.CHANGE_PIN.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //create request to server
        String session = "?";
        String jsonRequestLogin = SoapAPI.getJsonRequestChangePass(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                configInfo.getPinLoginEncrypted(),

                session,
                passNew.trim(),
                passRetype.trim(),

                configInfo.getAccountId());

    }

    private IChangePassView mIChangePassView;
    private ChangePassModel mChangePassModel;
}
