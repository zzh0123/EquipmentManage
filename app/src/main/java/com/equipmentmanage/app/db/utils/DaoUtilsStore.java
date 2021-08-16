package com.equipmentmanage.app.db.utils;


import com.equipmentmanage.app.bean.TaskBean;
import com.equipmentmanage.app.db.gen.TaskBeanDao;

/**
 * 存放DaoUtils
 */
public class DaoUtilsStore {
    private volatile static DaoUtilsStore instance = new DaoUtilsStore();
    private CommonDaoUtils<TaskBean> mTaskBeanDaoUtils;

    public static DaoUtilsStore getInstance() {
        return instance;
    }

    private DaoUtilsStore() {
        DaoManager mManager = DaoManager.getInstance();
        TaskBeanDao _TaskBeanDao = mManager.getDaoSession().getTaskBeanDao();
        mTaskBeanDaoUtils = new CommonDaoUtils<TaskBean>(TaskBean.class, _TaskBeanDao);
    }

    public CommonDaoUtils<TaskBean> getUserDaoUtils() {
        return mTaskBeanDaoUtils;
    }

}