package com.isoftstone.smartsite.http.patrolplan;

import com.isoftstone.smartsite.http.pageable.PageBean;

import java.util.ArrayList;

/**
 * Created by gone on 2017/11/18.
 */

public class PatrolPlanBeanPage extends PageBean{
    private ArrayList<PatrolPlanBean> content;

    public ArrayList<PatrolPlanBean> getContent() {
        return content;
    }

    public void setContent(ArrayList<PatrolPlanBean> content) {
        this.content = content;
    }
}
