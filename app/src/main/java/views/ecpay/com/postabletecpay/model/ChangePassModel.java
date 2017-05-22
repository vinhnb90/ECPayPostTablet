package views.ecpay.com.postabletecpay.model;

import android.content.Context;

import views.ecpay.com.postabletecpay.model.sharedPreference.ICommonSharedReference;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;

/**
 * Created by VinhNB on 5/20/2017.
 */

public class ChangePassModel implements ICommonSharedReference {
    private SharePrefManager sharePrefManager;

    public ChangePassModel(Context context) {
        sharePrefManager = SharePrefManager.getInstance(context);
    }

    @Override
    public SharePrefManager getManagerSharedPref() {
        return sharePrefManager;
    }
}
