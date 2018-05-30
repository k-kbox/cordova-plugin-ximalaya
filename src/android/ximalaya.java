package com.cordova.plugins.ximalaya;

import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This class echoes a string called from JavaScript.
 */
public class ximalaya extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }
        else if (action.equals("init")) {
            String appKey = args.getString(0);
            String packId = args.getString(1);
            String appSecret = args.getString(2);
            this.init(appKey, packId, appSecret);
            return true;
        }
        else if (action.equals("getCategories")) {
            Map<String, String> map = new HashMap<String, String>();
            this.getCategories(map, callbackContext);
        }
        return false;
    }

    private void init(String appKey, String packId, String appSecret) {
        CommonRequest.getInstanse().setAppkey(appKey);
        CommonRequest.getInstanse().setPackid(packId);
        CommonRequest.getInstanse().init(cordova.getActivity().getApplicationContext(), appSecret);
    }

    private void getCategories(Map<String, String> map, CallbackContext callbackContext) {
        // Map<String, String> map = new HashMap<String, String>();
        CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
            @Override
            public void onSuccess(CategoryList object) {
                JSONObject json = new JSONObject();
                try {
                    json.put("code", 0);
                    json.put("message", "success");
                    json.put("categories", new JSONArray(object.getCategories()));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                callbackContext.success(json.toString());
            }
​
            @Override
            public void onError(int code, String message) {
                JSONObject json = new JSONObject();
                try {
                    json.put("code", code);
                    json.put("message", message);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                callbackContext.error(json.toString());
            }
        });
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
