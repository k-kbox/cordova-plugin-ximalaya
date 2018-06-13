var exec = require('cordova/exec');

exports.init = function (arg0, arg1, arg2, success, error) {
    exec(success, error, 'ximalaya', 'init', [arg0, arg1, arg2]);
}

exports.getCategories = function (arg0, success, error) {
  exec(success, error, 'ximalaya', 'getCategories', [arg0]);
}

exports.getCategories = function (arg0, success, error) {
  exec(success, error, 'ximalaya', 'getTags', [arg0]);
}

exports.getCategories = function (arg0, arg1, success, error) {
  exec(success, error, 'ximalaya', 'getAlbumList', [arg0, arg1]);
}
