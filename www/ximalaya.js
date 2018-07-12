var exec = require('cordova/exec');

var ximalaya = {

  init: function (app_key, pack_id, app_secret, success, error) {
    exec(success, error, 'Ximalaya', 'init', [arg0, arg1, arg2]);
  },
  /**
   *
   * @param arg0
   * @param arg1
   * @param suucess
   * @param error
   */
  callApi: function (name, opts, success, error) {
    exec(success, error, 'Ximalaya', name, [opts]);
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