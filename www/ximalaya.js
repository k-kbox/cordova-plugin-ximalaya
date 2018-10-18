var exec = require('cordova/exec');

var ximalaya = {

  init: function (app_key, pack_id, app_secret, success, error) {
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
  }

};

module.exports = ximalaya;

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