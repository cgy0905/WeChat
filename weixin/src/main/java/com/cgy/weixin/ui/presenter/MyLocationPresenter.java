package com.cgy.weixin.ui.presenter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.cgy.weixin.R;
import com.cgy.weixin.model.data.LocationData;
import com.cgy.weixin.ui.base.BaseActivity;
import com.cgy.weixin.ui.base.BasePresenter;
import com.cgy.weixin.ui.view.IMyLocationView;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cgy
 * 2018/6/13  17:41
 */
public class MyLocationPresenter extends BasePresenter<IMyLocationView>{

    private List<Geo2AddressResultObject.ReverseAddressResult.Poi> mData = new ArrayList<>();
    private int mSelectedPois = 0;
    private LQRAdapterForRecyclerView<Geo2AddressResultObject.ReverseAddressResult.Poi> mAdapter;
    public MyLocationPresenter(BaseActivity context) {
        super(context);
    }

    public void loadData(Geo2AddressResultObject obj) {
        mData.clear();
        mData.addAll(obj.result.pois);
        setAdapter();
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new LQRAdapterForRecyclerView<Geo2AddressResultObject.ReverseAddressResult.Poi>(mContext, mData, R.layout.item_location_poi) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, Geo2AddressResultObject.ReverseAddressResult.Poi item, int position) {
                    helper.setText(R.id.tvTitle, item.title).setText(R.id.tvDesc, item.address)
                            .setViewVisibility(R.id.ivSelected, mSelectedPois == position ? View.VISIBLE : View.GONE);
                }
            };
            getView().getRvPOI().setAdapter(mAdapter);
            mAdapter.setOnItemClickListener((helper, parent, itemView, position) -> {
                mSelectedPois = position;
                setAdapter();
            });
        } else {
            mAdapter.notifyDataSetChangedWrapper();
        }
    }

    public void sendLocation() {
        if (mData != null && mData.size() > mSelectedPois) {
            Geo2AddressResultObject.ReverseAddressResult.Poi poi = mData.get(mSelectedPois);
            Intent data = new Intent();
            LocationData locationData = new LocationData(poi.location.lat, poi.location.lng, poi.title, getMapUrl(poi.location.lat, poi.location.lng));
            data.putExtra("location", locationData);
            mContext.setResult(Activity.RESULT_OK, data);
            mContext.finish();

        }
    }

    //    获取位置静态图
    //    http://apis.map.qq.com/ws/staticmap/v2/?center=39.8802147,116.415794&zoom=10&size=600*300&maptype=landform&markers=size:large|color:0xFFCCFF|label:k|39.8802147,116.415794&key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77
    //    http://st.map.qq.com/api?size=708*270&center=114.215843,22.685120&zoom=17&referer=weixin
    //    http://st.map.qq.com/api?size=708*270&center=116.415794,39.8802147&zoom=17&referer=weixin
    private String getMapUrl(float lat, float lng) {
        String url = "http://st.map.qq.com/api?size=708*270&center=\" + y + \",\" + x + \"&zoom=17&referer=weixin";
        return url;
    }
}
