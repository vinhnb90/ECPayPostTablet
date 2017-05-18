package views.ecpay.com.postabletecpay.view.ThanhToan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoChiTietFragment;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoHoanTraFragment;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoLichSuFragment;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoTongHopFragment;

/**
 * Created by macbook on 4/30/17.
 */

public class TimKiemAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENT_COUNT = 6;

    public TimKiemAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return null;
    }

    @Override
    public int getCount() {
        return FRAGMENT_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Mã KH/Số thẻ";
            case 1:
                return "Tên KH";
            case 2:
                return "Số điện thoại";
            case 3:
                return "Địa chỉ";
            case 4:
                return "Sổ ghi chỉ số";
            case 5:
                return "Lộ trình";
        }
        return null;
    }

}
