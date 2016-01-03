var fs = require('fs'),
	http = require('http'),
	socketio = require('socket.io'),
	express = require('express'),
	app = express(),
	connect = require('connect'),
//	bodyParser = require('body-parser'),
	mongoose = require('mongoose'),
	controller = require('./server/controllers/controllers'),
	Users = require('./server/models/users');
	var port     = process.env.PORT || 8080;

//mongoose.connect('mongodb://localhost:27017/uomdb');

// Configuration 
//app.use(bodyParser());
app.use(express.static(__dirname + '/public')); 
app.use(connect.logger('dev')); 
app.use(connect.json()); 
app.use(connect.urlencoded());
// Routes  
require('./Routes/routes.js')(app);    

/* var server = http.createServer(function(req, resp){
	resp.writeHead(200, { 'Content-type': 'text/html'});
	resp.end(fs.readFileSync(__dirname + '/index.html'));
}).listen(8888);
*/
app.listen(port);
/*
socketio.listen(server).on('connection', function(socket){
	socket.emit('hand_shake', { 'shaker' : 'server'});
		console.log('Connected');


	socket.on('message', function(msg){
		console.log('message received: ', msg.body);
		console.log('UserName: ', msg.user);
		console.log('PhoneNr: ', msg.phoneNr);
		//socket.broadcast.emit('message',msg);
		controller.create(msg, function(result){
			console.log('Db response: ', result.body);
		});
	});

	socket.on('addUser', function(msg){
		//var user = Users(msg.user,msg.phoneNr);
		//user.name = msg.user;
		//user.phone = msg.phoneNr;
		console.log('adding: ', msg)
		controller.add(msg, function(result){
			console.log('db result: ', result);
		});
	});

	socket.on('updateUser', function(msg){
		console.log('Updating', msg.user);
		controller.update(msg, function(result){
			console.log(result);
		});
	});

	socket.on('hand_shake_ok', function(msg){
		console.log('connecting device: ', msg);
	});

	socket.on('disconnect', function(socket){
		console.log('Device disconnected');
	});
});
*/