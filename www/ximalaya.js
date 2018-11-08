var exec = require('cordova/exec');

var _callback;

var ximalaya = {

  init: function (app_key, pack_id, app_secret, success, error) {
    _instance = this;
    exec(success, error, 'Ximalaya', 'init', [app_key, pack_id, app_secret]);
  },
  /**
   *
   * @param arg0
   * @param arg1
   * @param suucess
   * @param error
   */
  callApi: function (name, opts, success, error) {
    // exec(success, error, 'Ximalaya', name, [opts]);
    exec(function (data) {
      var d = JSON.parse(data);
      if (d.code === 0) {
        // console.log(d.data)
        var keys = [];
        for (var k in d.data) {
          // console.log(k);
          keys.push(k);
        }
        success && success(keys.length === 1 ? d.data[keys[0]] :  d.data);
      } else {
        success && success(d)
      }
    }, function (err) {
      error && error(err);
    }, 'Ximalaya', name, [opts]);
  },
  player: function(name, opts, success, error) {
    if (name === 'onStatus') {
      console.log('add callback');
      _callback = opts;
    }
    exec(function(p) { success && success(p); }, error, 'Ximalaya', 'player.' + name, [opts]);
  },
  playList: function(tracks, startIndex, success, error) {
    exec(success, error, 'Ximalaya', 'playList', [tracks, startIndex]);
  },
  onStatus: function (id, msgType, value) {
    if (_callback) {
      _callback(value);
    } else {
      if (console.error) {
        console.error("Received Ximalaya.onStatus callback for unknown media :: " + id);
      }
    }
  }
};

module.exports = ximalaya;

function onMessageFromNative(msg) {
  if (msg.action == 'status') {
    ximalaya.onStatus(msg.status.id, msg.status.msgType, msg.status.value);
  } else {
    throw new Error('Unknown ximalaya action' + msg.action);
  }
}

if (cordova.platformId === 'android' || cordova.platformId === 'amazon-fireos' || cordova.platformId === 'windowsphone') {

  var channel = require('cordova/channel');

  channel.createSticky('onXimalayaPluginReady');
  channel.waitForInitialization('onXimalayaPluginReady');

  channel.onCordovaReady.subscribe(function() {
    exec(onMessageFromNative, undefined, 'Ximalaya', 'messageChannel', []);
    channel.initializationComplete('onXimalayaPluginReady');
  });
}

// exports.getCategories = function (arg0, success, error) {
//   exec(success, error, 'ximalaya', 'getCategories', [arg0]);
// }
//
// exports.getTags = function (arg0, success, error) {
//   exec(success, error, 'ximalaya', 'getTags', [arg0]);
// }
//
// exports.getAlbumList = function (arg0, arg1, success, error) {
//   exec(success, error, 'ximalaya', 'getAlbumList', [arg0, arg1]);
// }
//
// exports.getCategoryRecommendAlbums = function (arg0, arg1, success, error) {
//   exec(success, error, 'ximalaya', 'getCategoryRecommendAlbums', [arg0, arg1]);
// }