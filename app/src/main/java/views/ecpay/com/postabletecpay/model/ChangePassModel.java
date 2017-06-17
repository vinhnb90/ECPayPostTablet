package views.ecpay.com.postabletecpay.model;

import android.content.Context;
import android.text.TextUtils;

import views.ecpay.com.postabletecpay.model.sharedPreference.ICommonSharedReference;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;

/**
 * Created by VinhNB on 5/20/2017.
 */

public class ChangePassModel extends CommonModel implements ICommonSharedReference {
    private SharePrefManager sharePrefManager;

    public ChangePassModel(Context context) {
        super(context);
        sharePrefManager = SharePrefManager.getInstance(context);
    }

    @Override
    public SharePrefManager getManagerSharedPref() {
        return sharePrefManager;
    }

    public String getSessionLogin(String mEdong) {
        if (TextUtils.isEmpty(mEdong))
            return null;

        return sqLiteConnection.selectSessionAccount(mEdong);
    }
}
