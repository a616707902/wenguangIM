package com.wenguang.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.wenguang.chat.R;
import com.wenguang.chat.utils.common.SortModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * 作者：陈攀
 * 邮件：616707902@qq.com
 * 创建时间：2016/12/3.
 * 描述:
 */
public class ReContactAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> mList;
    private List<SortModel> mSelectedList;
    private Context mContext;
    LayoutInflater mInflater;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> isSelected;

    public ReContactAdapter(Context mContext, List<SortModel> list) {
        this.mContext = mContext;
        mSelectedList = new ArrayList<SortModel>();
        isSelected = new HashMap<Integer, Boolean>();
        if (list == null) {
            this.mList = new ArrayList<SortModel>();
        } else {
            this.mList = list;
        }
    }
    // 初始化isSelected的数据
    private void initDate(){
        for(int i=0; i<mList.size();i++) {
            getIsSelected().put(i,false);
        }
    }
    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<SortModel> list) {
        if (list == null) {
            this.mList = new ArrayList<SortModel>();
        } else {
            this.mList = list;
        }
        initDate();
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final SortModel mContent = mList.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_recontact, null);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.item_name);
            viewHolder.cbChecked = (CheckBox) view.findViewById(R.id.cbChecked);
            viewHolder.tvNumber = (TextView) view.findViewById(R.id.item_phone);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.tvTitle.setText(this.mList.get(position).name);
        viewHolder.tvNumber.setText(this.mList.get(position).number);
       // viewHolder.cbChecked.setChecked(isSelected(mContent));
        // 根据isSelected来设置checkbox的选中状况
        viewHolder.cbChecked.setChecked(getIsSelected().get(position));
        return view;

    }

    public static class ViewHolder {
        public TextView tvTitle;
        public TextView tvNumber;
        public CheckBox cbChecked;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return mList.get(position).sortLetters.charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mList.get(i).sortLetters;
            char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    private boolean isSelected(SortModel model) {
        return mSelectedList.contains(model);
        //return true;
    }

//    public void toggleChecked(int position) {
//        if (isSelected(mList.get(position))) {
//            removeSelected(position);
//        } else {
//            setSelected(position);
//        }
//
//    }

//    private void setSelected(int position) {
//        if (!mSelectedList.contains(mList.get(position))) {
//            mSelectedList.add(mList.get(position));
//        }
//    }
//
//    private void removeSelected(int position) {
//        if (mSelectedList.contains(mList.get(position))) {
//            mSelectedList.remove(mList.get(position));
//        }
//    }

    public  List<SortModel> getSelectedList() {
        mSelectedList.clear();
        for (int i=0;i<mList.size();i++){
            if (getIsSelected().get(i)){
                mSelectedList.add(mList.get(i));
            }
        }
        return mSelectedList;
    }

    public void selectAll(){
      //  mSelectedList.clear();
        if (mList!=null){
            for (int i=0;i<mList.size();i++){
                getIsSelected().put(i, true);
                notifyDataSetChanged();
            }
        }
    }
    public void ReselectAll(){
        //  mSelectedList.clear();
        if (mList!=null){
            for (int i=0;i<mList.size();i++){
                getIsSelected().put(i, false);
                notifyDataSetChanged();
            }
        }
    }
    public static HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        ReContactAdapter.isSelected = isSelected;
    }
}

