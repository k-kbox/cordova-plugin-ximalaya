package com.cordova.plugins.ximalaya;

import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;

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
public class Ximalaya extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        /*if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }*/

        if (action.equals("init")) {
            this.init(args);
            return true;
        }

        if (action.equals("getCategories")) {
            this.getCategories(args, callbackContext);
            return true;
        }

        if (action.equals("getTags")) {
            this.getTags(args, callbackContext);
            return true;
        }

        if (action.equals("getAlbumList")) {
            this.getAlbumList(args, callbackContext);
            return true;
        }


        return false;
    }

    private void init(JSONArray args) {
        String appKey = args.getString(0);
        String packId = args.getString(1);
        String appSecret = args.getString(2);
        CommonRequest.getInstanse().setAppkey(appKey);
        CommonRequest.getInstanse().setPackid(packId);
        CommonRequest.getInstanse().init(cordova.getActivity().getApplicationContext(), appSecret);
    }
    private void getTags(JSONArray args, CallbackContext callbackContext) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, args.getInt(0));
        map.put(DTransferConstants.CATEGORY_ID ,"0");
        map.put(DTransferConstants.CALC_DIMENSION ,"1");
        CommonRequest.getAlbumList(map, new IDataCallBack<AlbumList>() {
            @Override
            public void onSuccess(AlbumList object) {
                JSONObject json = new JSONObject();
                try {
                    json.put("code", 0);
                    json.put("message", "success");
                    json.put("data", new JSONArray(object.getCategories()));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                callbackContext.success(json.toString());
            }

            @Override
            public void onError(int code, String message) {
                JSONObject json = new JSONObject();
                try {
                    json.put("code", code);
                    json.put("message", message);
                    json.put("data", new JSONArray());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                callbackContext.error(json.toString());
            }
        });
    }

    private void getTags(JSONArray args, CallbackContext callbackContext) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, args.getInt(0));
        map.put(DTransferConstants.TYPE, 0);
        CommonRequest.getTags(map, new IDataCallBack<TagList>() {
            @Override
            public void onSuccess(CategoryList object) {
                JSONObject json = new JSONObject();
                try {
                    json.put("code", 0);
                    json.put("message", "success");
                    json.put("data", new JSONArray(object.getCategories()));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                callbackContext.success(json.toString());
            }

            @Override
            public void onError(int code, String message) {
                JSONObject json = new JSONObject();
                try {
                    json.put("code", code);
                    json.put("message", message);
                    json.put("data", new JSONArray());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                callbackContext.error(json.toString());
            }
        });
    }

    private void getCategories(JSONArray args, CallbackContext callbackContext) {
        Map<String, String> map = new HashMap<String, String>();
        CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
            @Override
            public void onSuccess(CategoryList object) {
                JSONObject json = new JSONObject();
                try {
                    json.put("code", 0);
                    json.put("message", "success");
                    json.put("data", new JSONArray(object.getCategories()));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                callbackContext.success(json.toString());
            }

            @Override
            public void onError(int code, String message) {
                JSONObject json = new JSONObject();
                try {
                    json.put("code", code);
                    json.put("message", message);
                    json.put("data", new JSONArray());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                callbackContext.error(json.toString());
            }
        });
    }

    /*private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }*/
}
