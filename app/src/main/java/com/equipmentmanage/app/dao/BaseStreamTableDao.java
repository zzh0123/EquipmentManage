package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.BaseStreamTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23
 */
@Dao
public interface BaseStreamTableDao {
    @Query("SELECT * FROM tb_base_stream")
    List<BaseStreamTableBean> getAll();

    @Query("SELECT * FROM tb_base_stream WHERE id IN (:id)")
    List<BaseStreamTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_stream WHERE id ==(:id)")
    BaseStreamTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BaseStreamTableBean> baseStreamTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(BaseStreamTableBean baseStreamTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BaseStreamTableBean baseStreamTableBean);

    @Delete
    void delete(BaseStreamTableBean baseStreamTableBean);

    @Query("DELETE FROM tb_base_stream")
    int deleteAll();
}
