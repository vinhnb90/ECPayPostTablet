package views.ecpay.com.postabletecpay.util.DialogHelper.Helper;


import views.ecpay.com.postabletecpay.util.DialogHelper.Entity.DialogEntity;
import views.ecpay.com.postabletecpay.util.DialogHelper.Imp.DialogImp;
import views.ecpay.com.postabletecpay.util.DialogHelper.Imp.LayoutOKBasicImpDialog;

/**
 * Created by VinhNB on 2/28/2017.
 */

public class LayoutOKBasicDialogHelper extends DialogHelper{
    private DialogEntity mDialogEntity;
    public LayoutOKBasicDialogHelper(DialogEntity dialogEntity) {
        if (dialogEntity == null)
            return;
        this.mDialogEntity = dialogEntity;
    }

    @Override
    public DialogImp build() {
        return new LayoutOKBasicImpDialog(mDialogEntity);
    }
}
