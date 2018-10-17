package com.cordova.plugins.ximalaya;

import com.google.gson.Gson;
//import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
//import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
//import com.ximalaya.ting.android.opensdk.model.album.CategoryRecommendAlbumsList;
//import com.ximalaya.ting.android.opensdk.model.category.CategoryList;
//import com.ximalaya.ting.android.opensdk.model.tag.TagList;
//import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class echoes a string called from JavaScript.
 */
public class Ximalaya extends CordovaPlugin {

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        /*if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }*/

        if (action.equals("init")) {
//            this.init(args);
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        String appKey = args.getString(0);
                        String packId = args.getString(1);
                        String appSecret = args.getString(2);
                        CommonRequest.getInstanse().setAppkey(appKey);
                        CommonRequest.getInstanse().setPackid(packId);
                        CommonRequest.getInstanse().init(cordova.getActivity().getApplicationContext(), appSecret);
                        JSONObject json = new JSONObject();
                        try {
                            json.put("code", 0);
                            json.put("message", "success");
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        callbackContext.success(json.toString());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        JSONObject json = new JSONObject();
                        try {
                            json.put("code", -1);
                            json.put("message", e.getMessage());
                        }
                        catch (Exception e1) {
                            e.printStackTrace();
                        }
                        callbackContext.error(json.toString());
                    }
                }
            });
            return true;
        }
        else {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {

                    try {
                        Method method = CommonRequest.class.getDeclaredMethod(action, Map.class, IDataCallBack.class);
                        Map<String, String> map = new HashMap<String, String>();

                        if (args.length() > 0) {
                            JSONObject j = new JSONObject(args.getString(0));
                            Iterator<String> iter = j.keys();
                            while (iter.hasNext()) {
                                String k = iter.next();
                                map.put(k, j.optString(k, ""));
                            }
                        }
        //                map.put(DTransferConstants.ALBUM_ID, args.getString(0));
        //                map.put(DTransferConstants.SORT, "asc");
        //                map.put(DTransferConstants.PAGE, args.getString(1));
                        method.invoke(null, map, createDataCallback(callbackContext));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        JSONObject json = new JSONObject();
                        try {
                            json.put("code", -1);
                            json.put("message", e.getMessage());
                        }
                        catch (Exception e1) {
                            e.printStackTrace();
                        }
                        callbackContext.error(json.toString());
                    }
                }
            });
            return true;
        }

//        if (action.equals("getCategories")) {
//            this.getCategories(args, callbackContext);
//            return true;
//        }
//
//        if (action.equals("getTags")) {
//            this.getTags(args, callbackContext);
//            return true;
//        }
//
//        if (action.equals("getAlbumList")) {
//            this.getAlbumList(args, callbackContext);
//            return true;
//        }
//
//        if (action.equals("getTracks")) {
//            this.getTracks(args, callbackContext);
//            return true;
//        }
//
//        if (action.equals("getCategoryRecommendAlbums")) {
//            this.getCategoryRecommendAlbums(args, callbackContext);
//            return true;
//        }



//        return false;
    }

//    private void getTracks(JSONArray args, CallbackContext callbackContext) throws JSONException {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put(DTransferConstants.ALBUM_ID, args.getString(0));
//        map.put(DTransferConstants.SORT, "asc");
//        map.put(DTransferConstants.PAGE, args.getString(1));
//        CommonRequest.getTracks(map,
//                (IDataCallBack<TrackList>) createDataCallback(callbackContext));
//    }
//
//    private void getCategoryRecommendAlbums(JSONArray args, CallbackContext callbackContext) throws JSONException {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put(DTransferConstants.CATEGORY_ID, args.getString(0));
//        map.put("display_count" ,"10");
//        CommonRequest.getCategoryRecommendAlbums(map,
//                (IDataCallBack<CategoryRecommendAlbumsList>) createDataCallback(callbackContext));
//    }
//
//    private void getAlbumList(JSONArray args, CallbackContext callbackContext) throws JSONException {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put(DTransferConstants.CATEGORY_ID, args.getString(0));
//        map.put(DTransferConstants.TAG_NAME, args.getString(1));
//        map.put(DTransferConstants.CALC_DIMENSION ,"1");
//        map.put(DTransferConstants.PAGE, args.getString(2));
//
//        CommonRequest.getAlbumList(map,
//                (IDataCallBack<AlbumList>) createDataCallback(callbackContext));
//    }
//
//    private void getTags(JSONArray args, CallbackContext callbackContext) throws JSONException {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put(DTransferConstants.CATEGORY_ID, args.getString(0));
//        map.put(DTransferConstants.TYPE, "0");
//        CommonRequest.getTags(map,
//                (IDataCallBack<TagList>) createDataCallback(callbackContext));
//    }
//
//    private void getCategories(JSONArray args, CallbackContext callbackContext) throws JSONException {
//        Map<String, String> map = new HashMap<String, String>();
//        CommonRequest.getCategories(map,
//                (IDataCallBack<CategoryList>) createDataCallback(callbackContext));
//    }
//
//    private void init(JSONArray args) throws JSONException {
//        String appKey = args.getString(0);
//        String packId = args.getString(1);
//        String appSecret = args.getString(2);
//        CommonRequest.getInstanse().setAppkey(appKey);
//        CommonRequest.getInstanse().setPackid(packId);
//        CommonRequest.getInstanse().init(cordova.getActivity().getApplicationContext(), appSecret);
//    }

    private IDataCallBack<?> createDataCallback(CallbackContext callbackContext) {
        return new IDataCallBack<Object>() {
            @Override
            public void onSuccess(Object o) {
                JSONObject json = new JSONObject();
                try {
                    json.put("code", 0);
                    json.put("message", "success");
                    json.put("data", new JSONObject(new Gson().toJson(o)));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                callbackContext.success(json.toString());
            }

            @Override
            public void onError(int i, String s) {
                JSONObject json = new JSONObject();
                try {
                    json.put("code", i);
                    json.put("message", s);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                callbackContext.error(json.toString());
            }
        };
    }



    /*private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }*/
}
