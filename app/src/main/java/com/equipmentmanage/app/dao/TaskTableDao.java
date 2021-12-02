package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.BaseChemicalTableBean;
import com.equipmentmanage.app.bean.TaskTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/22 21:21
 */
@Dao
public interface TaskTableDao {
    @Query("SELECT * FROM tb_task")
    List<TaskTableBean> getAll();

    @Query("SELECT * FROM tb_task WHERE id IN (:id)")
    List<TaskTableBean> loadAllByIds(int[] id);

    @Query("SELECT * FROM tb_task WHERE id ==(:id)")
    TaskTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TaskTableBean> taskTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TaskTableBean taskTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(TaskTableBean taskTableBean);

    @Delete
    void delete(TaskTableBean taskTableBean);

    @Query("DELETE FROM tb_task")
    int deleteAll();

    @Query("DELETE FROM tb_task WHERE id ==(:id)")
    int deleteAllByUser(String id);
}
