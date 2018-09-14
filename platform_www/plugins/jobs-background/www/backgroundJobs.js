cordova.define("jobs-background.backgroundJobs", function(require, exports, module) {
/*global cordova, module*/

module.exports = {
    schedule: function (name, successCallback, errorCallback) {
    	console.log("recieved");
        cordova.exec(successCallback, errorCallback, "backgroundJobs", "scheduleJob", [name]);
    }
};

});
