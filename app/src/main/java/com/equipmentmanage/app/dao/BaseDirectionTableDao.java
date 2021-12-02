package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.BaseDirectionTableBean;
import com.equipmentmanage.app.bean.BaseDirectionTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/30
 */
@Dao
public interface BaseDirectionTableDao {
    @Query("SELECT * FROM tb_base_direction")
    List<BaseDirectionTableBean> getAll();

    @Query("SELECT * FROM tb_base_direction WHERE id IN (:id)")
    List<BaseDirectionTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_direction WHERE id ==(:id)")
    BaseDirectionTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BaseDirectionTableBean> baseDirectionTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BaseDirectionTableBean baseDirectionTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BaseDirectionTableBean baseDirectionTableBean);

    @Delete
    void delete(BaseDirectionTableBean baseDirectionTableBean);

    @Query("DELETE FROM tb_base_direction")
    void deleteAll();
}
