package views.ecpay.com.postabletecpay.view.ThanhToan;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoChiTietFragment;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoHoanTraFragment;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoLichSuFragment;
import views.ecpay.com.postabletecpay.view.BaoCao.BaoCaoTongHopFragment;

/**
 * Created by macbook on 4/30/17.
 */

public class TimKiemAdapter extends FragmentPagerAdapter {

    public TimKiemAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return null;
    }

    @Override
    public int getCount() {
        return Common.TYPE_ACCOUNT.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = Common.TYPE_SEARCH.findCodeMessage(position).getType();
        return title;
    }

}
