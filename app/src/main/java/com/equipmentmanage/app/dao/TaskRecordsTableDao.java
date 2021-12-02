package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.TaskRecordsTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/11/06
 */
@Dao
public interface TaskRecordsTableDao {
    @Query("SELECT * FROM tb_base_task_records")
    List<TaskRecordsTableBean> getAll();

    @Query("SELECT * FROM tb_base_task_records WHERE id IN (:id)")
    List<TaskRecordsTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_task_records WHERE id ==(:id)")
    TaskRecordsTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TaskRecordsTableBean> taskRecordsTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TaskRecordsTableBean taskRecordsTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(TaskRecordsTableBean taskRecordsTableBean);

    @Delete
    void delete(TaskRecordsTableBean taskRecordsTableBean);

    @Query("DELETE FROM tb_base_task_records")
    void deleteAll();
}
