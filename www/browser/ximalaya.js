
// import CryptoJS from 'crypto-js';
var CryptoJS = require('./CryptoJS');

// import * as _http from 'http'
// import * as _https from 'https'
var _https = require('./https');

// const url = 'https://read.k-kbox.com/api/gql';

// const cache = {};
//
// var http = {
//   'http:': _http,
//   'https:': _https
// }

// import * as _url from 'url'
var _url = require('./url');

// var accessToken = null;
// var device_id = null;

var Buffer = require('./buffer').Buffer;

// decleare var CryptoJS;

function hashcode(str) {
  var hash = 0, i, chr, len;
  if (str.length === 0) return hash;
  for (i = 0, len = str.length; i < len; i++) {
    chr   = str.charCodeAt(i);
    hash  = ((hash << 5) - hash) + chr;
    hash |= 0; // Convert to 32bit integer
  }
  return hash;

  // var hash = 0, i, char;
  // if (str.length == 0) return hash;
  // for (i = 0; i < str.length; i++) {
  //   char = str.charCodeAt(i);
  //   hash = ((hash<<5)-hash)+char;
  //   hash = hash & hash; // Convert to 32bit integer
  // }
  // return hash;
}

function getRandomString(length) {
  var base = "abcdefghijklmnopqrstuvwxyz0123456789";
  var sb = '';
  for (var i = 0; i < length; i++) {
    sb += base.charAt(Math.floor((Math.random()*base.length)));
    // sb.append(base.charAt(random.nextInt(base.length())));
  }
  return sb;
}

function getUTF8Length(string) {
  var utf8length = 0;
  for (var n = 0; n < string.length; n++) {
    var c = string.charCodeAt(n);
    if (c < 128) {
      utf8length++;
    }
    else if ((c > 127) && (c < 2048)) {
      utf8length = utf8length + 2;
    }
    else {
      utf8length = utf8length + 3;
    }
  }
  return utf8length;
}

function getDeviceId() {
  var device_id = localStorage.getItem('device_id');
  if (!device_id) {
    var base = "abcdef0123456789";
    var sb = '';
    for (var i = 0; i < 16; i++) {
      sb += base.charAt(Math.floor((Math.random() * 16)));
      // sb.append(base.charAt(random.nextInt(base.length())));
    }
    device_id = sb.toString();
    localStorage.setItem('device_id', device_id);
  }
  return device_id;
}

function getSignature(opts) {

  var keys = [];
  for (let k in opts) {
    keys.push(k);
  }
  keys.sort();

  var params = [];

  for (let k of keys) {
    params.push(k + '=' + opts[k]);
  }

  // console.log(params.join("&"))

  var str = new Buffer(params.join('&')).toString('base64');
  // console.log(str);

  var hmac = CryptoJS.HmacSHA1(str, CONFIG.app_secret);

  var md5 = CryptoJS.MD5(hmac);

  // console.log(hmac.toString(), md5.toString());

  // console.log(MD5.hex_md5(hmac));
  //
  // console.log(MD5.hex_hmac_md5(appsecret, str));

  return md5.toString();
  // return CryptoJS.HmacSHA1(str, appsecret).toString();
}

function paramsToStr(opts) {

  var keys = [];
  for (let k in opts) {
    keys.push(k);
  }
  keys.sort();

  var params = [];

  for (let k of keys) {
    params.push(k + '=' + encodeURI(opts[k]));
  }

  return params.join('&');
}

// const app_key = "b52cf09deb2d278cd8c19ffaeadf992b";
// const app_secret = "0faac8ec89153249d54f4af9c7094496";
// const pack_id = "com.kuaiyou.app.reading";
// const os_type = '2';
// const sdk_version = 'v5.6.0';

const CONFIG = {
  app_key: "b52cf09deb2d278cd8c19ffaeadf992b",
  app_secret: "0faac8ec89153249d54f4af9c7094496",
  pack_id: "com.kuaiyou.app.reading",
  os_type: '2',
  sdk_version: 'v5.6.0'
};

const API_LIST = {
  getCategories: {
    url: 'https://api.ximalaya.com/openapi-gateway-app/categories/list',
    opts: {

    }
  },
  getTags: 'https://api.ximalaya.com/openapi-gateway-app/v2/tags/list',
  getAlbumList: 'https://api.ximalaya.com/openapi-gateway-app/v2/albums/list',
  getTracks: 'https://api.ximalaya.com/openapi-gateway-app/albums/browse',
  getBatch: 'https://api.ximalaya.com/openapi-gateway-app/albums/get_batch',
  getUpdateBatch: 'https://api.ximalaya.com/openapi-gateway-app/albums/get_update_batch',
  getHotTracks: 'https://api.ximalaya.com/openapi-gateway-app/tracks/hot',
  getBatchTracks: 'https://api.ximalaya.com/openapi-gateway-app/tracks/get_batch',
  getLastPlayTracks: 'https://api.ximalaya.com/openapi-gateway-app/tracks/get_last_play_tracks',
  getMetadataList: 'https://api.ximalaya.com/openapi-gateway-app/metadata/list',
  getMetadataAlbumList: 'https://api.ximalaya.com/openapi-gateway-app/metadata/albums',
  getAnnouncerCategoryList: 'https://api.ximalaya.com/openapi-gateway-app/announcers/categories',
  getAnnouncerList: 'https://api.ximalaya.com/openapi-gateway-app/announcers/list',
  getAlbumsByAnnouncer: 'https://api.ximalaya.com/openapi-gateway-app/announcers/albums',
  getTracksByAnnouncer: 'https://api.ximalaya.com/openapi-gateway-app/announcers/tracks',
  getAnnouncersBatch: 'https://api.ximalaya.com/openapi-gateway-app/announcers/get_batch',
  getSearchedAlbums: 'https://api.ximalaya.com/openapi-gateway-app/search/albums',
  getSearchedTracks: 'https://api.ximalaya.com/openapi-gateway-app/search/tracks',
  getHotWords: 'https://api.ximalaya.com/openapi-gateway-app/search/hot_words',
  getSuggestWord: 'https://api.ximalaya.com/openapi-gateway-app/search/suggest_words',
  getSearchAnnouncers: 'https://api.ximalaya.com/openapi-gateway-app/search/announcers',
  getSearchAll: 'https://api.ximalaya.com/openapi-gateway-app/search/all',
  getDiscoveryRecommendAlbums: 'https://api.ximalaya.com/openapi-gateway-app/albums/discovery_recommend_albums',
  getCategoryRecommendAlbums: 'https://api.ximalaya.com/openapi-gateway-app/albums/category_recommend_albums',
  getRelativeAlbums: 'https://api.ximalaya.com/openapi-gateway-app/v2/albums/relative_album',
  getRecommendDownloadList: 'https://api.ximalaya.com/openapi-gateway-app/albums/recommend_download',
  getRelativeAlbumsUseTrackId: 'https://api.ximalaya.com/openapi-gateway-app/v2/tracks/relative_album',
  getGuessLikeAlbum: {
    url: 'https://api.ximalaya.com/openapi-gateway-app/v2/albums/guess_like',
    opts: {
      device_type: "2"
    }
  },
}

function mergeOptions(api, access_token, options) {
  var opts = {
    access_token: access_token,
    app_key: CONFIG.app_key,
    client_os_type: CONFIG.os_type,
    device_id: getDeviceId(), // '47e1762a531d3b24',
    pack_id: CONFIG.pack_id,
    sdk_version: CONFIG.sdk_version
  };

  if (typeof api === 'object' && api.opts) {
    for (let k in api.opts) {
      opts[k] = api.opts[k];
    }
  }

  for (let k in options) {
    opts[k] = options[k];
  }

  return opts;
}

function request(url, method, options) {
  return new Promise((resolve, reject) => {
    // console.log('wx.request', options)

    /*var key = hashcode(JSON.stringify(options)) + url;
    // console.log(key, cache, new Date().getTime());

    // cache
    if (cache.hasOwnProperty(key) &&
      cache[key].expired > new Date().getTime()) {
      // console.log('cache hitted')
      resolve(cache[key].data);
    } else*/ {

      var params = [];
      for (var k in options) {
        params.push(k + '=' + options[k]);
      }

      var uri = _url.parse(url);

      // console.log(uri);

      var opts = method === 'GET' ? {
        host: 'read.k-kbox.com', // uri.host,
        port: 443, // uri.port,
        path: '/api/ximalaya' + uri.path + (params.length > 0 ? ("?" + paramsToStr(options)) : ""),
        method: 'GET',
        // headers: {
        //   'Content-Type': "text/plain; charset=utf-8",
        //   'Host': uri.host,
        //   'Referer': uri.protocol + "://" + uri.host
        // }
      } : {
        host: 'read.k-kbox.com', // uri.host,
        port: 443, // uri.port,
        path: '/api/ximalaya' + uri.path,
        method: 'POST',
        headers: {
          'Content-Type': "application/x-www-form-urlencoded",
          'Content-Length': getUTF8Length(params.join("&")),
          // 'Host': uri.host,
          // 'Referer': uri.protocol + "://" + uri.host
        }
      };

      var req = /*http[uri.protocol]*/_https.request(opts, function(res) {
        var body = '';
        res.setEncoding('utf8');
        res.on('data', function(data) {
          // console.log(body);
          body += data;
        }).on('end', function() {
          // console.log(body);
          // console.log(new Buffer(body).toString('utf-8'));
          //
          // cb(body);
          // resolve(new Buffer(body).toString('utf-8'));
          resolve(JSON.parse(body))
        });
        // res.on('error', function (error) {
        //   reject(error)
        // })
      });
      req.on('error', (e) => {
        reject(e);
      });
      if (method === 'GET') {
        req.end()
      } else {
        req.end(params.join("&"));
      }

      // http.request({
      //   url: url,
      //   method: method,
      //   data: params.join('&'),
      //   header: {
      //     'content-type': 'application/x-www-form-urlencoded', // 'application/json', // 默认值
      //     // 'Access-Token': wx.getStorageSync('token') || null
      //   },
      //   function (res) {
      //     // console.log(res);
      //     if (options.fetchPolicy !== 'network-only' && options.fetchPolicy !== 'no-cache') {
      //       // console.log('cache')
      //       cache[key] = {
      //         data: res.data,
      //         expired: new Date().getTime() + 600 * 1000
      //       }
      //     }
      //     resolve(res.data);
      //   }
      // })
    }
  });
}

function getAccessToken() {

  let accessToken;

  let s = localStorage.getItem('accessToken');

  if (s) {
    accessToken = JSON.parse(s);
  }

  var timestamp = new Date().getTime();

  // console.log(accessToken.timestamp, accessToken.timestamp + (accessToken.expires_in - 5) * 1000, timestamp)

  if (!accessToken || (accessToken.timestamp + (accessToken.expires_in + 5) * 1000) < timestamp) {
    var opts = {
      client_id: CONFIG.app_key,
      device_id: getDeviceId(),
      grant_type: 'client_credentials',
      nonce: getRandomString(9),
      timestamp: '' + timestamp
    }

    opts['sig'] = getSignature(opts);

    // console.log(opts)

    return request("https://api.ximalaya.com/oauth2/secure_access_token", "POST", opts)
      .then(data => {
        console.log(typeof data, data)
        accessToken = (typeof data === 'string') ? JSON.parse(data) : data;
        accessToken['timestamp'] = timestamp;
        // console.log(accessToken)
        // accessToken = data;
        localStorage.setItem('accessToken', JSON.stringify(accessToken));
        return accessToken;
      })
  } else {
    return Promise.resolve(accessToken);
  }
}

var ximalaya = {

  init: function (app_key, pack_id, app_secret, success, error) {
    CONFIG.app_key = app_key;
    CONFIG.pack_id = pack_id;
    CONFIG.app_secret = app_secret;
    getAccessToken()
      .then((d) => {
        if (success) success('ok')
      })
      .catch((e) => {
        if (error) error('err')
      });
  },
  /**
   *
   * @param arg0
   * @param arg1
   * @param suucess
   * @param error
   */
  callApi: function (name, opts, success, error) {
    getAccessToken()
      .then((t) => {

        if (API_LIST.hasOwnProperty(name)) {
          let api = API_LIST[name];
          var options = mergeOptions(api, t.access_token, opts);

          options['sig'] = getSignature(options);

          request((typeof api === 'object' ? api.url : api) + "?" + paramsToStr(options), "GET", {})
            .then((data) => {
              if (success) success(data);
            })
            .catch((err) => {
              if (error) error(err);
            })
        } else {
          if (error) error({code: -1, message: "api not found"});
        }
        // .then(data => {
        //   // console.log(data)
        //   return data;
        // })
      })
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