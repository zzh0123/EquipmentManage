package com.equipmentmanage.app.db.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.equipmentmanage.app.bean.TaskBean;

import com.equipmentmanage.app.db.gen.TaskBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig taskBeanDaoConfig;

    private final TaskBeanDao taskBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        taskBeanDaoConfig = daoConfigMap.get(TaskBeanDao.class).clone();
        taskBeanDaoConfig.initIdentityScope(type);

        taskBeanDao = new TaskBeanDao(taskBeanDaoConfig, this);

        registerDao(TaskBean.class, taskBeanDao);
    }
    
    public void clear() {
        taskBeanDaoConfig.clearIdentityScope();
    }

    public TaskBeanDao getTaskBeanDao() {
        return taskBeanDao;
    }

}