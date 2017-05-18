package views.ecpay.com.postabletecpay.view.BaoCao;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import views.ecpay.com.postabletecpay.R;

/**
 * Created by macbook on 4/28/17.
 */

public class BaoCaoFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.ibBack) ImageButton ibBack;

    public BaoCaoFragment() {

    }

    private OnFragmentInteractionListener listener;

    public static BaoCaoFragment newInstance() {
        return new BaoCaoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bao_cao, container, false);

        ButterKnife.bind(this, view);

        ibBack.setOnClickListener(this);

        viewPager.setAdapter(new BaoCaoAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnFragmentInteractionListener {
    }

}
