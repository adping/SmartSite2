package com.isoftstone.smartsite.model.inspectplan.data;

import android.net.Uri;
import android.view.View;

/**
 * Created by Administrator on 2017-11-24.
 */

public class InspectorData {
    private   String  sort="";                //名字对应的字母
    private  String  contactName = "";      //联系人姓名
    private Uri iconUri = null;             //头像地址
    private  boolean selected=false;     //是否被选中
    private  int visible= View.VISIBLE;      //是否显示字母    View.GONE  隐藏      View.INVISIBLE  不可见，但仍然占据空间     View.VISIBLE   可见

    public String getSort(){
        return  sort;
    }

    public void setSort(String sort){
        this.sort = sort;
    }

    public String getContactName(){
        return  contactName;
    }

    public void setContactName(String contactName){
        this.contactName = contactName;
    }

    public Uri getIconUri(){
        return iconUri;
    }

    public void setIconUri(Uri iconUri){
        this.iconUri = iconUri;
    }

    public boolean getSelected(){
        return selected;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
    }

    public int getVisible(){
        return visible;
    }

    public void setVisible(int visible){
        this.visible = visible;
    }
}
