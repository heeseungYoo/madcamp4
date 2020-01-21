var express = require('express');
var bodyParser = require('body-parser');
var mongoose = require('mongoose');

var app = express();

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended : true}));
var port = process.env.PORT || 80;

mongoose.connect('mongodb://localhost:27017/emergency');

app.listen(port, function () {
  console.log('server is running on' + port);
});


//스키마
var Schema_login = mongoose.Schema;
var Schema = mongoose.Schema;
//var PointSchema = new Schema({type: String, date: String, point: Number}, {_id:false});
var personSchema = new Schema_login({
  name : String,
  userID : {type:String, unique:true, trim:true}, 
  userPassword : String,
  email : String,
  userBirth: {type: String, default:"none"},
  userBloodtype: {type: String, default:"none"},
  userHeight: {type: Number, default:0},
  userWeight: {type: Number, default:0},
  userEmerContact: {type: String, default:"none"},
  userAllergy: {type:String, default:"none"},
  userDisease: {type: String, default:"none"}
});

var Person = mongoose.model("Person", personSchema);
//var Point = mongoose.model("Point", PointSchema);

//전체 학생 명단
app.get('/persons', function (req, res, next){
  Person.find({}, function (error, login){
    if (error) {
      return res.json(error);
    }
    return res.json(login);
    });
});

app.get('/persons/:userID', function(req, res, next){
  Person.find({userID: req.params.userID}, function(err, found){
    if(err){
      return res.json(err);
    }
    return res.send(found);
    });
});

app.delete('/persons/:id', function (req, res, next){
  Person.remove({ _id : req.params.id}, function (error, login){
    if (error) {
      return res.json(error);
    }
    return res.json(login);
    });
});

app.post('/persons', function(req, res, next){
  var userID = req.body.userID;
  var userPassword = req.body.userPassword;
  var name = req.body.name;
  var email = req.body.email;
  var person = Person({
    userID : userID,
    userPassword : userPassword,
    name : name,
    email : email,
  });

  person.save(function(err){
    if (err) {
      return res.json(err);
    }
    return res.send("Successfully Created");
  });
});

app.put('/persons/:userID', function(req, res, next) {
  var userID = req.body.userID;
  var userBirth = req.body.userBirth;
  var userBloodtype = req.body.userBloodtype;
  var userHeight = req.body.userHeight;
  var userWeight = req.body.userWeight;
  var userEmerContact = req.body.userEmerContact;
  var userAllergy = req.body.userAllergy;
  var userDisease = req.body.userDisease;
  Person.findOneAndUpdate({userID:userID}, 
    {userBirth: userBirth,
     userBloodtype: userBloodtype,
     userHeight: userHeight,
     userWeight: userWeight,
     userEmerContact: userEmerContact,
     userAllergy: userAllergy,
     userDisease: userDisease},
     {new: true},
      function(err, found){
        if(err){
          return res.json(err);
        }
        console.log("-------yeeees----")
        return res.send(found);
        }
    );
});

//-------------------------------------------------------------

app.put('/personalInfo/:userID', function(req, res, next) {

  const json = req.body.points
  const obj = JSON.parse(json);
  Login.findOneAndUpdate({userID:req.body.userID}, 
    {$push:{points:{type:obj.type, date:obj.date, point:obj.point}}},{new:true}, function (error, login){
      if (error) {
        console.log("req---" + req.body.points);
        return res.json(error);
      }
      console.log("req---" + req.body.points);
      console.log("req---userID-- " + req.body.userID);
      return res.json(login);
    });    
});


var pipeline = [
  {"$unwind": "$points"},
  {
    "$group": {
      "_id":"$_id",
      "userImg": {"$first":"$userImg"},
      "userID": {"$first":"$userID"},
      "userPassword":{"$first":"$userPassword"},
      "name":{"$first":"$name"},
      "email":{"$first":"$email"},
      "points": {"$push": "$points"},
      "totalPoints":{"$sum":"$points.point"}
    }
  }, {"$out":"logins"}
]


app.get('/personalInfo/:userID', function(req, res, next) {
  Login.aggregate(pipeline).exec(function(err, results) {
    console.log(JSON.stringify(results));
    if(err) throw err;
    Login.find({userID:req.params.userID}, function(err, found) {
      if(err) return res.json(err);
     
    return res.send(found);
    })
  })
});



