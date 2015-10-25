var fs = require('fs')
	, http = require('http')
	, socketio = require('socket.io')
	, MongoClient = require('mongodb').MongoClient;


var server = http.createServer(function(req, resp){
	resp.writeHead(200, { 'Content-type': 'text/html'});
	resp.end(fs.readFileSync(__dirname + '/index.html'));
}).listen(8888);


socketio.listen(server).on('connection', function(socket){
	socket.emit('hand_shake', { 'shaker' : 'server'});
		console.log('Connected');


	socket.on('message', function(msg){
		console.log('message received: ', msg);
		socket.broadcast.emit('message',msg);
	});

	socket.on('hand_shake_ok', function(msg){
		console.log('handshake: ', msg);
	});

	socket.on('disconnect', function(socket){
		console.log('Device disconnected');
	});
});
