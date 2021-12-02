package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.BaseAreaTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23
 */
@Dao
public interface BaseAreaTableDao {
    @Query("SELECT * FROM tb_base_area")
    List<BaseAreaTableBean> getAll();

    @Query("SELECT * FROM tb_base_area WHERE id IN (:id)")
    List<BaseAreaTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_area WHERE id ==(:id)")
    BaseAreaTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BaseAreaTableBean> baseAreaTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(BaseAreaTableBean baseAreaTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BaseAreaTableBean baseAreaTableBean);

    @Delete
    void delete(BaseAreaTableBean baseAreaTableBean);

    @Query("DELETE FROM tb_base_area")
    int deleteAll();
}
