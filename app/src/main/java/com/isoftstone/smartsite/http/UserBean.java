package com.isoftstone.smartsite.http;

/**
 * Created by gone on 2017/11/1.
 */

public class UserBean {
    private Long id;       //	用户ID
    private String account; //用户账号
    private String name;     //用户名称
    private String password;   //	密码
    private String imageData;   //用户头像(保存路径)
    private String telephone;   //	String	电话
    private String fax;         //	传真
    private String email;     //	邮件
    private String address;       //	地址
    private String description;   //	描述
    private String departmentId;  //	机构id
    private String employeeCode;  //	员工编号
    private String createTime;   //	Date(yyyy-MM-dd HH:mm:ss)	创建时间
    private Long creator;        //创建人
    private int accountType; //	 账号类型
    private int resetPwd;//	 重置密码
    private int locked;//	是否锁定（0未锁定，1锁定）
    private int delFlag;//	删除标记
    private int sex	;//	    性别（0：男，1：女）
    private String registerId;//	极光推送Id
    //roles	Set<Role>	关联的角色
    //archs	Set<Arch>	关联的区域


    private Permission mPermission = null;//用户权限信息

    public Permission getmPermission() {
        return mPermission;
    }

    public void setmPermission(Permission mPermission) {
        this.mPermission = mPermission;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getResetPwd() {
        return resetPwd;
    }

    public void setResetPwd(int resetPwd) {
        this.resetPwd = resetPwd;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", imageData='" + imageData + '\'' +
                ", telephone='" + telephone + '\'' +
                ", fax='" + fax + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", employeeCode='" + employeeCode + '\'' +
                ", createTime='" + createTime + '\'' +
                ", creator=" + creator +
                ", accountType=" + accountType +
                ", resetPwd=" + resetPwd +
                ", locked=" + locked +
                ", delFlag=" + delFlag +
                ", sex=" + sex +
                ", registerId='" + registerId + '\'' +
                '}';
    }


    public  static class Permission{
        private boolean B_USER_ADD = false;
        private boolean B_PATROL_ACCEPT = false;
        private boolean B_ARCH_ADD = false;
        private boolean ENVIRMENT_VIEW_DATAQUERY = false;
        private boolean MENU_2 = false;
        private boolean VS_MAP = false;
        private boolean B_ARCH_DELETE = false;
        private boolean MENU_1 = false;
        private boolean B_USER_UPDATE = false;
        private boolean B_USER_RESETPWD = false;
        private boolean ENVIRMENT_VIEW_MAP = false;
        private boolean B_PATROL_REPLY = false;
        private boolean B_USER_UNLOCK = false;
        private boolean B_USER_DELETE = false;
        private boolean B_DEVICE_ADD = false;
        private boolean B_ROLE_UPDATE = false;
        private boolean M_SYSTEMCONFIG = false;
        private boolean MANUAL_RECONGIZED = false;
        private boolean M_PATROL = false;
        private boolean B_ARCH_UPDATE = false;
        private boolean M_LOG = false;
        private boolean VS_RECORD = false;
        private boolean B_ROLE_DELETE = false;
        private boolean M_PLAN = false;
        private boolean M_DEVICE = false;
        private boolean B_DEVICE_DELETE = false;
        private boolean B_DEVICE_THRESHOLDSET = false;
        private boolean B_DEVICE_TONGBU = false;
        private boolean MESSAGE_CENTER = false;
        private boolean B_PATROL_ADD = false;
        private boolean B_DEVICE_UPDATE = false;
        private boolean M_PATROL_REPORT = false;    //巡查报告权限
        private boolean M_PATROL_ACCEPT = false;    //验收报告权限
        private boolean MUCKCAR_TRACK = false;
        private boolean M_MESSAGE_DELETE_ROLE = false;
        private boolean B_PATROL_VISIT = false;
        private boolean MUCKCAR_MONITOR = false;
        private boolean M_ROLE = false;
        private boolean M_MESSAGE = false;
        private boolean ENVIRMENT_VIEW = false;
        private boolean M_ARCH = false;
        private boolean M_MESSAGE_ADD_ROLE = false;
        private boolean M_USER = false;
        private boolean B_ROLE_ADD = false;
        private boolean VS = false;

        public boolean isB_USER_ADD() {
            return B_USER_ADD;
        }

        public void setB_USER_ADD(boolean b_USER_ADD) {
            B_USER_ADD = b_USER_ADD;
        }

        public boolean isB_PATROL_ACCEPT() {
            return B_PATROL_ACCEPT;
        }

        public void setB_PATROL_ACCEPT(boolean b_PATROL_ACCEPT) {
            B_PATROL_ACCEPT = b_PATROL_ACCEPT;
        }

        public boolean isB_ARCH_ADD() {
            return B_ARCH_ADD;
        }

        public void setB_ARCH_ADD(boolean b_ARCH_ADD) {
            B_ARCH_ADD = b_ARCH_ADD;
        }

        public boolean isENVIRMENT_VIEW_DATAQUERY() {
            return ENVIRMENT_VIEW_DATAQUERY;
        }

        public void setENVIRMENT_VIEW_DATAQUERY(boolean ENVIRMENT_VIEW_DATAQUERY) {
            this.ENVIRMENT_VIEW_DATAQUERY = ENVIRMENT_VIEW_DATAQUERY;
        }

        public boolean isMENU_2() {
            return MENU_2;
        }

        public void setMENU_2(boolean MENU_2) {
            this.MENU_2 = MENU_2;
        }

        public boolean isVS_MAP() {
            return VS_MAP;
        }

        public void setVS_MAP(boolean VS_MAP) {
            this.VS_MAP = VS_MAP;
        }

        public boolean isB_ARCH_DELETE() {
            return B_ARCH_DELETE;
        }

        public void setB_ARCH_DELETE(boolean b_ARCH_DELETE) {
            B_ARCH_DELETE = b_ARCH_DELETE;
        }

        public boolean isMENU_1() {
            return MENU_1;
        }

        public void setMENU_1(boolean MENU_1) {
            this.MENU_1 = MENU_1;
        }

        public boolean isB_USER_UPDATE() {
            return B_USER_UPDATE;
        }

        public void setB_USER_UPDATE(boolean b_USER_UPDATE) {
            B_USER_UPDATE = b_USER_UPDATE;
        }

        public boolean isB_USER_RESETPWD() {
            return B_USER_RESETPWD;
        }

        public void setB_USER_RESETPWD(boolean b_USER_RESETPWD) {
            B_USER_RESETPWD = b_USER_RESETPWD;
        }

        public boolean isENVIRMENT_VIEW_MAP() {
            return ENVIRMENT_VIEW_MAP;
        }

        public void setENVIRMENT_VIEW_MAP(boolean ENVIRMENT_VIEW_MAP) {
            this.ENVIRMENT_VIEW_MAP = ENVIRMENT_VIEW_MAP;
        }

        public boolean isB_PATROL_REPLY() {
            return B_PATROL_REPLY;
        }

        public void setB_PATROL_REPLY(boolean b_PATROL_REPLY) {
            B_PATROL_REPLY = b_PATROL_REPLY;
        }

        public boolean isB_USER_UNLOCK() {
            return B_USER_UNLOCK;
        }

        public void setB_USER_UNLOCK(boolean b_USER_UNLOCK) {
            B_USER_UNLOCK = b_USER_UNLOCK;
        }

        public boolean isB_USER_DELETE() {
            return B_USER_DELETE;
        }

        public void setB_USER_DELETE(boolean b_USER_DELETE) {
            B_USER_DELETE = b_USER_DELETE;
        }

        public boolean isB_DEVICE_ADD() {
            return B_DEVICE_ADD;
        }

        public void setB_DEVICE_ADD(boolean b_DEVICE_ADD) {
            B_DEVICE_ADD = b_DEVICE_ADD;
        }

        public boolean isB_ROLE_UPDATE() {
            return B_ROLE_UPDATE;
        }

        public void setB_ROLE_UPDATE(boolean b_ROLE_UPDATE) {
            B_ROLE_UPDATE = b_ROLE_UPDATE;
        }

        public boolean isM_SYSTEMCONFIG() {
            return M_SYSTEMCONFIG;
        }

        public void setM_SYSTEMCONFIG(boolean m_SYSTEMCONFIG) {
            M_SYSTEMCONFIG = m_SYSTEMCONFIG;
        }

        public boolean isMANUAL_RECONGIZED() {
            return MANUAL_RECONGIZED;
        }

        public void setMANUAL_RECONGIZED(boolean MANUAL_RECONGIZED) {
            this.MANUAL_RECONGIZED = MANUAL_RECONGIZED;
        }

        public boolean isM_PATROL() {
            return M_PATROL;
        }

        public void setM_PATROL(boolean m_PATROL) {
            M_PATROL = m_PATROL;
        }

        public boolean isB_ARCH_UPDATE() {
            return B_ARCH_UPDATE;
        }

        public void setB_ARCH_UPDATE(boolean b_ARCH_UPDATE) {
            B_ARCH_UPDATE = b_ARCH_UPDATE;
        }

        public boolean isM_LOG() {
            return M_LOG;
        }

        public void setM_LOG(boolean m_LOG) {
            M_LOG = m_LOG;
        }

        public boolean isVS_RECORD() {
            return VS_RECORD;
        }

        public void setVS_RECORD(boolean VS_RECORD) {
            this.VS_RECORD = VS_RECORD;
        }

        public boolean isB_ROLE_DELETE() {
            return B_ROLE_DELETE;
        }

        public void setB_ROLE_DELETE(boolean b_ROLE_DELETE) {
            B_ROLE_DELETE = b_ROLE_DELETE;
        }

        public boolean isM_PLAN() {
            return M_PLAN;
        }

        public void setM_PLAN(boolean m_PLAN) {
            M_PLAN = m_PLAN;
        }

        public boolean isM_DEVICE() {
            return M_DEVICE;
        }

        public void setM_DEVICE(boolean m_DEVICE) {
            M_DEVICE = m_DEVICE;
        }

        public boolean isB_DEVICE_DELETE() {
            return B_DEVICE_DELETE;
        }

        public void setB_DEVICE_DELETE(boolean b_DEVICE_DELETE) {
            B_DEVICE_DELETE = b_DEVICE_DELETE;
        }

        public boolean isB_DEVICE_THRESHOLDSET() {
            return B_DEVICE_THRESHOLDSET;
        }

        public void setB_DEVICE_THRESHOLDSET(boolean b_DEVICE_THRESHOLDSET) {
            B_DEVICE_THRESHOLDSET = b_DEVICE_THRESHOLDSET;
        }

        public boolean isB_DEVICE_TONGBU() {
            return B_DEVICE_TONGBU;
        }

        public void setB_DEVICE_TONGBU(boolean b_DEVICE_TONGBU) {
            B_DEVICE_TONGBU = b_DEVICE_TONGBU;
        }

        public boolean isMESSAGE_CENTER() {
            return MESSAGE_CENTER;
        }

        public void setMESSAGE_CENTER(boolean MESSAGE_CENTER) {
            this.MESSAGE_CENTER = MESSAGE_CENTER;
        }

        public boolean isB_PATROL_ADD() {
            return B_PATROL_ADD;
        }

        public void setB_PATROL_ADD(boolean b_PATROL_ADD) {
            B_PATROL_ADD = b_PATROL_ADD;
        }

        public boolean isB_DEVICE_UPDATE() {
            return B_DEVICE_UPDATE;
        }

        public void setB_DEVICE_UPDATE(boolean b_DEVICE_UPDATE) {
            B_DEVICE_UPDATE = b_DEVICE_UPDATE;
        }

        public boolean isM_PATROL_REPORT() {
            return M_PATROL_REPORT;
        }

        public void setM_PATROL_REPORT(boolean m_PATROL_REPORT) {
            M_PATROL_REPORT = m_PATROL_REPORT;
        }

        public boolean isM_PATROL_ACCEPT() {
            return M_PATROL_ACCEPT;
        }

        public void setM_PATROL_ACCEPT(boolean m_PATROL_ACCEPT) {
            M_PATROL_ACCEPT = m_PATROL_ACCEPT;
        }

        public boolean isMUCKCAR_TRACK() {
            return MUCKCAR_TRACK;
        }

        public void setMUCKCAR_TRACK(boolean MUCKCAR_TRACK) {
            this.MUCKCAR_TRACK = MUCKCAR_TRACK;
        }

        public boolean isM_MESSAGE_DELETE_ROLE() {
            return M_MESSAGE_DELETE_ROLE;
        }

        public void setM_MESSAGE_DELETE_ROLE(boolean m_MESSAGE_DELETE_ROLE) {
            M_MESSAGE_DELETE_ROLE = m_MESSAGE_DELETE_ROLE;
        }

        public boolean isB_PATROL_VISIT() {
            return B_PATROL_VISIT;
        }

        public void setB_PATROL_VISIT(boolean b_PATROL_VISIT) {
            B_PATROL_VISIT = b_PATROL_VISIT;
        }

        public boolean isMUCKCAR_MONITOR() {
            return MUCKCAR_MONITOR;
        }

        public void setMUCKCAR_MONITOR(boolean MUCKCAR_MONITOR) {
            this.MUCKCAR_MONITOR = MUCKCAR_MONITOR;
        }

        public boolean isM_ROLE() {
            return M_ROLE;
        }

        public void setM_ROLE(boolean m_ROLE) {
            M_ROLE = m_ROLE;
        }

        public boolean isM_MESSAGE() {
            return M_MESSAGE;
        }

        public void setM_MESSAGE(boolean m_MESSAGE) {
            M_MESSAGE = m_MESSAGE;
        }

        public boolean isENVIRMENT_VIEW() {
            return ENVIRMENT_VIEW;
        }

        public void setENVIRMENT_VIEW(boolean ENVIRMENT_VIEW) {
            this.ENVIRMENT_VIEW = ENVIRMENT_VIEW;
        }

        public boolean isM_ARCH() {
            return M_ARCH;
        }

        public void setM_ARCH(boolean m_ARCH) {
            M_ARCH = m_ARCH;
        }

        public boolean isM_MESSAGE_ADD_ROLE() {
            return M_MESSAGE_ADD_ROLE;
        }

        public void setM_MESSAGE_ADD_ROLE(boolean m_MESSAGE_ADD_ROLE) {
            M_MESSAGE_ADD_ROLE = m_MESSAGE_ADD_ROLE;
        }

        public boolean isM_USER() {
            return M_USER;
        }

        public void setM_USER(boolean m_USER) {
            M_USER = m_USER;
        }

        public boolean isB_ROLE_ADD() {
            return B_ROLE_ADD;
        }

        public void setB_ROLE_ADD(boolean b_ROLE_ADD) {
            B_ROLE_ADD = b_ROLE_ADD;
        }

        public boolean isVS() {
            return VS;
        }

        public void setVS(boolean VS) {
            this.VS = VS;
        }
    }
}
