package views.ecpay.com.postabletecpay.presenter;

import android.content.Intent;


/**
 * Created by MyPC on 23/06/2017.
 */

public interface IBluetoothPresenter {
    void getOnActivityResult(int requestCode, int resultCode, Intent data);
}
