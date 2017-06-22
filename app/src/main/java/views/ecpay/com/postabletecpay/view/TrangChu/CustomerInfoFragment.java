package views.ecpay.com.postabletecpay.view.TrangChu;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import views.ecpay.com.postabletecpay.R;

/**
 * Created by duydatpham on 6/23/17.
 */

public class CustomerInfoFragment extends Fragment implements ICustomerInfoView
{


    public static CustomerInfoFragment newInstance() {
        return new CustomerInfoFragment();
    }

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_info, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public Context getContextView() {
        return this.getContext();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
