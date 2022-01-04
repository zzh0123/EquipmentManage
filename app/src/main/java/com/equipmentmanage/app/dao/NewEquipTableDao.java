package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.NewEquipTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/11
 */
@Dao
public interface NewEquipTableDao {
    @Query("SELECT * FROM tb_new_equip")
    List<NewEquipTableBean> getAll();

    @Query("SELECT * FROM tb_new_equip WHERE code IN (:code)")
    List<NewEquipTableBean> loadAllByCodes(String[] code);

    @Query("SELECT * FROM tb_new_equip WHERE code ==(:code)")
    NewEquipTableBean loadByCode(String code);

    @Query("SELECT * FROM tb_new_equip WHERE device_code ==(:deviceCode) AND area_code ==(:areaCode)")
    List<NewEquipTableBean> loadAllByDeviceAndAreaCode(String deviceCode, String areaCode);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<NewEquipTableBean> newEquipTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(NewEquipTableBean newEquipTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(NewEquipTableBean newEquipTableBean);

    @Delete
    void delete(NewEquipTableBean newEquipTableBean);

    @Query("DELETE FROM tb_new_equip")
    int deleteAll();

    @Query("DELETE FROM tb_new_equip WHERE `device_code` ==(:deviceCode) "
            + "AND `area_code` ==(:areaCode) AND `code` ==(:equipCode)")
    int deleteByEquipCode(String deviceCode, String areaCode, String equipCode);

    @Query("DELETE FROM tb_new_equip WHERE `device_code` ==(:deviceCode) "
            + "AND `area_code` ==(:areaCode)")
    int deleteByAreaCode(String deviceCode, String areaCode);

    @Query("UPDATE tb_new_equip SET `code` =(:equipCode), `name` =(:equipName) WHERE`device_code` ==(:deviceCode) "
            + "AND `area_code` ==(:areaCode)")
    int updateByEquipCode(String equipCode, String equipName, String deviceCode, String areaCode);
}
