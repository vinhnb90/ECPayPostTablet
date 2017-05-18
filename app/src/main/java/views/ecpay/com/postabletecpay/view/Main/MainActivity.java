package views.ecpay.com.postabletecpay.view.Main;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoFragment;
import views.ecpay.com.postabletecpay.view.TaiKhoan.TaiKhoanFragment;
import views.ecpay.com.postabletecpay.view.ThanhToan.ThanhToanFragment;
import views.ecpay.com.postabletecpay.view.TrangChu.TrangChuFragment;

public class MainActivity extends AppCompatActivity implements TrangChuFragment.OnFragmentInteractionListener,
        ThanhToanFragment.OnFragmentInteractionListener, BaoCaoFragment.OnFragmentInteractionListener,
        TaiKhoanFragment.OnFragmentInteractionListener {

    public static BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = TrangChuFragment.newInstance();
                    break;
                case R.id.navigation_pay:
                    fragment = ThanhToanFragment.newInstance();
                    break;
                case R.id.navigation_report:
                    fragment = BaoCaoFragment.newInstance();
                    break;
                case R.id.navigation_accout:
                    fragment = TaiKhoanFragment.newInstance();
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
        } catch(Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, TrangChuFragment.newInstance());
        fragmentTransaction.commit();

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public static void updateNavigationBarState(int actionId){
        Menu menu = navigation.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setChecked(menuItem.getItemId() == actionId);
        }
    }

}
