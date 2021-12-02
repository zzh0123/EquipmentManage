package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.InstrumentTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23
 */
@Dao
public interface BaseInstrumentTableDao {
    @Query("SELECT * FROM tb_base_instrument")
    List<InstrumentTableBean> getAll();

    @Query("SELECT * FROM tb_base_instrument WHERE id IN (:id)")
    List<InstrumentTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_instrument WHERE id ==(:id)")
    InstrumentTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<InstrumentTableBean> instrumentTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(InstrumentTableBean instrumentTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(InstrumentTableBean instrumentTableBean);

    @Delete
    void delete(InstrumentTableBean instrumentTableBean);

    @Query("DELETE FROM tb_base_instrument")
    void deleteAll();
}
