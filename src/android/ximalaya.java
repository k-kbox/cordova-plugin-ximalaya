package com.cordova.plugins.ximalaya;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;


import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        }
        return false;
    }

    private void init(String appKey, String packId, String appSecret) {
        CommonRequest.getInstanse().setAppkey(appKey);
        CommonRequest.getInstanse().setPackid(packId);
        CommonRequest.getInstanse().init(cordova.getActivity().getApplicationContext(), appSecret);
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
