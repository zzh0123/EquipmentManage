package com.equipmentmanage.app.netapi;

/**
 * Author: zzh
 * Date: 2021/8/14
 * Description:
 */
public class ConstantValue {

    public static String all_device_type = "全部装置类型";
    public static String all_device_type_value = "";

    public static String all_img_type = "全部类型";
    public static String all_img_type_value = "";

    /**
     * 0 校准设备(日常校准), 1 漂移校准
     */
    public static String correct_type = "correct_type";
    public static String correct_type_0 = "0";
    public static String correct_type_1 = "1";

    /**
     * 0 校准设备(日常校准), 1 漂移校准
     */
    public static String isPass_type = "isPass_type";
    public static String isPass_type_0 = "0";
    public static String isPass_type_1 = "1";
    public static String isPass_type_0_name = "否";
    public static String isPass_type_1_name = "是";

    public static String userId = "userId";
    public static String username = "username";
    public static String realname = "realname";
    public static String orgCode = "orgCode";
    public static String orgCodeTxt = "orgCodeTxt";
    public static String telephone = "telephone";

    public static String city = "city";
    public static String department = "department";


    // 装置列表参数
    public static String belongCompany = "belongCompany";
    public static String chemicalName = "chemicalName";
    public static String createBy = "createBy";
    public static String createTime = "createTime";
    public static String deviceCapacity = "deviceCapacity";

    public static String deviceCode = "deviceCode";
    public static String deviceName = "deviceName";
    public static String deviceType = "deviceType";
    public static String id = "id";
    public static String leakingDate = "leakingDate";

    public static String pageNo = "pageNo";
    public static String pageSize = "pageSize";
    public static String sysOrgCode = "sysOrgCode";
    public static String testSdate = "testSdate";
    public static String updateBy = "updateBy";

    public static String updateTime = "updateTime";
    public static String useDate = "useDate";


    public static String belongCompany1 = "A01A04"; // A01A01

    /**
     * 半层   0 否, 1 是
     */
    public static String half_type = "half_type";
    public static String half_type_0 = "0";
    public static String half_type_1 = "1";

    /**
     * 不可达   0 否, 1 是
     */
    public static String unreachable_type = "unreachable_type";
    public static String unreachable_type_0 = "0";
    public static String unreachable_type_1 = "1";
    public static String unreachable_type_0_name = "否";
    public static String unreachable_type_1_name = "是";

    /**
     * 是否保温   0 否, 1 是
     */
    public static String heatPre_type = "heatPre_type";
    public static String heatPre_type_0 = "0";
    public static String heatPre_type_1 = "1";
    public static String heatPre_type_0_name = "否";
    public static String heatPre_type_1_name = "是";


    public static final int BLUE_TOOTH_DIALOG = 0x111;
    public static final int BLUE_TOOTH_TOAST = 0x123;
    public static final int BLUE_TOOTH_WRAITE = 0X222;
    public static final int BLUE_TOOTH_READ = 0X333;
    public static final int BLUE_TOOTH_SUCCESS = 0x444;

    // 蓝牙指令
    public static String log_start = "log start";
    public static String log_stop = "log stop";
    // 通过<CR> 或<CR><LF>所有指令会被终止。
    // 如果一个无效的指令被TVA接受，它将回应一个“bad emd”。通过<CR><LF>
    // 终止这个回应。
    public static String log_stop_all = "<CR><LF>";
    public static String log_error = "bad emd";

    public static String last_time = "最后下载时间：";

    public static String event_1 = "event_1";
    public static String event_daily_save = "event_daily_save";
    public static String event_drift_save = "event_drift_save";
    public static String event_weather_save = "event_weather_save";
    public static String event_clear_cache = "event_clear_cache";

    public static String event_belong_company = "event_belong_company";

    public static String event_photo_save = "event_photo_save";

    public static String tag_num = "L-0000";
    public static String tag_num3 = "L-000";
    public static String tag_num2 = "L-00";
    public static String tag_num1 = "L-0";
    public static String tag_num0 = "L-";

    // 校准 1 通过  0 不通过
    public static String correct_pass_1 = "1";
    public static String correct_pass_0 = "0";


    public static String correct_0 = "0";
    public static String correct_1 = "1";
    public static String correct_0_name = "未校准";
    public static String correct_1_name = "已校准";

    public static String entered_0 = "0";
    public static String entered_1 = "1";
    public static String entered_0_name = "未录入";
    public static String entered_1_name = "已录入";

    public static String seal_point_checked_0 = "0";
    public static String seal_point_checked_1 = "1";
}
