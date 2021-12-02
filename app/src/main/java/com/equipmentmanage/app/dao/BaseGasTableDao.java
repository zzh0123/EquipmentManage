package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.GasTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23
 */
@Dao
public interface BaseGasTableDao {
    @Query("SELECT * FROM tb_base_gas")
    List<GasTableBean> getAll();

    @Query("SELECT * FROM tb_base_gas WHERE id IN (:id)")
    List<GasTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_gas WHERE id ==(:id)")
    GasTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GasTableBean> gasTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(GasTableBean gasTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(GasTableBean gasTableBean);

    @Delete
    void delete(GasTableBean gasTableBean);

    @Query("DELETE FROM tb_base_gas")
    void deleteAll();
}
