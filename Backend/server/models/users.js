var mongoose = require('mongoose');

module.exports = mongoose.model('Users_old',{
	name:String,
	phone:String
});

var Schema = mongoose.Schema;  

var userSchema = mongoose.Schema({    
     token : String,     
     email: String,  
     hashed_password: String,    
     salt : String,  
     temp_str:String 
});  

mongoose.connect('mongodb://localhost:27017/uomdb'); 
module.exports = mongoose.model('users', userSchema);