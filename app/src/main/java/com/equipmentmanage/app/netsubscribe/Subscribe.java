package com.equipmentmanage.app.netsubscribe;
import com.equipmentmanage.app.bean.CorrectCheckBean;
import com.equipmentmanage.app.bean.LoginPostBean;
import com.equipmentmanage.app.bean.PutOnRecordBean;
import com.equipmentmanage.app.bean.TaskBean;
import com.equipmentmanage.app.bean.TaskResultBean;
import com.equipmentmanage.app.bean.User;
import com.equipmentmanage.app.bean.WeatherParamsBean;
import com.equipmentmanage.app.utils.netutils.RetrofitFactory;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2019/11/8$ 16:50$
 */
public class Subscribe {

    /**
     * 登录
     */
    public static void login(LoginPostBean loginPostBean, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().login(loginPostBean);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 登出
     */
    public static void logout(LoginPostBean loginPostBean, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().logout(loginPostBean);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 检查结果上传
     */
    public static void accessResultList(List<TaskResultBean.Records> records, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().accessResultList(records);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 检测仪器校准
     */
    public static void accessCorrecting(CorrectCheckBean correctCheckBean, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().accessCorrecting(correctCheckBean);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 气象参数
     */
    public static void accessWeather(WeatherParamsBean weatherParamsBean, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().accessWeather(weatherParamsBean);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }



    /**
     * getDeviceTypeList
     */
    public static void getDictList(String code, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getDictList(code);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 标准气查询
     */
    public static void getGasList(String belongCompany, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getGasList(belongCompany);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 阈值查询 orgCode
     */
    public static void getThresholdList(String orgCode, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getThresholdList(orgCode);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 仪器查询
     */
    public static void getInstrumentList(String belongCompany, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getInstrumentList(belongCompany);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 泄漏源查询
     */
    public static void getLeakageList(String belongCompany, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getLeakageList(belongCompany);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 装置-基础数据不联动
     */
    public static void getBaseDeviceList(String belongCompany, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getBaseDeviceList(belongCompany);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 设备
     */
    public static void getBaseEquipmentList(String belongCompany, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getBaseEquipmentList(belongCompany);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 区域
     */
    public static void getBaseAreaList(String belongCompany, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getBaseAreaList(belongCompany);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 产品流
     */
    public static void getBaseStreamList(String belongCompany, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getBaseStreamList(belongCompany);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 装置 区域 设备联动
     * 先选装置 LDAR_BASE_DEVICE
     * 区域  LDAR_BASE_AREA
     * 设备 LDAR_BASE_EQUIPMENT
     */
    public static void getBaseLinkList(String code, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getBaseLinkList(code);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 化学品
     */
    public static void getBaseChemicalList(String belongCompany, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getBaseChemicalList(belongCompany);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 组件类型
     */
    public static void getBaseComponentTypeList(String belongCompany, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getBaseComponentTypeList(belongCompany);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 方向
     */
    public static void getBaseDirectionList(String belongCompany, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getBaseDirectionList(belongCompany);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }


    /**
     * 建档数据上传
     */
    public static void putOnRecordUpload(List<PutOnRecordBean> records, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().putOnRecordUpload(records);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }



    /**
     * getDeviceList
     */
    public static void getDeviceList(Map<String, Object> map, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getDeviceList(map);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 获取设备管理列表
     */
    public static void getEquipmentList(Map<String, Object> map, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getEquipmentList(map);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 获取区域管理列表
     */
    public static void getAreaList(Map<String, Object> map, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getAreaList(map);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 获取产品流列表
     */
    public static void getProductFlowist(Map<String, Object> map, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getProductFlowist(map);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 获取任务清单列表
     */
    public static void getTaskList(Map<String, Object> map, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getTaskList(map);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 传单张图片
     */
    public static void uploadImg(MultipartBody.Part part, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().uploadImg(part);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 传多张图片
     */
    public static void uploadImgList(MultipartBody.Part[] parts, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().uploadImgList(parts);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }










    /**
     * getUserList
     */
    public static void getUserList(DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getUserList();
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    public static void getUserList1(DisposableObserver<ResponseBody> subscriber, String citykey) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getUserList1(citykey);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * getUserResultByUserId
     */
    public static void getUserResultByUserId(String userId, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getUserResultByUserId(userId);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * getUsersByPage
     */
    public static void getUsersByPage(Map<String, Object> map, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getUsersByPage(map);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * insert
     */
//    public static void insert(User user, DisposableObserver<ResponseBody> subscriber) {
//        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().insert(user);
//        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
//    }

    /**
     * insertUser
     */
    public static void insertUser(User user, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().insertUser(user);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * insertUser1
     */
    public static void insertUser1(String userId, String userName, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().insertUser1(userId, userName);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * insertUser2
     */
    public static void insertUser2(Map<String, Object> map, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().insertUser2(map);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * upload
     */
    public static void upload(MultipartBody.Part part, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().upload(part);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * multiUpload
     */
    public static void multiUpload(MultipartBody.Part[] parts, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().multiUpload(parts);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * multiUpload1
     */
    public static void multiUpload1(RequestBody userId, RequestBody content, RequestBody typeList, List<MultipartBody.Part> parts, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().multiUpload1(userId, content, typeList, parts);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * multiUpload2
     */
    public static void multiUpload2(Map<String, RequestBody> map, List<MultipartBody.Part> parts, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().multiUpload2(map, parts);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * test
     */
    public static void test(String age, Map<String, RequestBody> map, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().test(age, map);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }


    public static void downLoadFile(String fileUrl, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().downloadFile(fileUrl);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }
}
