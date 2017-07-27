package views.ecpay.com.postabletecpay.view.BaoCao;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;
import views.ecpay.com.postabletecpay.view.TrangChu.MainPageFragment;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;

/**
 * Created by macbook on 4/28/17.
 */

public class BaoCaoFragment extends Fragment implements View.OnClickListener, IBaoCaoView {

    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.ibtn_frag_bao_cao_back) ImageButton ibBack;
    private String mEdong;
    public BaoCaoFragment() {

    }

    private OnFragmentInteractionListener listener;

    public static BaoCaoFragment newInstance(String edong) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EDONG, edong);


        BaoCaoFragment fragment = new BaoCaoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bao_cao, container, false);

        ButterKnife.bind(this, view);
        mEdong = getArguments().getString(KEY_EDONG, Common.TEXT_EMPTY);
        ((MainActivity)this.getContext()).switchNavigationBottomMenu(MainActivity.ID_MENU_BOTTOM.REPORT);
        ibBack.setOnClickListener(this);

        viewPager.setAdapter(new BaoCaoAdapter(this, getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnPayFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ViewGroup viewGroup = (ViewGroup) getView();
        assert viewGroup != null;
        viewGroup.removeAllViewsInLayout();
        View view = onCreateView(getActivity().getLayoutInflater(), viewGroup, null); viewGroup.addView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ibtn_frag_bao_cao_back:
            Common.runAnimationClickViewScale(v, R.anim.scale_view_push, Common.TIME_DELAY_ANIM);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("TAG", "viewPager.getCurrentItem()  = " + viewPager.getCurrentItem() );
                        if(viewPager.getCurrentItem() == 3)
                        {
                            ((BaoCaoLichSuFragment)((BaoCaoAdapter)viewPager.getAdapter()).getFragment(3)).hideLichSu();
                        }

//                        Fragment fragment = MainPageFragment.newInstance(mEdong);
//                        if (fragment != null) {
//                            FragmentTransaction fragmentTransaction = BaoCaoFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
//                            fragmentTransaction.replace(R.id.frameLayout, fragment);
//                            fragmentTransaction.commit();
//                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }, Common.TIME_DELAY_ANIM);
                break;
        }
    }


    @Override
    public void showBackBtn(boolean pshow) {
        ibBack.setVisibility(pshow ? View.VISIBLE : View.GONE);
    }

    public interface OnFragmentInteractionListener {
    }

}
