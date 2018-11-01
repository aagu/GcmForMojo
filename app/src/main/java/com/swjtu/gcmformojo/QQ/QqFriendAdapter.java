package com.swjtu.gcmformojo.QQ;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.swjtu.gcmformojo.R;

import java.util.List;

/**
 * QQ好友数据显示
 * Created by HeiPi on 2017/2/21.
 */

public class QqFriendAdapter extends BaseExpandableListAdapter {

    private Context context;
    public List<QqFriendGroup> group;
    public QqFriendAdapter(Context context, List<QqFriendGroup> group) {
        super();
        this.context = context;
        this.group = group;
    }


    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHold groupHold;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.qq_friend_group_items, null);
            groupHold = new GroupHold();
            groupHold.tvGroupName = convertView.findViewById(R.id.qq_friend_group_name);
            groupHold.tvGroupName.setText(getGroup(groupPosition).toString());// 设置大组成员名称
            groupHold.ivGoToChildIv = convertView.findViewById(R.id.qq_group_indicator);// 是否展开大组的箭头图标

            convertView.setTag(groupHold);
        } else {
            groupHold = (GroupHold)convertView.getTag();
        }

        if (isExpanded) {
            // 大组展开时的箭头图标
            groupHold.ivGoToChildIv.setImageResource(R.drawable.ic_unfold);
        }
        else {
            // 大组合并时的箭头图标
            groupHold.ivGoToChildIv.setImageResource(R.drawable.ic_fold);
        }


        return convertView;
    }

    // 得到大组成员的id
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // 得到大组成员名称
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition).getGroupName();
    }

    // 得到大组成员总数
    public int getGroupCount() {
        return group.size();
    }

    // 得到小组成员的view
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.qq_friend_child_items, null);
        }
        final TextView qqFriendNameView = (TextView) convertView
                .findViewById(R.id.qq_friend_child_name);// 显示好友名
      /*  final TextView title2 = (TextView) convertView
                .findViewById(R.id.id_item);// 显示用户id
        ImageView icon = (ImageView) convertView
                .findViewById(R.id.imageView_item);// 显示用户头像，其实还可以判断是否在线，选择黑白和彩色头像，我这里未处理，没资源，呵呵

        final String name = group.get(groupPosition).getChild(childPosition)
                .getName();
        final String id = group.get(groupPosition).getChild(childPosition)
                .getId()
                + "";
        final int img = group.get(groupPosition).getChild(childPosition)
                .getImg();
                */
        final String name = group.get(groupPosition).getChild(childPosition)
                .get_name();
        final String markname = group.get(groupPosition).getChild(childPosition)
                .get_markname();
        if(markname.equals("null"))
            qqFriendNameView.setText("\t\t"+name);
        else
            qqFriendNameView.setText("\t\t"+markname);

        return convertView;
    }

    // 得到小组成员id
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // 得到小组成员的名称
    public Object getChild(int groupPosition, int childPosition) {
        return group.get(groupPosition).getChild(childPosition);
    }

    // 得到小组成员的数量
    public int getChildrenCount(int groupPosition) {
        return group.get(groupPosition).getChildSize();
    }

    /**
     * Indicates whether the child and group IDs are stable across changes to
     * the underlying data. 表明大組和小组id是否稳定的更改底层数据。
     *
     * @return whether or not the same ID always refers to the same object
     * @see
     */
    public boolean hasStableIds() {
        return true;
    }

    // 得到小组成员是否被选择
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 这个方法是我自定义的，用于下拉刷新好友的方法
     *
     * @param group
     *            传递进来的新数据
     */
    public void updata(List<QqFriendGroup> group) {
        this.group = null;
        this.group = group;
    }

    class GroupHold {
        TextView  tvGroupName;
        ImageView ivGoToChildIv;
    }
}
