package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.NewAreaTableBean;
import com.equipmentmanage.app.bean.NewAreaTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/11
 */
@Dao
public interface NewAreaTableDao {
    @Query("SELECT * FROM tb_new_area")
    List<NewAreaTableBean> getAll();

    @Query("SELECT * FROM tb_new_area WHERE code IN (:code)")
    List<NewAreaTableBean> loadAllByCodes(String[] code);

    @Query("SELECT * FROM tb_new_area WHERE code ==(:code)")
    NewAreaTableBean loadByCode(String code);

    @Query("SELECT * FROM tb_new_area WHERE device_code ==(:deviceCode)")
    List<NewAreaTableBean> loadAllByDeviceCode(String deviceCode);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NewAreaTableBean> newAreaTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(NewAreaTableBean newAreaTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(NewAreaTableBean newAreaTableBean);

    @Delete
    void delete(NewAreaTableBean newAreaTableBean);

    @Query("DELETE FROM tb_new_area")
    int deleteAll();
}
