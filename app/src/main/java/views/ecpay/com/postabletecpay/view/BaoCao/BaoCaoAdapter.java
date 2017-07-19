package views.ecpay.com.postabletecpay.view.BaoCao;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by macbook on 4/30/17.
 */

public class BaoCaoAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENT_COUNT = 4;


    Map<Integer, Fragment> fragmentMap;

    IBaoCaoView baoCaoView;

    public BaoCaoAdapter(IBaoCaoView view, FragmentManager fm) {
        super(fm);
        this.baoCaoView = view;
        fragmentMap = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = BaoCaoTongHopFragment.newInstance(this.baoCaoView);
                break;
            case 1:
                fragment = BaoCaoChiTietFragment.newInstance(this.baoCaoView);
                break;
            case 2:
                fragment = BaoCaoHoanTraFragment.newInstance(this.baoCaoView);
                break;
            case 3:
                fragment = BaoCaoLichSuFragment.newInstance(this.baoCaoView);
                break;
        }
        if(fragment != null)
        {
            fragmentMap.put(position, fragment);
        }
        return fragment;
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


    public Fragment getFragment(int pos)
    {
        return fragmentMap.get(pos);
    }

}
