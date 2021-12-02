package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.ThresholdTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23
 */
@Dao
public interface BaseThresholdTableDao {
    @Query("SELECT * FROM tb_base_threshold")
    List<ThresholdTableBean> getAll();

    @Query("SELECT * FROM tb_base_threshold WHERE id IN (:id)")
    List<ThresholdTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_threshold WHERE id ==(:id)")
    ThresholdTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ThresholdTableBean> thresholdTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ThresholdTableBean thresholdTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ThresholdTableBean thresholdTableBean);

    @Delete
    void delete(ThresholdTableBean thresholdTableBean);

    @Query("DELETE FROM tb_base_threshold")
    void deleteAll();
}
