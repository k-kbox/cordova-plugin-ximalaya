package com.cordova.plugins.ximalaya;

import com.google.gson.Gson;
//import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.auth.call.IXmlyAuthListener;
import com.ximalaya.ting.android.opensdk.auth.exception.XmlyException;
import com.ximalaya.ting.android.opensdk.auth.handler.XmlySsoHandler;
import com.ximalaya.ting.android.opensdk.auth.model.XmlyAuth2AccessToken;
import com.ximalaya.ting.android.opensdk.auth.model.XmlyAuthInfo;
import com.ximalaya.ting.android.opensdk.datatrasfer.AccessTokenManager;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.PlayableModel;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;
import com.ximalaya.ting.android.opensdk.player.constants.PlayerConstants;
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener;
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException;
//import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
//import com.ximalaya.ting.android.opensdk.model.album.CategoryRecommendAlbumsList;
//import com.ximalaya.ting.android.opensdk.model.category.CategoryList;
//import com.ximalaya.ting.android.opensdk.model.tag.TagList;
//import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

/**
 * This class echoes a string called from JavaScript.
 */
public class Ximalaya extends CordovaPlugin {

    public static final String TAG = "Cordova.Plugin.Ximalaya";

    private CallbackContext messageChannel;

    private XmPlayerManager player;

    /**
     * 喜马拉雅授权实体类对象
     */
    private XmlyAuthInfo authInfo;

    /**
     * 喜马拉雅授权管理类对象
     */
    private XmlySsoHandler ssoHandler;

    public static final String REDIRECT_URL =  "https://read.k-kbox.com/api/ximalaya/validate_third_token";

    @Override
    protected void pluginInitialize() {

        super.pluginInitialize();

        Log.d(TAG, "plugin initialized.");
    }

    @Override
    public void onDestroy() {
        XmPlayerManager.release();
        super.onDestroy();
    }

    @Override
    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, String.format("%s is called. Callback ID: %s. args: %s",
                action, callbackContext.getCallbackId(), args.toString()));

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

                        authInfo = new XmlyAuthInfo(cordova.getActivity(),
                                appKey, packId, REDIRECT_URL, appKey);

                        ssoHandler = new XmlySsoHandler(cordova.getActivity(), authInfo);

                        initPlayer();

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
        else if (action.equals("oauth2")) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        String third_uid = args.getString(0);
                        String third_token = args.getString(0);

                        if (TextUtils.isEmpty(AccessTokenManager.getInstanse().getThirdToken())) {
//                            Bundle bundle = ssoHandler.authorizeByThirdSync(third_uid, third_token);
//                            XmlyAuth2AccessToken accessToken = XmlyAuth2AccessToken.parseAccessToken(bundle);
//                            if (accessToken.isSessionValid()) {
//                                /**
//                                 * 如果是第三方登录方式需要使用如下方式
//                                 */
//                                AccessTokenManager.getInstanse().setAccessTokenAndUidByThirdType(accessToken.getToken(),
//                                        accessToken.getExpiresAt(), third_uid, third_token);
//                            }

                            ssoHandler.authorizeByThird(third_uid, third_token, new IXmlyAuthListener() {
                                @Override
                                public void onComplete(Bundle bundle) {
                                    XmlyAuth2AccessToken accessToken = XmlyAuth2AccessToken.parseAccessToken(bundle);
                                    if (accessToken.isSessionValid()) {
                                        /**
                                         * 如果是第三方登录方式需要使用如下方式
                                         */
                                        AccessTokenManager.getInstanse().setAccessTokenAndUidByThirdType(accessToken.getToken(),
                                                accessToken.getExpiresAt(), third_uid, third_token);
                                    }
                                    callbackContext.success(bundle.toString());
                                }

                                @Override
                                public void onXmlyException(XmlyException e) {
                                    e.printStackTrace();
                                    JSONObject json = new JSONObject();
                                    try {
                                        json.put("code", -1);
                                        json.put("message", e.getMessage());
                                    }
                                    catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    callbackContext.error(json.toString());
                                }

                                @Override
                                public void onCancel() {
                                    JSONObject json = new JSONObject();
                                    try {
                                        json.put("code", -1);
                                        json.put("message", "cancel");
                                    }
                                    catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    callbackContext.error(json.toString());
                                }
                            });
                        }
                        else {
                            callbackContext.success(AccessTokenManager.getInstanse().getAccessToken());
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        JSONObject json = new JSONObject();
                        try {
                            json.put("code", -1);
                            json.put("message", e.getMessage());
                        }
                        catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        callbackContext.error(json.toString());
                    }
                }
            });
            return true;
        }
        else if (action.startsWith("player.")) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        String name = action.substring(7);
                        Log.e(TAG, "call player " + name);

                        getPlayer();

                        if (name.equals("play")) {
                            if (args.length() > 0 && !args.isNull(0) && args.get(0) != null) {
                                int idx = args.getInt(0);
                                getPlayer().play(idx);
                            } else {
                                getPlayer().play();
                            }
                        }
                        else if (name.equals("pause")) {
                            getPlayer().pause();
                        }
                        else if (name.equals("stop")) {
                            getPlayer().stop();
                        }
                        else if (name.equals("seek")) {
                            int pos = args.getInt(0);
                            getPlayer().seekTo(pos);
                        }
                        else if (name.equals("current")) {
                            int pos = getPlayer().getPlayCurrPositon();
                            callbackContext.success(pos);
                        }
                        else if (name.equals("duration")) {
                            int duraton = getPlayer().getDuration();
                            callbackContext.success(duraton);
                        }
                        else if (name.equals("onStatus")) {
                            setPlayerListener();
                        }
                        else {
                            callbackContext.error("no action");
                        }
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
        else if (action.equals("playList")) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        JSONArray arr = args.getJSONArray(0);
        //                List<Track> list = new Gson().fromJson(arr.toString(), List.class);
                        List<Track> list = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            list.add(new Gson().fromJson(arr.getJSONObject(i).toString(), Track.class));
                        }
                        int idx = args.getInt(1);
                        Log.e(TAG, "startIndex: " + idx + ", title: " + list.get(idx).getTrackTitle());
                        getPlayer().playList(list, idx);
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
        else if (action.equals("messageChannel")) {
            messageChannel = callbackContext;
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

    private void sendStatusChange(int messageType, Integer additionalCode, String value) {
        Log.e(TAG, "sendStatusChange " + value);
        if (additionalCode != null && value != null) {
            throw new IllegalArgumentException("Only one of additionalCode or value can be specified, not both");
        }

        JSONObject statusDetails = new JSONObject();
        try {
            statusDetails.put("id", -1);
            statusDetails.put("msgType", messageType);
            if (additionalCode != null) {
                JSONObject code = new JSONObject();
                code.put("code", additionalCode.intValue());
                statusDetails.put("value", code);
            }
            else if (value != null) {
                statusDetails.put("value", value);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to create status details", e);
        }

        this.sendEventMessage("status", statusDetails);
    }

    void sendEventMessage(String action, JSONObject actionData) {
        JSONObject message = new JSONObject();
        try {
            message.put("action", action);
            if (actionData != null) {
                message.put(action, actionData);
            }
        } catch (JSONException e) {
            LOG.e(TAG, "Failed to create event message", e);
        }

        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, message);
        pluginResult.setKeepCallback(true);
        if (messageChannel != null) {
            messageChannel.sendPluginResult(pluginResult);
        }
    }

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

    private XmPlayerManager getPlayer() {
        return XmPlayerManager.getInstance(cordova.getActivity());
    }

    private void initPlayer() {
        Log.e(TAG, "init player");
        getPlayer().init();
    }

    private void setPlayerListener() {
        Log.e(TAG, "add player listener");
        getPlayer().addPlayerStatusListener(new IXmPlayerStatusListener() {
            @Override
            public void onPlayStart() {
                sendStatusChange(0, null, "start");
            }

            @Override
            public void onPlayPause() {
                sendStatusChange(0, null, "pause");
            }

            @Override
            public void onPlayStop() {
                sendStatusChange(0, null, "stop");
            }

            @Override
            public void onSoundPlayComplete() {
                sendStatusChange(0, null, "complete");
            }

            @Override
            public void onSoundPrepared() {
                sendStatusChange(0, null, "prepared");
            }

            @Override
            public void onSoundSwitch(PlayableModel lastModel, PlayableModel curModel) {
                if (curModel == null && lastModel instanceof Track) {
                    // isAudition 表示是否为部分试听声音
                    if (((Track) lastModel).isAudition() &&
                            XmPlayerManager.getInstance(cordova.getActivity().getApplicationContext()).getPlayerStatus() == PlayerConstants.STATE_IDLE) {
                        // 这里面写入试听结束后的代码,比如可以引导用户进行购买等操作
                        Log.e(TAG, "试听结束");
                        new AlertDialog.Builder(cordova.getActivity()).setMessage("试听结束").setNeutralButton("确定", null).create().show();
                    }
                }
                sendStatusChange(0, null, "switch");
            }

            @Override
            public void onBufferingStart() {
                sendStatusChange(0, null, "buffer-start");
            }

            @Override
            public void onBufferingStop() {
                sendStatusChange(0, null, "buffer-stop");
            }

            @Override
            public void onBufferProgress(int i) {
                sendStatusChange(0, null, "buffer-progress");
            }

            @Override
            public void onPlayProgress(int i, int i1) {
                sendStatusChange(0, null, "progress");
            }

            @Override
            public boolean onError(XmPlayerException e) {
                return false;
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
