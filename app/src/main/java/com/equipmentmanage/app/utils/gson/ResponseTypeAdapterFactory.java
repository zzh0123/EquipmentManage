package com.equipmentmanage.app.utils.gson;

import com.equipmentmanage.app.bean.BaseBean;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;



public class ResponseTypeAdapterFactory extends CustomizedTypeAdapterFactory<BaseBean> {
    private static final String CODE_OK = "200";
    public static final String DATA = "data";
    public static final String CODE = "code";
    public static final String MSG = "message";
    public static final String SUCCESS = "success";
    
    public ResponseTypeAdapterFactory() {
        super(BaseBean.class);
    }
    
    @Override
    protected void onRead(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement code = jsonObject.get(CODE);// 获取code字段
        JsonElement success=jsonObject.get(SUCCESS);
        // 调用成功，不处理
        if (code == null || !code.isJsonPrimitive() || CODE_OK.equals(code.getAsString()) || ((success!=null)&&(success.getAsBoolean()))) {
            return;
        }
        JsonElement data = jsonObject.get(DATA);
        if (data != null) {
            // 将data的值挪到message中
            jsonObject.remove(DATA);
//            jsonObject.add(MSG, data);
        }
    }
}
