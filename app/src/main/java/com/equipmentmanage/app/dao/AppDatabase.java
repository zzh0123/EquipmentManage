package com.equipmentmanage.app.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.equipmentmanage.app.bean.BaseAreaTableBean;
import com.equipmentmanage.app.bean.BaseChemicalTableBean;
import com.equipmentmanage.app.bean.BaseCompanyTableBean;
import com.equipmentmanage.app.bean.BaseComponentTypeTableBean;
import com.equipmentmanage.app.bean.BaseDeviceTableBean;
import com.equipmentmanage.app.bean.BaseDirectionTableBean;
import com.equipmentmanage.app.bean.BaseEquipmentTableBean;
import com.equipmentmanage.app.bean.BaseStreamTableBean;
import com.equipmentmanage.app.bean.DeviceTableBean;
import com.equipmentmanage.app.bean.EquipmentManageTableBean;
import com.equipmentmanage.app.bean.GasTableBean;
import com.equipmentmanage.app.bean.ImgTableBean;
import com.equipmentmanage.app.bean.ImgTableBean1;
import com.equipmentmanage.app.bean.InstrumentTableBean;
import com.equipmentmanage.app.bean.LeakageTableBean;
import com.equipmentmanage.app.bean.NewAreaTableBean;
import com.equipmentmanage.app.bean.NewEquipTableBean;
import com.equipmentmanage.app.bean.TaskRecordsTableBean;
import com.equipmentmanage.app.bean.TaskTableBean;
import com.equipmentmanage.app.bean.ThresholdTableBean;
import com.equipmentmanage.app.bean.WeatherParamsTableBean;


/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2021/10/22 21:35
 */
@Database(entities = {TaskTableBean.class, EquipmentManageTableBean.class, GasTableBean.class,
        DeviceTableBean.class, ThresholdTableBean.class, InstrumentTableBean.class,
        LeakageTableBean.class,
        BaseDeviceTableBean.class, BaseEquipmentTableBean.class, BaseAreaTableBean.class,
        BaseStreamTableBean.class,
        ImgTableBean.class,
        BaseComponentTypeTableBean.class, BaseChemicalTableBean.class, BaseDirectionTableBean.class,
        WeatherParamsTableBean.class, TaskRecordsTableBean.class, BaseCompanyTableBean.class,
        NewAreaTableBean.class, NewEquipTableBean.class,
        ImgTableBean1.class
        }, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    private static final Object sLock = new Object();
    public abstract TaskTableDao taskTableDao();
    public abstract EquipmentManageDao equipmentManageDao();
    public abstract BaseGasTableDao baseGasTableDao();
    public abstract DeviceTableDao deviceTableDao();
    public abstract BaseThresholdTableDao baseThresholdTableDao();
    public abstract BaseInstrumentTableDao baseInstrumentTableDao();
    public abstract BaseLeakageTableDao baseLeakageTableDao();
    public abstract BaseDeviceTableDao baseDeviceTableDao();
    public abstract BaseEquipmentTableDao baseEquipmentTableDao();
    public abstract BaseAreaTableDao baseAreaTableDao();
    public abstract ImgTableDao imgTableDao();
    public abstract BaseStreamTableDao baseStreamTableDao();
    public abstract BaseComponentTypeTableDao baseComponentTypeTableDao();
    public abstract BaseDirectionTableDao baseDirectionTableDao();
    public abstract BaseChemicalTableDao baseChemicalTableDao();
    public abstract WeatherParamsTableDao weatherParamsTableDao();
    public abstract TaskRecordsTableDao taskRecordsTableDao();
    public abstract BaseCompanyTableDao baseCompanyTableDao();

    public abstract NewAreaTableDao newAreaTableDao();
    public abstract NewEquipTableDao newEquipTableDao();
    public abstract ImgTableDao1 imgTableDao1();






    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "landa.db")
                                .allowMainThreadQueries()
//                                .addMigrations(MIGRATION_1_2)
                                .build();
            }
            return INSTANCE;
        }
    }

//    Room.databaseBuilder(getApplicationContext(), MyDb.class, "database-name")
//            .addMigrations(MIGRATION_1_2, MIGRATION_2_3).build();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE 'tb_equipment_manage' ('id' TEXT PRIMARY KEY NOT NULL, "
                    + "'content'TEXT)");
        }
    };

//    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE Book "
//                    + " ADD COLUMN pub_year INTEGER");
//        }
//    };
}