package views.ecpay.com.postabletecpay.view.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.presenter.IMainPresenter;
import views.ecpay.com.postabletecpay.presenter.MainPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoFragment;
import views.ecpay.com.postabletecpay.view.TaiKhoan.UserInfoFragment;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;
import views.ecpay.com.postabletecpay.view.TrangChu.MainPageFragment;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.REQUEST_BARCODE;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.RESPONSE_BARCODE;

public class MainActivity extends AppCompatActivity implements
        IMainView,
        MainPageFragment.OnFragmentInteractionListener,
        PayFragment.CallbackDeleteBillOnlineDialog,
        BaoCaoFragment.OnFragmentInteractionListener,
        UserInfoFragment.OnFragmentInteractionListener{

    private IOnPauseScannerBarcodeListner pauseScannerBarcodeListner;

    public static BottomNavigationView sNavigation;
    public static String mEdong;
    private IMainPresenter iMainPresenter;
    private static boolean isLoginCall;

    private Handler mHander = new Handler();
    private Runnable mRunableCheckPostBill = new Runnable() {
        @Override
        public void run() {
            boolean currentConnect = Common.isNetworkConnected(MainActivity.this);
            if (!hasNetworkLast && currentConnect) {
                if (iMainPresenter != null) {
                    iMainPresenter.checkAndPostBill();
                }
            }
            hasNetworkLast = currentConnect;
            if(mHander != null && mRunableCheckPostBill != null)
                mHander.postDelayed(mRunableCheckPostBill, Common.TIME_OUT_CHECK_CONNECTION);
        }
    };


    private boolean hasNetworkLast;

    @Override
    public Context getContextView() {
        return this;
    }

    @Override
    public void showTextMessage(String textMessage) {
        Toast.makeText(this, textMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshInfoMain() {
        //check fragment
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
            return;
        }

        if (fragmentVisibling instanceof MainPageFragment)
            ((MainPageFragment) fragmentVisibling).refreshInfoMain();
    }

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

    public MainActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        hasNetworkLast = Common.isNetworkConnected(this);
        mHander.postDelayed(mRunableCheckPostBill, Common.TIME_OUT_CHECK_CONNECTION);
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
        iMainPresenter = new MainPresenter(this, mEdong);
//        iMainPresenter.refreshDataPayAdapter();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isLoginCall) {
                    try {
                        iMainPresenter.synchronizePC();
                    } catch (Exception e) {
                        showTextMessage(e.getMessage());
                        e.printStackTrace();
                    } finally {
                        isLoginCall = false;
                    }
                }
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, MainPageFragment.newInstance(mEdong));
        fragmentTransaction.commit();

        sNavigation = (BottomNavigationView) findViewById(R.id.navigation_ac_main);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        //check fragment
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling instanceof MainPageFragment)
            ((MainPageFragment) fragmentVisibling).refreshInfoMain();
    }


    @Override
    protected void onDestroy() {
        if(mHander != null && mRunableCheckPostBill != null)
            mHander.removeCallbacks(mRunableCheckPostBill);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BARCODE && resultCode == RESPONSE_BARCODE & data != null) {

            if (pauseScannerBarcodeListner != null) {
                pauseScannerBarcodeListner.pause();
            }

            //check fragment
            Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
            if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
                return;
            }


        }
    }

    public IOnPauseScannerBarcodeListner getPauseScannerBarcodeListner() {
        return pauseScannerBarcodeListner;
    }

    public void setPauseScannerBarcodeListner(IOnPauseScannerBarcodeListner pauseScannerBarcodeListner) {
        this.pauseScannerBarcodeListner = pauseScannerBarcodeListner;
    }

    private void getBundle() {
        mEdong = getIntent().getExtras().getString(KEY_EDONG, "");
        isLoginCall = true;
    }

    public static void updateNavigationBarState(int actionId) {
        Menu menu = sNavigation.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setChecked(menuItem.getItemId() == actionId);
        }
    }

    //region PayAdapter.BillInsidePayAdapter.BillInsidePayViewHolder.OnInterationBillInsidePayAdapter
//    @Override
//    public void processCheckedBillFragment(String edong, String code, int posCustomer, List<PayAdapter.BillEntityAdapter> billList, int posBillInside, int indexBegin, int indexEnd) {
//        if (TextUtils.isEmpty(edong))
//            return;
//        if (TextUtils.isEmpty(code))
//            return;
//        if (billList == null || billList.size() == ZERO)
//            return;
//
//        PayAdapter.BillEntityAdapter bill = billList.get(posBillInside);
//
//       /* //
//        boolean isNotBillPayedTermBefore = false;
//        int index = posBillInside;
//
//        String term = billList.get(posBillInside).getTHANG_THANH_TOAN();
//
//
//        for (; index < billList.size(); index++) {
//            String termIndex = billList.get(index).getTHANG_THANH_TOAN();
//            if (billList.get(index).getTRANG_THAI_TT() == Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode() && billList.get(index).isChecked() == false && termIndex.equals(term)== false) {
//                isNotBillPayedTermBefore = true;
//            }
//        }*/
//
//        //check fragment
//        PayFragment payFragment = null;
//        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
//        if (fragmentVisibling instanceof PayFragment && fragmentVisibling.isVisible() == true) {
//            payFragment = (PayFragment) fragmentVisibling;
//        }
//
//        if (payFragment == null)
//            return;
//
//       /* if (isNotBillPayedTermBefore) {
//            payFragment.showMessageNotifyPayfrag(Common.CODE_REPONSE_BILL_ONLINE.ex10003.getMessage());
//            return;
//        }*/
//
//        payFragment.showBillCheckedFragment(edong, code, posCustomer, bill, posBillInside, indexBegin, indexEnd);
//    }
//
//    @Override
//    public void processDeleteBillOnlineFragment(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside) {
//        boolean fail = TextUtils.isEmpty(edong) || TextUtils.isEmpty(code) || bill == null;
//        if (fail)
//            return;
//
//        //check fragment
//        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
//        if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
//            return;
//        }
//        if (fragmentVisibling instanceof PayFragment)
//            ((PayFragment) fragmentVisibling).processDialogDeleteBillOnline(edong, code, bill, posCustomerInside);
//    }
//
//    @Override
//    public void processUnCheckedBillFragment(String message) {
//        boolean fail = TextUtils.isEmpty(message);
//        if (fail)
//            return;
//
//        //check fragment
//        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
//        if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
//            return;
//        }
//        if (fragmentVisibling instanceof PayFragment)
//            ((PayFragment) fragmentVisibling).showMessageNotifyPayfrag(message);
//
//    }
    //endregion


    //region PayFragment.CallbackPayingOnlineDialog

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

    //region OnFragmentInteractionListener
    @Override
    public void switchNavigationBottomMenu(ID_MENU_BOTTOM typeMenu) {
        sNavigation.getMenu().getItem(typeMenu.getIndex());
    }
    //endregion

    public interface IOnPauseScannerBarcodeListner {
        public void pause();
    }

}
