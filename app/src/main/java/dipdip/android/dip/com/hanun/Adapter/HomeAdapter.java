package dipdip.android.dip.com.hanun.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import dipdip.android.dip.com.hanun.Model.HomeModel;
import dipdip.android.dip.com.hanun.R;

public class HomeAdapter extends RecyclerView.ViewHolder {

    public TextView tvNama;
    public TextView tvusia;
    public TextView tvWeb;
    public TextView btnOpen;

    public HomeAdapter(View itemView) {
        super(itemView);
        tvNama = itemView.findViewById(R.id.tv_nama);
        tvusia = itemView.findViewById(R.id.tv_usia);
     }

    public void bindToHome(HomeModel homeModel, View.OnClickListener onClickListener){
        tvNama.setText(homeModel.nama);
        tvusia.setText(homeModel.usia);
        tvWeb.setText(homeModel.token);
        btnOpen.setOnClickListener(onClickListener);
    }
}