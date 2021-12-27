package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.BaseMediumStateTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/26
 */
@Dao
public interface BaseMediumStateTableDao {
    @Query("SELECT * FROM tb_base_medium_state")
    List<BaseMediumStateTableBean> getAll();

    @Query("SELECT * FROM tb_base_medium_state WHERE id IN (:id)")
    List<BaseMediumStateTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_medium_state WHERE id ==(:id)")
    BaseMediumStateTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BaseMediumStateTableBean> baseMediumStateTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BaseMediumStateTableBean baseMediumStateTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BaseMediumStateTableBean baseMediumStateTableBean);

    @Delete
    void delete(BaseMediumStateTableBean baseMediumStateTableBean);

    @Query("DELETE FROM tb_base_medium_state")
    void deleteAll();
}
