var exec = require('cordova/exec');

exports.init = function (arg0, arg1, arg2, success, error) {
    exec(success, error, 'ximalaya', 'init', [arg0, arg1, arg2]);
}

/**
 *
 * @param arg0
 * @param arg1
 * @param suucess
 * @param error
 */
exports.callApi = function (arg0, arg1, success, error) {
  exec(success, error, 'ximalaya', arg0, [arg1]);
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