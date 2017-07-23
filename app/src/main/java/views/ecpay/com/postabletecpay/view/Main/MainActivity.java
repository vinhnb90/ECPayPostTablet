package views.ecpay.com.postabletecpay.view.Main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import views.ecpay.com.postabletecpay.Config.Config;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.presenter.IMainPresenter;
import views.ecpay.com.postabletecpay.presenter.MainPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoFragment;
import views.ecpay.com.postabletecpay.view.TaiKhoan.UserInfoFragment;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;
import views.ecpay.com.postabletecpay.view.TrangChu.MainPageFragment;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_DELAY_DOWNLOAD;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.REQUEST_BARCODE;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.RESPONSE_BARCODE;

public class MainActivity extends AppCompatActivity implements
        IMainView,
        MainPageFragment.OnFragmentInteractionListener,
        BaoCaoFragment.OnFragmentInteractionListener,
        UserInfoFragment.OnFragmentInteractionListener {

    private IOnPauseScannerBarcodeListner pauseScannerBarcodeListner;

    public static BottomBar sNavigation;
    public static String mEdong;
    private IMainPresenter iMainPresenter;
    private static boolean isLoginCall;

    private ProgressDialog progressDialog;
<<<<<<< HEAD
    private Handler progressBarHandler = new Handler();

=======
    private String showProgress = "SHOW_DIALOG" ;
>>>>>>> datpd
    private Handler mHander = new Handler();
    private Runnable mRunableCheckPostBill = new Runnable() {
        @Override
        public void run() {
            boolean currentConnect = Common.isNetworkConnected(MainActivity.this);
            boolean posted = true;
            if (!hasNetworkLast && currentConnect) {
                if (iMainPresenter != null) {
                    try {
                        posted = iMainPresenter.checkAndPostBill();
                    } catch (Exception e) {
                        MainActivity.this.finish();
                    }
                }
            }
            if (posted)
                hasNetworkLast = currentConnect;
            if (mHander != null && mRunableCheckPostBill != null)
                mHander.postDelayed(mRunableCheckPostBill, Common.TIME_OUT_CHECK_CONNECTION);
            iMainPresenter.checkAndPostBill();
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
        if (fragmentVisibling instanceof MainPageFragment)
            ((MainPageFragment) fragmentVisibling).refreshInfoMain();
    }

    @Override
    public void startShowPbarDownload() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);

        progressBarHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Bắt đầu quá trình đồng bộ ...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setProgress(0);
                progressDialog.setMax(100);
                progressDialog.show();
            }
        });
    }

    @Override
    public void finishHidePbarDownload() {
<<<<<<< HEAD
        if (progressDialog == null)
            return;
        progressBarHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                //kết thúc thì refresh lại thông tin nếu đang ở mainPage
                refreshInfoMain();
            }
        });
    }

    @Override
    public void updatePbarDownload(final String message, final int statusProcess) {
        if (progressDialog == null)
            return;
        progressBarHandler.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(message);
                progressDialog.setProgress(statusProcess);
            }
        });
    }

    @Override
    public void updateDelayPbarDownload(final String title, final int statusProcess, final int timeDelay) {
        if (progressDialog == null)
            return;
        progressBarHandler.postDelayed(new Runnable() {
                                           @Override
                                           public void run() {
                                               progressDialog.setMessage(title);
                                               progressDialog.setProgress(statusProcess);
                                           }
                                       }, timeDelay
        );
=======
        if(progressDialog != null&& progressDialog.isShowing())
        {
            progressDialog.dismiss();
            refreshInfoMain();
        }
>>>>>>> datpd
    }

    public enum ID_MENU_BOTTOM {
        HOME(0),
        PAY(1),
        REPORT(2),
        ACCOUNT(3);

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
                    fragment = BaoCaoFragment.newInstance(mEdong);
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
        if (progressDialog != null) {
            savedInstanceState.putBoolean(showProgress, progressDialog.isShowing());
        }
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
                        //start UI prgressbar
                        startShowPbarDownload();
                        iMainPresenter.sync();
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                iMainPresenter.syncBookCmis();
//                            }
//                        }).start();

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

        sNavigation = (BottomBar) findViewById(R.id.navigation_ac_main);
        sNavigation.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                Fragment fragment = null;
                switch (tabId) {
                    case R.id.navigation_home:
                        fragment = MainPageFragment.newInstance(mEdong);
                        break;
                    case R.id.navigation_pay:
                        fragment = PayFragment.newInstance(mEdong);
                        break;
                    case R.id.navigation_report:
                        fragment = BaoCaoFragment.newInstance(mEdong);
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
            }
        });

        sNavigation.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
            }
        });
    }
    @Override
    public void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (state != null){
            if (state.getBoolean("SHOW_DIALOG") && progressDialog.isShowing()){
                startShowPbarDownload();
            }
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            final View view = getCurrentFocus();

            if (view != null) {
                final boolean consumed = super.dispatchTouchEvent(ev);

                final View viewTmp = getCurrentFocus();
                final View viewNew = viewTmp != null ? viewTmp : view;

                if (viewNew.equals(view)) {
                    final Rect rect = new Rect();
                    final int[] coordinates = new int[2];

                    view.getLocationOnScreen(coordinates);

                    rect.set(coordinates[0], coordinates[1], coordinates[0] + view.getWidth(), coordinates[1] + view.getHeight());

                    final int x = (int) ev.getX();
                    final int y = (int) ev.getY();

                    if (rect.contains(x, y)) {
                        return consumed;
                    }
                } else if (viewNew instanceof EditText) {
                    return consumed;
                }

                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(viewNew.getWindowToken(), 0);

                viewNew.clearFocus();

                return consumed;
            }
        }

        return super.dispatchTouchEvent(ev);
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
    }


    @Override
    protected void onDestroy() {
        if (mHander != null && mRunableCheckPostBill != null)
            mHander.removeCallbacks(mRunableCheckPostBill);
<<<<<<< HEAD
        isLoginCall = false;
=======
//        if (progressDialog != null && progressDialog.isShowing()){
//            progressDialog.cancel();
//        }
>>>>>>> datpd
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

    Toast toast;
    long lastTimePressed = 0;

    @Override
    public void onBackPressed() {
        Fragment currentFragment = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (currentFragment instanceof MainPageFragment) {
            if (System.currentTimeMillis() - lastTimePressed > 2500) {
                toast = Toast.makeText(getApplicationContext(),
                        "Nhấn lại lần nữa để thoát", Toast.LENGTH_SHORT);
                toast.show();
                lastTimePressed = System.currentTimeMillis();
            } else {
                if (toast != null) {
                    toast.cancel();
                }
                System.exit(0);
            }
        } else {
            this.getSupportFragmentManager().popBackStack();
        }
    }

    public void setPauseScannerBarcodeListner(IOnPauseScannerBarcodeListner pauseScannerBarcodeListner) {
        this.pauseScannerBarcodeListner = pauseScannerBarcodeListner;
    }

    private void getBundle() {
        mEdong = getIntent().getExtras().getString(KEY_EDONG, "");
        isLoginCall = true;
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


    //region OnFragmentInteractionListener
    @Override
    public void switchNavigationBottomMenu(final ID_MENU_BOTTOM typeMenu) {

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sNavigation.selectTabAtPosition(typeMenu.getIndex());
            }
        });


    }
    //endregion

    public interface IOnPauseScannerBarcodeListner {
        public void pause();
    }

    @Override
    public void showRespone(String code, String description) {
        if (!Config.isShowRespone())
            return;

        try {
            Toast.makeText(this, "CODE: " + code + "\n DESCRIPTION: " + description, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
