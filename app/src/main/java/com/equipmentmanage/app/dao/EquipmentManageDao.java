package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.EquipmentManageTableBean;
import com.equipmentmanage.app.bean.TaskBean;
import com.equipmentmanage.app.bean.TaskTableBean;

import java.util.List;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/22 21:36
 */
@Dao
public interface EquipmentManageDao {
    @Query("SELECT * FROM tb_equipment_manage")
    List<EquipmentManageTableBean> getAll();

    @Query("SELECT * FROM tb_equipment_manage WHERE id IN (:id)")
    List<EquipmentManageTableBean> loadAllByIds(int[] id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EquipmentManageTableBean> equipmentManageTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(EquipmentManageTableBean equipmentManageTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(EquipmentManageTableBean EquipmentManageTableBean);

    @Delete
    void delete(EquipmentManageTableBean EquipmentManageTableBean);

    @Query("DELETE FROM tb_equipment_manage")
    void deleteAll();
}
