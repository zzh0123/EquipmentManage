package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.DeviceTableBean;
import com.equipmentmanage.app.bean.DeviceTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23
 */
@Dao
public interface DeviceTableDao {
    @Query("SELECT * FROM tb_device")
    List<DeviceTableBean> getAll();

    @Query("SELECT * FROM tb_device WHERE id IN (:id)")
    List<DeviceTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_device WHERE id ==(:id)")
    DeviceTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DeviceTableBean> deviceTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DeviceTableBean deviceTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(DeviceTableBean deviceTableBean);

    @Delete
    void delete(DeviceTableBean deviceTableBean);

    @Query("DELETE FROM tb_device")
    void deleteAll();
}
