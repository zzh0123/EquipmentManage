package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.BaseDeviceTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23
 */
@Dao
public interface BaseDeviceTableDao {
    @Query("SELECT * FROM tb_base_device")
    List<BaseDeviceTableBean> getAll();

    @Query("SELECT * FROM tb_base_device WHERE id IN (:id)")
    List<BaseDeviceTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_device WHERE id ==(:id)")
    BaseDeviceTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BaseDeviceTableBean> baseDeviceTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(BaseDeviceTableBean baseDeviceTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BaseDeviceTableBean baseDeviceTableBean);

    @Delete
    void delete(BaseDeviceTableBean baseDeviceTableBean);

    @Query("DELETE FROM tb_base_device")
    int deleteAll();
}
