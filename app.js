const http = require('http');
var express = require('express');
var fs = require('fs');

var x; //latitude
var y; //longitude
var n; //신고자 이름
var a; //알레르기
var d;
var h;
var w;
var c;
var b;
var result;
var iconv = require('iconv-lite');
var jschardet = require('jschardet');

const server = http.createServer((req, res)=> {
    // res.write('<h1>Hello Node!<h1>');
    // res.end('<p>Hello Server!</p>');

    // // Send client db data
    if (req.method === 'GET'){
//res.write('<h1></h1>');
if(result != null){
//
//res.write(iconv.encode(result,'UTF-8'));
var content = jschardet.detect(result);
console.log(content);
//res.write("<meta charset=\"utf-8\">");

//res.write("<h1>신고자이름 : "+n+"<br>알레르기 : "+a+"<br>질병 : "+d+"<br>혈액형 : "+b+"<br>신장 : "+h+"<br>체중 : "+w+"<br>연락처 : "+c+"</h1>");

res.write("<!Doctype html><html lang=\"en\"><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\"><meta name=\"description\" content=\"\"><meta name=\"author\" content=\"Mark Otto, Jacob Thornton, and Bootstrap contributors\"><meta name=\"generator\" content=\"Jekyll v3.8.6\"><title>신고현황</title><link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css\" integrity=\"sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh\" crossorigin=\"anonymous\"><script src=\"https://code.jquery.com/jquery-3.4.1.slim.min.js\" integrity=\"sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n\" crossorigin=\"anonymous\"></script><script src=\"https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js\" integrity=\"sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo\" crossorigin=\"anonymous\"></script><script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js\" integrity=\"sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6\" crossorigin=\"anonymous\"></script><link rel=\"canonical\" href=\"https://getbootstrap.com/docs/4.4/examples/jumbotron/\"><link href=\"/docs/4.4/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh\" crossorigin=\"anonymous\"><link rel=\"apple-touch-icon\" href=\"/docs/4.4/assets/img/favicons/apple-touch-icon.png\" sizes=\"180x180\"><link rel=\"icon\" href=\"/docs/4.4/assets/img/favicons/favicon-32x32.png\" sizes=\"32x32\" type=\"image/png\"><link rel=\"icon\" href=\"/docs/4.4/assets/img/favicons/favicon-16x16.png\" sizes=\"16x16\" type=\"image/png\"><link rel=\"manifest\" href=\"/docs/4.4/assets/img/favicons/manifest.json\"><link rel=\"mask-icon\" href=\"/docs/4.4/assets/img/favicons/safari-pinned-tab.svg\" color=\"#563d7c\"><link rel=\"icon\" href=\"/docs/4.4/assets/img/favicons/favicon.ico\"><meta name=\"msapplication-config\" content=\"/docs/4.4/assets/img/favicons/browserconfig.xml\"><meta name=\"theme-color\" content=\"#563d7c\"><style>.bd-placeholder-img {font-size: 1.125rem;text-anchor: middle;-webkit-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;}@media (min-width: 768px) {.bd-placeholder-img-lg {font-size: 3.5rem;}}</style><link href=\"jumbotron.css\" rel=\"stylesheet\"></head><body><nav class=\"navbar navbar-expand-md navbar-dark fixed-top bg-dark\"><a class=\"navbar-brand\" href=\"#\">신고현황</a><button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarsExampleDefault\" aria-controls=\"navbarsExampleDefault\" aria-expanded=\"false\" aria-label=\"Toggle navigation\"><span class=\"navbar-toggler-icon\"></span></button><div class=\"collapse navbar-collapse\" id=\"navbarsExampleDefault\"><ul class=\"navbar-nav mr-auto\"><li class=\"nav-item active\"><a class=\"nav-link\" href=\"#\"><span class=\"sr-only\">(current)</span></a></li><li class=\"nav-item\"><a class=\"nav-link\" href=\"#\"></a></li><li class=\"nav-item\"><a class=\"nav-link disabled\" href=\"#\" tabindex=\"-1\" aria-disabled=\"true\"></a></li><li class=\"nav-item dropdown\"><a class=\"nav-link dropdown-toggle\" href=\"#\" id=\"dropdown01\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\"></a><div class=\"dropdown-menu\" aria-labelledby=\"dropdown01\"><a class=\"dropdown-item\" href=\"#\">Action</a><a class=\"dropdown-item\" href=\"#\">Another action</a><a class=\"dropdown-item\" href=\"#\">Something else here</a></div></li></ul><form class=\"form-inline my-2 my-lg-0\"><button class=\"btn btn-outline-success my-2 my-sm-0\" type=\"submit\">환자정보</button></form></div></nav><main role=\"main\"><div class=\"jumbotron\"><div class=\"container\"><h1 class=\"display-3\">환자정보</h1><p></p></div></div><div class=\"container\"><div class=\"row\"><div class=\"col-md-4\"><h2>이름</h2><p><button class=\"btn btn-outline-success my-2 my-sm-0\" type=\"submit\">"+n+"</button></p></div><div class=\"col-md-4\"><h2>혈액형</h2><p><button class=\"btn btn-outline-success my-2 my-sm-0\" type=\"submit\">"+b+"</button></p></div><div class=\"col-md-4\"><h2>긴급연락처</h2><p><button class=\"btn btn-outline-success my-2 my-sm-0\" type=\"submit\">"+c+"</button></p></div></div><br><div class=\"row\"><div class=\"col-md-4\"><h2>알레르기</h2><p><button class=\"btn btn-outline-success my-2 my-sm-0\" type=\"submit\">"+a+"</button></p></div><div class=\"col-md-4\"><h2>질병</h2><p><button class=\"btn btn-outline-success my-2 my-sm-0\" type=\"submit\">"+d+"</button></p></div><div class=\"col-md-4\"><h2>신장/체중</h2><p><button class=\"btn btn-outline-success my-2 my-sm-0\" type=\"submit\">"+h+"cm</button></p><p><button class=\"btn btn-outline-success my-2 my-sm-0\" type=\"submit\">"+w+"kg</button></p></div></div><hr></div></main><footer class=\"container\"><p>&copy; 희승Company 2020 </p></footer><script src=\"https://code.jquery.com/jquery-3.4.1.slim.min.js\" integrity=\"sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n\" crossorigin=\"anonymous\"></script><script>window.jQuery || document.write('<script src=\"/docs/4.4/assets/js/vendor/jquery.slim.min.js\"></script>')</script><script src=\"/docs/4.4/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-6khuMg9gaYr5AxOqhkVIODVIvm9ynTT5J4V1cfthmT+emCG6yVmEZsRHdxlotUnm\" crossorigin=\"anonymous\"></script></body></html>");
res.write("<!DOCTYPE html><html><head><title>응급상황</title><meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\"><meta charset=\"utf-8\"><style>#map {height: 100%;}html, body {height: 100%;margin: 0;padding: 0;}</style><script src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyAJ0YeE_JEEu1bbKRzgfsx5Sd1Q9vxvCJE&callback=initMap\"></script><script>var map;function initialize() {var mapOptions = {zoom: 18,center: {lat:"+x+" , lng: "+y+"}};map = new google.maps.Map(document.getElementById('map'),mapOptions);var marker = new google.maps.Marker({position: {lat: "+x+", lng: "+y+"},map: map});var infowindow = new google.maps.InfoWindow({content: '<br>위치:' + marker.getPosition() + '<br>신고자이름 : "+n+" </p>'});google.maps.event.addListener(marker, 'click', function() {infowindow.open(map, marker);});}google.maps.event.addDomListener(window, 'load', initialize);</script></head><body><div id=\"map\"></div></body></html>");
}
res.end();


        // console.log("request came");

        // //send contact data
        // if (req.url === '/contact'){
        //     mongo.connect(url, {
        //         useNewUrlParser : true,
        //         useUnifiedTopology:true}, (err, client) => {
        //             if (err){
        //                 console.error(err);
        //                 return;
        //             };

        //             var db = client.db('madcamp');
        //             var collection = db.collection('contacts');
        //             collection.find().sort({"name":1}).toArray((err,items)=>{
        //                 res.end(JSON.stringify({"DB_Output":items}));
        //                 //res.end();
        //             }
        //             );
        //         }
                
        //     );
        // }

    }

    // put data from client to db 
    else if (req.method === 'POST'){

        var body = [];
            req.on('data', (data)=> {
                body.push(data);
                body = Buffer.concat(body).toString();
                console.log(body); 
                result = body;
console.log(result);
var info = JSON.parse(body);
x = info.myLatitude;
y = info.myLongitude;
n = info.userName;
a = info.userAllergy;
d = info.userDisease;
b = info.userBloodtype;
h = info.userHeight;
w = info.userWeight;
c = info.userEmerContact;
                res.end();
            });




        // //store contact data
        // if (req.url === '/contact'){
        //     let body = [];
        //     req.on('data', (data)=> {
        //         body.push(data);
        //         body = Buffer.concat(body).toString();
        //         //console.log(body); 

        //         var inputlist = JSON.parse(body);           
        //         mongo.connect(url, {
        //             useNewUrlParser : true,
        //             useUnifiedTopology:true}, (err, client) => {
        //                 if (err){
        //                     console.error(err);
        //                     return;
        //                 };
    
        //                 var db = client.db('madcamp');
        //                 var collection = db.collection('contacts');
        //                 collection.deleteMany({});
        //                 collection.insertMany(inputlist.DB_Input);
        //                 res.end();
        //                 //console.log(inputlist['DB_input'][1]);
                    
        //         });

        //     });

        // }

        //     //store contact data
        //     if (req.url === '/gallery'){
        //         console.log("gallery put request");
        //         let body = [];
        //         req.on('data', (data)=> {
        //             body.push(data);
        //             body = Buffer.concat(body);
        //             var image = {"filename": body};
        
        //             mongo.connect(url, {
        //                 useNewUrlParser : true,
        //                 useUnifiedTopology:true}, (err, client) => {
        //                     if (err){
        //                         console.error(err);
        //                         return;
        //                     };
        
        //                     var db = client.db('madcamp');
        //                     var collection = db.collection('contacts');
        //                     collection.insertOne(image);
        //                     res.end();
        //                     //console.log(inputlist['DB_input'][1]);
                        
        //             });
    
        //         });
    
        //     }
    }

    else if (req.method === 'PUT'){

        // //store contact data to DB
        // if (req.url === '/contact'){
        //     let body = [];
        //     req.on('data', (data)=> {
        //         body.push(data);
        //         body = Buffer.concat(body).toString();
        //         //console.log(body); 

        //         var inputlist = JSON.parse(body);           
        //         mongo.connect(url, {
        //             useNewUrlParser : true,
        //             useUnifiedTopology:true}, (err, client) => {
        //                 if (err){
        //                     console.error(err);
        //                     return;
        //                 };
    
        //                 var db = client.db('madcamp');
        //                 var collection = db.collection('contacts');
        //                 collection.insertMany(inputlist.DB_Input);
        //                 res.end();
        //                 //console.log(inputlist['DB_input'][1]);
                    
        //         });

        //     });

        // }

        // if (req.url === '/contactedit'){
        //     let body = [];
        //     req.on('data', (data)=> {
        //         body.push(data);
        //         body = Buffer.concat(body).toString();
        //         //console.log(body); 

        //         var inputlist = JSON.parse(body);           
        //         mongo.connect(url, {
        //             useNewUrlParser : true,
        //             useUnifiedTopology:true}, (err, client) => {
        //                 if (err){
        //                     console.error(err);
        //                     return;
        //                 };
    
        //                 var db = client.db('madcamp');
        //                 var collection = db.collection('contacts');
        //                 var item = inputlist['DB_Input'][0];
        //                 collection.deleteOne({"personID":item.personID});
        //                 collection.insertOne(item);
        //                 res.end();
        //                 //console.log(inputlist['DB_input'][1]);
                    
        //         });

        //     });

        // }


    }

    else if (req.method === 'DELETE'){

        // //store contact data
        // if (req.url === '/contact'){
        //     let body = [];
        //     req.on('data', (data)=> {
        //         body.push(data);
        //         body = Buffer.concat(body).toString();
        //         //console.log(body); 

        //         var inputlist = JSON.parse(body);           
        //         mongo.connect(url, {
        //             useNewUrlParser : true,
        //             useUnifiedTopology:true}, (err, client) => {
        //                 if (err){
        //                     console.error(err);
        //                     return;
        //                 };
    
        //                 var db = client.db('madcamp');
        //                 var collection = db.collection('contacts');
        //                 collection.deleteMany(inputlist['DB_Input'][0]);
        //                 res.end();
        //                 //console.log(inputlist['DB_input'][1]);
                    
        //         });

        //     });

        // }
    }

    //wrong method
    // res.write('<h1>Hello Node!<h1>');
    // res.end('<p>Hello Server</p>');
});

server.listen(80);
server.on('listening', ()=> {
    console.log('980번 포트에서 서버 대기 중입니다!');
    //console.log(string_input);
});

server.on('error', (error)=>{
    console.error(error);
});
