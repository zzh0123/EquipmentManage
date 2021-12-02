package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.ImgTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/23
 */
@Dao
public interface ImgTableDao {
    @Query("SELECT * FROM tb_base_img")
    List<ImgTableBean> getAll();

    @Query("SELECT * FROM tb_base_img WHERE file_name IN (:file_name)")
    List<ImgTableBean> loadAllByIds(String[] file_name);

    @Query("SELECT * FROM tb_base_img WHERE file_name ==(:file_name)")
    ImgTableBean loadById(String file_name);

    @Query("SELECT * FROM tb_base_img WHERE `current_date` ==(:currentDate)")
    List<ImgTableBean> loadByDate(String currentDate);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ImgTableBean> imgTableBeans);

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insert(ImgTableBean imgTableBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(ImgTableBean imgTableBean);


    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ImgTableBean imgTableBean);

    @Delete
    void delete(ImgTableBean imgTableBean);

    @Query("DELETE FROM tb_base_img")
    void deleteAll();
}
