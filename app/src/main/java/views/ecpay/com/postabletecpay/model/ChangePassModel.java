package views.ecpay.com.postabletecpay.model;

import android.content.Context;

import views.ecpay.com.postabletecpay.model.sharedPreference.ICommonSharedReference;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;

/**
 * Created by VinhNB on 5/20/2017.
 */

public class ChangePassModel implements ICommonSharedReference {

    @Override
    public SharePrefManager initialManagerSharedPref(Context context) {
        if (context == null)
            return null;

        return SharePrefManager.getInstance(context);
    }
}
