
var fs        = require('fs');
var path      = require('path');

fs
  .readdirSync(__dirname + '/res/android/drawable-xhdpi/')
  .forEach(file => {
    console.log('<resource-file src="res/android/drawable-xhdpi/' + file + '" target="res/drawable-xhdpi/' + file + '"/>');
  });
