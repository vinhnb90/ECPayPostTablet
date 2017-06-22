package views.ecpay.com.postabletecpay.view.TrangChu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.model.adapter.CustomerAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Customer;
import views.ecpay.com.postabletecpay.view.Util.ResizeAnimation;

import static views.ecpay.com.postabletecpay.util.commons.Common.KEY_EDONG;

/**
 * Created by MyPC on 21/06/2017.
 */

public class SearchCustomerFragment extends Fragment implements ISearchCustomer, View.OnClickListener {





    @Nullable
    @BindView(R.id.recycle_list_customer)
    RecyclerView rvKH;

    private String eDong;
    private Unbinder unbinder;

    private boolean isCurrentExpand;

    public static SearchCustomerFragment newInstance(String eDong) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_EDONG, eDong);


        SearchCustomerFragment fragment = new SearchCustomerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @BindView(R.id.layout_timkiem_nangcao)
    LinearLayout layout_timkiem_nangcao;
    @Nullable
    @BindView(R.id.btnExpand)
    ImageButton btnExpand;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_customer, container, false);
        unbinder = ButterKnife.bind(this, view);

        eDong = getArguments().getString(KEY_EDONG, Common.TEXT_EMPTY);


        btnExpand.setOnClickListener(this);

        isCurrentExpand = false;
        btnExpand.setRotation(180F);

        layout_timkiem_nangcao.setVisibility(View.GONE);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvKH.setLayoutManager(layoutManager);

        List<Customer> lst = new ArrayList<>();
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());
        lst.add(new Customer());

        rvKH.setAdapter(new CustomerAdapter(lst));
        rvKH.invalidate();


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnExpand) {
            //layout_timkiem_nangcao.setVisibility(layout_timkiem_nangcao.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

            float deg = btnExpand.getRotation() + 180F;
            btnExpand.animate().rotation(deg).setDuration(1);

            isCurrentExpand = !isCurrentExpand;
            layout_timkiem_nangcao.setVisibility(isCurrentExpand ? View.VISIBLE : View.GONE);
            return;
        }
    }
}
