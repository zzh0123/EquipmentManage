package com.equipmentmanage.app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.equipmentmanage.app.bean.BaseCompanyTableBean;

import java.util.List;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/12/4
 */
@Dao
public interface BaseCompanyTableDao {
    @Query("SELECT * FROM tb_base_company")
    List<BaseCompanyTableBean> getAll();

    @Query("SELECT * FROM tb_base_company WHERE id IN (:id)")
    List<BaseCompanyTableBean> loadAllByIds(String[] id);

    @Query("SELECT * FROM tb_base_company WHERE id ==(:id)")
    BaseCompanyTableBean loadById(String id);

//    @Query("SELECT * FROM tasktable WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    User findByName(String first, String last);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BaseCompanyTableBean> baseCompanyTableBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(BaseCompanyTableBean baseCompanyTableBean);

    //修改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(BaseCompanyTableBean baseCompanyTableBean);

    @Delete
    void delete(BaseCompanyTableBean baseCompanyTableBean);

    @Query("DELETE FROM tb_base_company")
    int deleteAll();
}
