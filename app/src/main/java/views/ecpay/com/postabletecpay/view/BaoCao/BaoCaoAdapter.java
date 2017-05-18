package views.ecpay.com.postabletecpay.view.BaoCao;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by macbook on 4/30/17.
 */

public class BaoCaoAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENT_COUNT = 4;

    public BaoCaoAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new BaoCaoTongHopFragment();
            case 1:
                return new BaoCaoChiTietFragment();
            case 2:
                return new BaoCaoHoanTraFragment();
            case 3:
                return new BaoCaoLichSuFragment();
        }
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
                return "Tổng hợp";
            case 1:
                return "Chi tiết";
            case 2:
                return "Hoàn trả";
            case 3:
                return "Lịch sử KH";
        }
        return null;
    }

}
