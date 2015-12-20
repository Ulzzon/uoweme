var Users = require('../models/users');

module.exports.create = function (req, res) {
  var user = new Users(req.body);
  user.save(function (err, result) {
    res.json(result);
  });
}

module.exports.list = function (req, res) {
  Users.find({}, function (err, results) {
    res.json(results);
  });
}

module.exports.add = function (req,res){
	var user = new Users();
	user.name = req.user;
	user.phone = req.phoneNr;
	user.save(function (err, result){
		res(result);
	});
}

module.exports.update = function (req, res){
	Users.find({}, function (err, findResult){
		var user = User(findResult);
		user.phone = req.newPhoneNr;
		user.save(function (err, result){
			res(result);
		});
	});
}