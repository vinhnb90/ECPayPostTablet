package views.ecpay.com.postabletecpay.view.Main;

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

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayBillsDialogAdapter;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoFragment;
import views.ecpay.com.postabletecpay.view.TaiKhoan.UserInfoFragment;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;
import views.ecpay.com.postabletecpay.view.TrangChu.MainPageFragment;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;

public class MainActivity extends AppCompatActivity implements
        MainPageFragment.OnFragmentInteractionListener,
        PayAdapter.OnInterationBillInsidePayAdapter,
        PayBillsDialogAdapter.OnInteractionBillDialogRecycler,
        PayFragment.OnFragmentInteractionListener,
        PayFragment.CallbackPayingOnlineDialog,
        PayFragment.CallbackDeleteBillOnlineDialog,
        BaoCaoFragment.OnFragmentInteractionListener,
        UserInfoFragment.OnFragmentInteractionListener {

    public static BottomNavigationView navigation;
    private String mEdong;

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

        navigation = (BottomNavigationView) findViewById(R.id.navigation_ac_main);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void getBundle() {
        mEdong = getIntent().getExtras().getString(KEY_EDONG, "");
    }

    public static void updateNavigationBarState(int actionId) {
        Menu menu = navigation.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setChecked(menuItem.getItemId() == actionId);
        }
    }

    //region PayAdapter.BillInsidePayAdapter.BillInsidePayViewHolder.OnInterationBillInsidePayAdapter
    @Override
    public void processCheckedBillFragment(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomer) {
        if (TextUtils.isEmpty(edong))
            return;
        if (TextUtils.isEmpty(code))
            return;
        if (bill == null)
            return;

        //check fragment
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
            return;
        }

        ((PayFragment) fragmentVisibling).showBillCheckedFragment(edong, code, bill, posCustomer);
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

        ((PayFragment) fragmentVisibling).processDialogDeleteBillOnline(edong, code, bill, posCustomerInside);
    }

    //endregion

    //region PayBillsDialogAdapter.OnInteractionBillDialogRecycler
    @Override
    public void processDataBillsDialogChecked(int pos, boolean isChecked) {

        //check fragment
        Fragment fragmentVisibling = this.getSupportFragmentManager().findFragmentById(R.id.frameLayout);
        if (fragmentVisibling == null || fragmentVisibling.isVisible() == false) {
            return;
        }

        ((PayFragment) fragmentVisibling).showBillCheckedDialog(mEdong, pos, isChecked);

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

        ((PayFragment) fragmentVisibling).bindViewAgain();
        ((PayFragment) fragmentVisibling).refreshRecyclerListFragment();
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

//        ((PayFragment) fragmentVisibling).showDialogDeleteBillOnline();
//        ((PayFragment) fragmentVisibling).refreshRecyclerListFragment();
    }
    //endregion
}
