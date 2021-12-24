package com.equipmentmanage.app.netapi;

import com.equipmentmanage.app.bean.CorrectCheckBean;
import com.equipmentmanage.app.bean.LoginPostBean;
import com.equipmentmanage.app.bean.NewRecordBean;
import com.equipmentmanage.app.bean.PutOnRecordBean;
import com.equipmentmanage.app.bean.TaskBean;
import com.equipmentmanage.app.bean.TaskResultBean;
import com.equipmentmanage.app.bean.User;
import com.equipmentmanage.app.bean.WeatherParamsBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Author: zzh
 * Date: 2021/8/9
 * Description: 网络接口地址 Api
 */
public interface HttpApi {

    // prefix
    String PREFIX = "/jeecg-boot";

    // 登录， post请求
//    @POST("/jeecg-boot/sys/mLogin")
    @POST(PREFIX + "/sys/mLogin")
    Observable<ResponseBody> login(@Body LoginPostBean loginPostBean);

    // 登出， post请求
//    @POST("/jeecg-boot/sys/logout")
    @POST(PREFIX + "/sys/logout")
    Observable<ResponseBody> logout(@Body LoginPostBean loginPostBean);

    // get请求
//    @GET("user/getUserList")
//    Observable<ResponseBody> getUserList();
    // /sys/dict/getDictItems/${code}   下拉框通过这个接口，获取字典值
//    @GET("/jeecg-boot/sys/dict/getDictItems/{code}")
    @GET(PREFIX + "/sys/dict/getDictItems/{code}")
    Observable<ResponseBody> getDeviceTypeList(@Path("code") String code);

    // 装置管理
//    @GET("/jeecg-boot/ldar/ldarBaseDevice/list")
    @GET(PREFIX + "/ldar/ldarBaseDevice/list")
    Observable<ResponseBody> getDeviceList(@QueryMap Map<String, Object> map);

    // getChemicalDetailList
//    @GET("/jeecg-boot/sys/dict/getDictItems/{code}")
    @GET(PREFIX + "/sys/dict/getDictItems/{code}")
    Observable<ResponseBody> getDictList(@Path("code") String code);

    // 获取设备管理列表
//    @GET("/jeecg-boot/ldar/baseEquipment/list")
    @GET(PREFIX + "/ldar/baseEquipment/list")
    Observable<ResponseBody> getEquipmentList(@QueryMap Map<String, Object> map);

    // 获取区域管理列表
//    @GET("/jeecg-boot/ldar/baseArea/list")
    @GET(PREFIX + "/ldar/baseArea/list")
    Observable<ResponseBody> getAreaList(@QueryMap Map<String, Object> map);

    // 获取产品流列表
//    @GET("/jeecg-boot/ldar/baseProdStream/list")
    @GET(PREFIX + "/ldar/baseProdStream/list")
    Observable<ResponseBody> getProductFlowist(@QueryMap Map<String, Object> map);

    // 获取任务清单列表-废弃
//    @GET("/jeecg-boot/ldar/liveTask/list")
//    Observable<ResponseBody> getTaskList(@QueryMap Map<String, Object> map);

    // 获取任务清单列表
//    @GET("/jeecg-boot/ldar/app/liveTaskApp/downLoadTaskList")
    @GET(PREFIX + "/ldar/app/liveTaskApp/downLoadTaskList")
    Observable<ResponseBody> getTaskList(@QueryMap Map<String, Object> map);

    // 检查结果上传， post请求
//    @POST("/jeecg-boot/ldar/app/liveTaskApp/accessResultList")
    @POST(PREFIX + "/ldar/app/liveTaskApp/accessResultList")
    Observable<ResponseBody> accessResultList(@Body List<TaskResultBean.Records> records);

    // 检测仪器校准， post请求
//    @POST("/jeecg-boot/ldar/app/liveTaskApp/accessCorrecting")
    @POST(PREFIX + "/ldar/app/liveTaskApp/accessCorrecting")
    Observable<ResponseBody> accessCorrecting(@Body CorrectCheckBean correctCheckBean);

    // 气象参数， post请求
//    @POST("/jeecg-boot/ldar/app/liveTaskApp/accessWeather")
    @POST(PREFIX + "/ldar/app/liveTaskApp/accessWeather")
    Observable<ResponseBody> accessWeather(@Body WeatherParamsBean weatherParamsBean);


    // --------基础数据--------

    // 标准气查询
//    @GET("/jeecg-boot/ldar/app/baseApp/queryGasList")
    @GET(PREFIX + "/ldar/app/baseApp/queryGasList")
    Observable<ResponseBody> getGasList(@Query("belongCompany") String belongCompany);

    // 阈值查询
//    @GET("/jeecg-boot/ldar/app/baseApp/queryCompanyList")
    @GET(PREFIX + "/ldar/app/baseApp/queryCompanyList")
    Observable<ResponseBody> getThresholdList(@Query("orgCode") String orgCode);

    // 仪器查询
//    @GET("/jeecg-boot/ldar/app/baseApp/queryInstrumentList")
    @GET(PREFIX + "/ldar/app/baseApp/queryInstrumentList")
    Observable<ResponseBody> getInstrumentList(@Query("belongCompany") String belongCompany);

    // 泄漏源查询
//    @GET("/jeecg-boot/ldar/app/baseApp/queryLeakingList")
    @GET(PREFIX + "/ldar/app/baseApp/queryLeakingList")
    Observable<ResponseBody> getLeakageList(@Query("belongCompany") String belongCompany);

    // 装置
//    @GET("/jeecg-boot/ldar/app/baseApp/queryDeviceList")
    @GET(PREFIX + "/ldar/app/baseApp/queryDeviceList")
    Observable<ResponseBody> getBaseDeviceList(@Query("belongCompany") String belongCompany);

    // 设备
//    @GET("/jeecg-boot/ldar/app/baseApp/queryEquipmentList")
    @GET(PREFIX + "/ldar/app/baseApp/queryEquipmentList")
    Observable<ResponseBody> getBaseEquipmentList(@Query("belongCompany") String belongCompany);

    // 区域
//    @GET("/jeecg-boot/ldar/app/baseApp/queryAreaList")
    @GET(PREFIX + "/ldar/app/baseApp/queryAreaList")
    Observable<ResponseBody> getBaseAreaList(@Query("belongCompany") String belongCompany);

    // 产品流
//    @GET("/jeecg-boot/ldar/app/baseApp/queryStreamList")
    @GET(PREFIX + "/ldar/app/baseApp/queryStreamList")
    Observable<ResponseBody> getBaseStreamList(@Query("belongCompany") String belongCompany);

    // 装置 区域 设备联动
    /**
     * 装置 区域 设备联动
     * 先选装置 LDAR_BASE_DEVICE
     * 区域  LDAR_BASE_AREA
     * 设备 LDAR_BASE_EQUIPMENT
     */
//    @GET("/jeecg-boot/sys/dict/getDictItems/{code}")
    @GET(PREFIX + "/sys/dict/getDictItems/{code}")
    Observable<ResponseBody> getBaseLinkList(@Path("code") String code);

    // 化学品
//    @GET("/jeecg-boot/ldar/app/baseApp/queryChemicalManagList")
    @GET(PREFIX + "/ldar/app/baseApp/queryChemicalManagList")
    Observable<ResponseBody> getBaseChemicalList(@Query("belongCompany") String belongCompany);

    // 组件类型
//    @GET("/jeecg-boot/ldar/app/baseApp/queryComponentTypeList")
    @GET(PREFIX + "/ldar/app/baseApp/queryComponentTypeList")
    Observable<ResponseBody> getBaseComponentTypeList(@Query("belongCompany") String belongCompany);

    // 方向
//    @GET("/jeecg-boot/ldar/app/baseApp/queryDirectionList")
    @GET(PREFIX + "/ldar/app/baseApp/queryDirectionList")
    Observable<ResponseBody> getBaseDirectionList(@Query("belongCompany") String belongCompany);



    // /sys/common/upload
    // 传单张图片
//    @POST("/jeecg-boot/sys/common/upload")
    @POST(PREFIX + "/sys/common/upload")
    @Multipart
    Observable<ResponseBody> uploadImg(@Part MultipartBody.Part part);

    // 传多张图片
//    @POST("/jeecg-boot/ldar/app/liveTaskApp/accessBookBuildPics")
    @POST(PREFIX + "/ldar/app/liveTaskApp/accessBookBuildPics")
    @Multipart
    Observable<ResponseBody> uploadImgList(@Part MultipartBody.Part[] parts);

    // 建档数据上传， post请求
//    @POST("/jeecg-boot/ldar/app/liveTaskApp/accessBookBuildList")
    @POST(PREFIX + "/ldar/app/liveTaskApp/accessBookBuildList")
    Observable<ResponseBody> putOnRecordUpload(@Body List<NewRecordBean> records);


    // 所属公司
    // /ldar/baseCompany/list  这个接口
//    @GET("/jeecg-boot/ldar/ldarBaseDevice/list")
    @GET(PREFIX + "/ldar/baseCompany/list")
    Observable<ResponseBody> getBaseCompanyList(@QueryMap Map<String, Object> map);






    @GET("weather_mini")
    Observable<ResponseBody> getDeviceList1(@QueryMap Map<String, Object> map);

    @GET("api/Authorize/PubOwner")
    Observable<ResponseBody> getUserList();

    @GET("weather_mini")
    Observable<ResponseBody> getUserList1(@Query("citykey") String citykey);

    @GET("user/getUserResultByUserId")
    Observable<ResponseBody> getUserResultByUserId(@Query("userId") String userId);

    @GET("user/getUsersByPage")
    Observable<ResponseBody> getUsersByPage(@QueryMap Map<String, Object> map);

    // post请求
    @POST("user/insertUser")
    Observable<ResponseBody> insertUser(@Body User user);

    @POST("user/insertUser1")
    @FormUrlEncoded
    Observable<ResponseBody> insertUser1(@Field("userId") String userId, @Field("userName") String userName);

    @POST("user/insertUser2")
    @FormUrlEncoded
    Observable<ResponseBody> insertUser2(@FieldMap Map<String, Object> map);


    /**
     * 文件下载
     */
    @GET()
    @Streaming
//使用Streaming 方式 Retrofit 不会一次性将ResponseBody 读取进入内存，否则文件很多容易OOM
    Observable<ResponseBody> downloadFile(@Url String fileUrl);//返回值使用 ResponseBody 之后会对ResponseBody 进行读取

    /**
     * 文件上传
     */
    // 传单张图片
    @POST("share/upload")
    @Multipart
    Observable<ResponseBody> upload(@Part MultipartBody.Part part);

    // 传多张图片
    @POST("share/multiUpload")
    @Multipart
    Observable<ResponseBody> multiUpload(@Part MultipartBody.Part[] parts);

    // 传参数 + 多张图片
    @POST("share/multiUpload1")
    @Multipart
    Observable<ResponseBody> multiUpload1(@Part("userId") RequestBody userId, @Part("content") RequestBody content,
                                          @Part("typeList") RequestBody typeList,
                                          @Part() List<MultipartBody.Part> parts);

    // 传参数 + 多张图片(建议)
    @POST("share/multiUpload2")
    @Multipart
    Observable<ResponseBody> multiUpload2(@PartMap Map<String, RequestBody> map,
                                          @Part() List<MultipartBody.Part> parts);

    @POST("share/test")
    @Multipart
    Observable<ResponseBody> test(@Part("age") String age, @PartMap Map<String, RequestBody> map);
//    Observable<ResponseBody> test(@Body RequestBody typeList);
}
