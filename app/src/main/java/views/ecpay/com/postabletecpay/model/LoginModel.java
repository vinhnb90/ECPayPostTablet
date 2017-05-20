package views.ecpay.com.postabletecpay.model;

import android.content.Context;

import retrofit2.Retrofit;
import views.ecpay.com.postabletecpay.model.sharedPreference.ICommonSharedReference;
import views.ecpay.com.postabletecpay.model.sharedPreference.SharePrefManager;
import views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection;

/**
 * Created by VinhNB on 5/15/2017.
 */

public class LoginModel implements ICommonSharedReference {
    private SQLiteConnection db;

    public LoginModel() {
    }

    @Override
    public SharePrefManager initialManagerSharedPref(Context context) {
        if (context == null)
            return null;

        return SharePrefManager.getInstance(context);
    }
}
