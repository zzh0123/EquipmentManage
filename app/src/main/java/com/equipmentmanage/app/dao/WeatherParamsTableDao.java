package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.WeatherParamsTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/11/06
 */
@Dao
public interface WeatherParamsTableDao {
    @Query("SELECT * FROM tb_base_weather_params")
    List<WeatherParamsTableBean> getAll();

    @Query("SELECT * FROM tb_base_weather_params WHERE id IN (:id)")
    List<WeatherParamsTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_weather_params WHERE id ==(:id)")
    WeatherParamsTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WeatherParamsTableBean> weatherParamsTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(WeatherParamsTableBean weatherParamsTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(WeatherParamsTableBean weatherParamsTableBean);

    @Delete
    void delete(WeatherParamsTableBean weatherParamsTableBean);

    @Query("DELETE FROM tb_base_weather_params")
    void deleteAll();
}
