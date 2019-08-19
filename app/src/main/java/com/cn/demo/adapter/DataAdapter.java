package com.cn.demo.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.demo.R;
import com.cn.demo.bean.TestBean;


/**
 * Date: 2019/7/19
 * <p>
 * Time: 3:32 PM
 * <p>
 * author: 鹿文龙
 */
public class DataAdapter extends BaseQuickAdapter<TestBean, BaseViewHolder> {

	public DataAdapter() {
		super(R.layout.listitem_layout);
	}

	@Override
	protected void convert(BaseViewHolder holder, TestBean item) {
		holder.setText(R.id.nameTv, item.name);
		holder.setText(R.id.mobileTv, item.mobile);
	}

	public void clear() {
		if (null != mData) {
			mData.clear();
			notifyDataSetChanged();
		}
	}
}
