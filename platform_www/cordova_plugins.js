cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
  {
    "id": "cordova-call.CordovaCall",
    "file": "plugins/cordova-call/www/CordovaCall.js",
    "pluginId": "cordova-call",
    "clobbers": [
      "cordova.plugins.CordovaCall"
    ]
  },
  {
    "id": "cordova-plugin-background-fetch.BackgroundFetch",
    "file": "plugins/cordova-plugin-background-fetch/www/BackgroundFetch.js",
    "pluginId": "cordova-plugin-background-fetch",
    "clobbers": [
      "window.BackgroundFetch"
    ]
  },
  {
    "id": "jobs-background.backgroundJobs",
    "file": "plugins/jobs-background/www/backgroundJobs.js",
    "pluginId": "jobs-background",
    "clobbers": [
      "backgroundJobs"
    ]
  }
];
module.exports.metadata = 
// TOP OF METADATA
{
  "cordova-call": "1.1.6",
  "cordova-plugin-background-fetch": "5.4.1",
  "cordova-plugin-whitelist": "1.3.3",
  "jobs-background": "0.7.0"
};
// BOTTOM OF METADATA
});