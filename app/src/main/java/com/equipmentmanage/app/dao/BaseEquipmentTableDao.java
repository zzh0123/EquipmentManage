package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.BaseEquipmentTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23
 */
@Dao
public interface BaseEquipmentTableDao {
    @Query("SELECT * FROM tb_base_equipment")
    List<BaseEquipmentTableBean> getAll();

    @Query("SELECT * FROM tb_base_equipment WHERE id IN (:id)")
    List<BaseEquipmentTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_equipment WHERE id ==(:id)")
    BaseEquipmentTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BaseEquipmentTableBean> baseEquipmentTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(BaseEquipmentTableBean baseEquipmentTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BaseEquipmentTableBean baseEquipmentTableBean);

    @Delete
    void delete(BaseEquipmentTableBean baseEquipmentTableBean);

    @Query("DELETE FROM tb_base_equipment")
    int deleteAll();
}
