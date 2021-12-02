package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.BaseComponentTypeTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/30
 */
@Dao
public interface BaseComponentTypeTableDao {
    @Query("SELECT * FROM tb_base_component_type")
    List<BaseComponentTypeTableBean> getAll();

    @Query("SELECT * FROM tb_base_component_type WHERE id IN (:id)")
    List<BaseComponentTypeTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_component_type WHERE id ==(:id)")
    BaseComponentTypeTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BaseComponentTypeTableBean> baseComponentTypeTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BaseComponentTypeTableBean baseComponentTypeTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BaseComponentTypeTableBean baseComponentTypeTableBean);

    @Delete
    void delete(BaseComponentTypeTableBean baseComponentTypeTableBean);

    @Query("DELETE FROM tb_base_component_type")
    void deleteAll();
}
