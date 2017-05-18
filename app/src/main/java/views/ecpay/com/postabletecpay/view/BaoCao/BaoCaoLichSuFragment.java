package views.ecpay.com.postabletecpay.view.BaoCao;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import views.ecpay.com.postabletecpay.R;

/**
 * Created by macbook on 4/30/17.
 */

public class BaoCaoLichSuFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.rbMaKH) RadioButton rbMaKH;
    @BindView(R.id.rbTenKH) RadioButton rbTenKH;
    @BindView(R.id.etSearch) EditText etSearch;
    @BindView(R.id.btTimKiem) Button btTimKiem;
    @BindView(R.id.rvDanhSach) RecyclerView rvDanhSach;
    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvMaKH) TextView tvMaKH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_baocao_lichsu, container, false);
        ButterKnife.bind(this, view);

        btTimKiem.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
