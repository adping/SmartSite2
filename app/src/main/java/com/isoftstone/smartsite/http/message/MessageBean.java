package com.isoftstone.smartsite.http.message;

/**
 * Created by gone on 2017/10/29.
 */

public class MessageBean {

    private  String infoId;
    private  String title;
    private  String content ;
    private  String turnTo;
    private  int status;
    private  String updateTime;
    private  String userId;
    private  InfoType infoType;

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTurnTo() {
        return turnTo;
    }

    public void setTurnTo(String turnTo) {
        this.turnTo = turnTo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public InfoType getInfoType() {
        return infoType;
    }

    public void setInfoType(InfoType infoType) {
        this.infoType = infoType;
    }

    public static  class  InfoType{
        private String infoTypeId;
        private String infoTypeName;
        private String infoTypeCode;
        private String level;
        private String hasChild;
        private String infoTypeParentCode;
        private String searchCode;
        private String filterTime;
        private String thresholdSet;

        public String getInfoTypeId() {
            return infoTypeId;
        }

        public void setInfoTypeId(String infoTypeId) {
            this.infoTypeId = infoTypeId;
        }

        public String getInfoTypeName() {
            return infoTypeName;
        }

        public void setInfoTypeName(String infoTypeName) {
            this.infoTypeName = infoTypeName;
        }

        public String getInfoTypeCode() {
            return infoTypeCode;
        }

        public void setInfoTypeCode(String infoTypeCode) {
            this.infoTypeCode = infoTypeCode;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getHasChild() {
            return hasChild;
        }

        public void setHasChild(String hasChild) {
            this.hasChild = hasChild;
        }

        public String getInfoTypeParentCode() {
            return infoTypeParentCode;
        }

        public void setInfoTypeParentCode(String infoTypeParentCode) {
            this.infoTypeParentCode = infoTypeParentCode;
        }

        public String getSearchCode() {
            return searchCode;
        }

        public void setSearchCode(String searchCode) {
            this.searchCode = searchCode;
        }

        public String getFilterTime() {
            return filterTime;
        }

        public void setFilterTime(String filterTime) {
            this.filterTime = filterTime;
        }

        public String getThresholdSet() {
            return thresholdSet;
        }

        public void setThresholdSet(String thresholdSet) {
            this.thresholdSet = thresholdSet;
        }
    }
}
