var app = {
    // Application Constructor
    initialize: function() {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

	onDeviceReady: function() {
		backgroundJobs.schedule("World", 
		success=>{
			console.log(success);
		}, 

		failure=>{
			console.log(failure);
		});
	}
};
app.initialize();