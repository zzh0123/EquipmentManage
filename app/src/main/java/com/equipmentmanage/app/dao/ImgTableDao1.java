package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.ImgTableBean1;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23
 */
@Dao
public interface ImgTableDao1 {
    @Query("SELECT * FROM tb_base_img1")
    List<ImgTableBean1> getAll();

    @Query("SELECT * FROM tb_base_img1 WHERE file_name IN (:file_name)")
    List<ImgTableBean1> loadAllByIds(String[] file_name);

    @Query("SELECT * FROM tb_base_img1 WHERE file_name ==(:file_name)")
    ImgTableBean1 loadById(String file_name);

    @Query("SELECT * FROM tb_base_img1 WHERE `current_date` ==(:currentDate)")
    List<ImgTableBean1> loadByDate(String currentDate);

    @Query("SELECT * FROM tb_base_img1 WHERE `current_date` ==(:currentDate) " + "AND `device_id` ==(:deviceId) "
            + "AND `area_code` ==(:areaCode) AND `equipment_code` ==(:equipmentCode)")
    List<ImgTableBean1> loadByDateAndEquip(String currentDate, String deviceId,
                                           String areaCode, String equipmentCode);

    @Query("SELECT * FROM tb_base_img1 WHERE `current_date` ==(:currentDate)")
    List<ImgTableBean1> loadByDeviceAndDate(String currentDate);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ImgTableBean1> imgTableBean1s);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insert(ImgTableBean1 imgTableBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(ImgTableBean1 imgTableBean1);


    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ImgTableBean1 imgTableBean1);

    @Delete
    void delete(ImgTableBean1 imgTableBean1);

    @Query("DELETE FROM tb_base_img1")
    void deleteAll();

    @Query("DELETE FROM tb_base_img1 WHERE `current_date` ==(:currentDate) " + "AND `device_id` ==(:deviceId) "
            + "AND `area_code` ==(:areaCode) AND `equipment_code` ==(:equipmentCode) AND `tag_num` ==(:tagNum)")
    int deleteByTagNum(String currentDate, String deviceId,
                        String areaCode, String equipmentCode,
                        String tagNum);

    @Query("DELETE FROM tb_base_img1 WHERE `device_id` ==(:deviceId) "
            + "AND `area_code` ==(:areaCode) AND `equipment_code` ==(:equipmentCode)")
    int deleteByEquipCode(String deviceId, String areaCode, String equipmentCode);

    @Query("DELETE FROM tb_base_img1 WHERE `device_id` ==(:deviceId) "
            + "AND `area_code` ==(:areaCode)")
    int deleteByAreaCode(String deviceId, String areaCode);
}
