package views.ecpay.com.postabletecpay.model.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

/**
 * Created by VinhNB on 9/19/2016.
 */
public class SharePrefManager {
    private static SharePrefManager sInstance;
    private Context mContext;

    public static SharePrefManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SharePrefManager(context);
        }
        return sInstance;
    }

    private SharePrefManager(Context context) {
        mContext = context;
    }

    public boolean addSharePref(String keySharePref, int mode) {
        boolean existSharePref = checkExistSharePref(keySharePref);
        if (existSharePref) {
            return false;
        }
        mContext.getSharedPreferences(keySharePref, mode).edit().commit();
        return true;
    }

    public SharedPreferences getSharePref(String keySharePref, int mode) {
        boolean existSharePref = checkExistSharePref(keySharePref);
        if(existSharePref){
            SharedPreferences preferences = mContext.getSharedPreferences(keySharePref, mode);
            return preferences;
        }
        return null;
    }

    private boolean checkExistSharePref(String keySharePref) {
        File f = new File(
                "/data/data/" + mContext.getApplicationContext().getPackageName() + "/shared_prefs/" + keySharePref + ".xml");
        if (f.exists())
            return true;
        else
            return false;
    }
}
