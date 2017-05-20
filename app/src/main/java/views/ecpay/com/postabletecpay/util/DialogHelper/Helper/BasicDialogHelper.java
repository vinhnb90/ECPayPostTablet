package views.ecpay.com.postabletecpay.util.DialogHelper.Helper;


import views.ecpay.com.postabletecpay.util.DialogHelper.Entity.DialogEntity;
import views.ecpay.com.postabletecpay.util.DialogHelper.Imp.BasicImpDialog;
import views.ecpay.com.postabletecpay.util.DialogHelper.Imp.DialogImp;

/**
 * Created by VinhNB on 2/28/2017.
 */

public class BasicDialogHelper extends DialogHelper{
    private DialogEntity mDialogEntity;

    public BasicDialogHelper(DialogEntity mDialogEntity) {
        if (mDialogEntity == null) {
            return;
        }
        this.mDialogEntity = mDialogEntity;
    }

    @Override
    public DialogImp build() {
        return new BasicImpDialog(this.mDialogEntity);
    }
}
