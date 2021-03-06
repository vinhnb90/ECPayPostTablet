package views.ecpay.com.postabletecpay.view.Main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.Result;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayBillsDialogAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoFragment;
import views.ecpay.com.postabletecpay.view.TaiKhoan.UserInfoFragment;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;
import views.ecpay.com.postabletecpay.view.TrangChu.MainPageFragment;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;
import static views.ecpay.com.postabletecpay.util.commons.Common.NEGATIVE_ONE;
import static views.ecpay.com.postabletecpay.util.commons.Common.ONE;
import static views.ecpay.com.postabletecpay.util.commons.Common.ZERO;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.REQUEST_BARCODE;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.RESPONSE_BARCODE;

public class MainActivity extends AppCompatActivity implements
        MainPageFragment.OnFragmentInteractionListener,
        PayAdapter.OnInterationBillInsidePayAdapter,
        PayBillsDialogAdapter.OnInteractionBillDialogRecycler,
        PayFragment.OnPayFragmentInteractionListener,
        PayFragment.CallbackPayingOnlineDialog,
        PayFragment.CallbackDeleteBillOnlineDialog,
        BaoCaoFragment.OnFragmentInteractionListener,
        ZXingScannerView.ResultHandler,
        UserInfoFragment.OnFragmentInteractionListener {

    public static BottomNavigationView sNavigation;
    public static String mEdong;

    public enum ID_MENU_BOTTOM {
        HOME(1),
        PAY(2),
        REPORT(3),
        ACCOUNT(4);

        private int index;

        ID_MENU_BOTTOM(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = MainPageFragment.newInstance(mEdong);
                    break;
                case R.id.navigation_pay:
                    fragment = PayFragment.newInstance(mEdong);
                    break;
                case R.id.navigation_report:
                    fragment = BaoCaoFragment.newInstance();
                    break;
                case R.id.navigation_accout:
                    fragment = UserInfoFragment.newInstance(mEdong);
                    break;
            }
            if (fragment != null) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            }
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            getSupportActionBar().hide();
            if (Build.VERSION.SDK_INT < 16) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                View decorView = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                decorView.setSystemUiVisibility(uiOptions);
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        getBundle();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, MainPageFragment.newInstance(mEdong));
        fragmentTransaction.commit();

        sNavigation = (BottomNavigationView) findViewById(R.id.navigation_ac_main);


       /* BottomNavigationMenuView menuView = (BottomNavigationMenuView) sNavigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            menuView.getChildAt(i).get
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }*/

        sNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    protected void onPause() {
        super.onPause();

        //check fragment
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
            return;
        }

        if (fragmentVisibling instanceof PayFragment)
            ((PayFragment) fragmentVisibling).onPauseScannerBarcode();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BARCODE && resultCode == RESPONSE_BARCODE & data != null) {
            //check fragment
            Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
            if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
                return;
            }

            if (fragmentVisibling instanceof PayFragment)
                ((PayFragment) fragmentVisibling).onPauseScannerBarcode();
        }
    }

    private void getBundle() {
        mEdong = getIntent().getExtras().getString(KEY_EDONG, "");
//        mEdong = "01656226909";
    }

    public static void updateNavigationBarState(int actionId) {
        Menu menu = sNavigation.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setChecked(menuItem.getItemId() == actionId);
        }
    }

    //region PayAdapter.BillInsidePayAdapter.BillInsidePayViewHolder.OnInterationBillInsidePayAdapter
    @Override
    public void processCheckedBillFragment(String edong, String code, int posCustomer, List<PayAdapter.BillEntityAdapter>  billList, int posBillInside) {
        if (TextUtils.isEmpty(edong))
            return;
        if (TextUtils.isEmpty(code))
            return;
        if (billList == null || billList.size() == ZERO)
            return;

        PayAdapter.BillEntityAdapter bill = billList.get(posBillInside);

        //TODO check hóa đơn kỳ xa nhất fragment
        boolean isHasBillNotPayTermBefore = false;
        int index = posCustomer + ONE;

        for (; index < billList.size(); index++) {
            if (billList.get(index).getStatus() == Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode()) {
                isHasBillNotPayTermBefore = true;
            }
        }

        //check fragment
        PayFragment payFragment = null;
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling instanceof PayFragment && fragmentVisibling.isVisible() == true) {
            payFragment = (PayFragment) fragmentVisibling;
        }

        if (payFragment == null)
            return;

        if (isHasBillNotPayTermBefore) {
            payFragment.showMessageNotifyPayfrag(Common.CODE_REPONSE_BILL_ONLINE.ex10003.getMessage());
            return;
        }

        payFragment.showBillCheckedFragment(edong, code, bill, posCustomer);
    }

    @Override
    public void processDeleteBillOnlineFragment(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside) {
        boolean fail = TextUtils.isEmpty(edong) || TextUtils.isEmpty(code) || bill == null;
        if (fail)
            return;

        //check fragment
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
            return;
        }
        if (fragmentVisibling instanceof PayFragment)
            ((PayFragment) fragmentVisibling).processDialogDeleteBillOnline(edong, code, bill, posCustomerInside);
    }
    //endregion

    //region PayBillsDialogAdapter.OnInteractionBillDialogRecycler
    @Override
    public void processCheckedBillsDialog(int pos, boolean isChecked) {

        //check fragment
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
            return;
        }
        if (fragmentVisibling instanceof PayFragment)
            ((PayFragment) fragmentVisibling).showBillCheckedDialog(mEdong, pos, isChecked);

    }

    @Override
    public void processClickMessageErrorBillDialog(String messageError) {
        Toast.makeText(this, messageError, Toast.LENGTH_SHORT).show();
//        //check fragment
//        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
//        if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
//            return;
//        }
//        if (fragmentVisibling instanceof PayFragment)
//            ((PayFragment) fragmentVisibling).showMessage(mEdong, pos, isChecked);
    }
    //endregion

    //region PayFragment.CallbackPayingOnlineDialog
    @Override
    public void processOnDismissPayingOnlineDialog() {
        //check fragment
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
            return;
        }
        if (fragmentVisibling instanceof PayFragment) {
            ((PayFragment) fragmentVisibling).bindViewAgain();
            ((PayFragment) fragmentVisibling).refreshRecyclerListFragment();
        }
    }

    //endregion

    //region PayFragment.CallbackDeleteBillOnlineDialog,
    @Override
    public void processOnDismissDeleteBillOnlineDialog() {
        //check fragment
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
            return;
        }

    }
    //endregion

    //region ZXingScannerView.ResultHandler
    @Override
    public void handleResult(Result result) {
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling instanceof PayFragment)
            ((PayFragment) fragmentVisibling).fillResultToTextBarcodeDialog(result.getText());
    }

    @Override
    public void fillToSearchText(String textBarcode) {
        if (TextUtils.isEmpty(textBarcode))
            return;
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling instanceof PayFragment)
            ((PayFragment) fragmentVisibling).fillResultToSearchText(textBarcode);
    }

    @Override
    public void setRootViewAgain() {
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling instanceof PayFragment) {
            ((PayFragment) fragmentVisibling).bindViewAgain();
        }
    }

    @Override
    public void refreshCamera(ZXingScannerView mScannerView) {
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void showMainPageFragment() {
        Fragment fragment = MainPageFragment.newInstance(mEdong);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
        }
    }


    //region OnFragmentInteractionListener
    @Override
    public void switchNavigationBottomMenu(ID_MENU_BOTTOM typeMenu) {
        sNavigation.getMenu().getItem(typeMenu.getIndex());
    }
    //endregion
}
