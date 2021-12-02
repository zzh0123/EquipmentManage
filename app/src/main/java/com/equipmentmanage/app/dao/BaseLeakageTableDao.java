package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.LeakageTableBean;
import com.equipmentmanage.app.bean.LeakageTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23
 */
@Dao
public interface BaseLeakageTableDao {
    @Query("SELECT * FROM tb_base_leakage")
    List<LeakageTableBean> getAll();

    @Query("SELECT * FROM tb_base_leakage WHERE id IN (:id)")
    List<LeakageTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_leakage WHERE id ==(:id)")
    LeakageTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LeakageTableBean> leakageTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LeakageTableBean leakageTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(LeakageTableBean leakageTableBean);

    @Delete
    void delete(LeakageTableBean leakageTableBean);

    @Query("DELETE FROM tb_base_leakage")
    void deleteAll();
}
