package com.equipmentmanage.app.utils.netutils;

/**
 * Author: zzh
 * Date: 2021/8/9
 * Description: 接口
 */
public interface OnSuccessAndFaultListener {
    void onSuccess(String result);

    void onFault(String errorMsg);
}
