var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'ximalaya', 'coolMethod', [arg0]);
};

exports.init = function (arg0, arg1, arg2, success, error) {
    exec(success, error, 'ximalaya', 'init', [arg0, arg1, arg2]);
}

exports.getCategories = function (arg0, success, error) {
  exec(success, error, 'ximalaya', 'getCategories', [arg0]);
}