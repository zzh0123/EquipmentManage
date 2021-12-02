package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.BaseChemicalTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/30
 */
@Dao
public interface BaseChemicalTableDao {
    @Query("SELECT * FROM tb_base_chemical")
    List<BaseChemicalTableBean> getAll();

    @Query("SELECT * FROM tb_base_chemical WHERE id IN (:id)")
    List<BaseChemicalTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_chemical WHERE id ==(:id)")
    BaseChemicalTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BaseChemicalTableBean> baseChemicalTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BaseChemicalTableBean baseChemicalTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BaseChemicalTableBean baseChemicalTableBean);

    @Delete
    void delete(BaseChemicalTableBean baseChemicalTableBean);

    @Query("DELETE FROM tb_base_chemical")
    void deleteAll();
}
