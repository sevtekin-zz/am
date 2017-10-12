var express = require("express");

var mysql = require ("mysql");

var bodyParser = require ("body-parser");

var https = require("https");

var fs = require("fs");

var app = express();

var filters = "";

const port = 443;


var dbuser = process.env.dbuser;
var dbpass = process.env.dbpass;
var dbname = process.env.dbname;
var dbport = process.env.dbport;
var dbhost = process.env.dbhost;
var dumplocation = process.env.dumplocation;


var pool  = mysql.createPool({
	  connectionLimit : 100,
	  host: dbhost,
	  user: dbuser,
	  password: dbpass,
      database: dbname
});




app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(bodyParser.json())

app.get("/", function(req, res){

//res.send("<h1>Hello " + dbname + " " + dbpass + " " + dbport +  "<h2>");
console.log("HEY I AM ALIVE");
res.send ("HEY I AM ALIVE");
});
app.get("/v1/cashentries", function(req, res){

      var sqlStr = "select cashentry.id,actualdate,amount,ownerid,categoryid,ownerentry.name as ownername, categoryentry.name as categoryname,description from cashentry,ownerentry,categoryentry where (ownerid=amdb.ownerentry.id) and (categoryid=amdb.categoryentry.id)";
      console.log(sqlStr);
      pool.getConnection(function(err, connection) {
  		if (err) {
  	  		console.log(' Error getting mysql_pool connection: ' + err);
  	  		throw err;
  	  	}
      connection.query(sqlStr,function(err,rows){
          if(!err) {
              console.log("Query ran");
              res.json(rows);
          }
      });
   });  
});


app.get("/v1/reports/sumbyrange/:before/:after", function(req, res){
      var sqlStr = "SELECT SUM(amount) FROM cashentry WHERE actualdate>='" + req.params.after + "' AND actualdate <= '" + req.params.before + "'";
      pool.getConnection(function(err, connection) {
    		if (err) {
    	  		console.log(' Error getting mysql_pool connection: ' + err);
    	  		throw err;
    	  	}
      connection.query(sqlStr,function(err,rows){
          var result = [];
          if(!err) {
            for (var i = 0; i < rows.length; i++) {
              result.push({id: 1, amount: rows[i].amount, actualdate:'2014-01-01', description: 'description', categoryid: 1, categoryname: 'category name', ownerid:1, ownername:'name'});
            }
            res.json(result);
          }
      });
   });   
});

app.get("/v1/reports/sumupto/:before", function(req, res){

      var sqlStr = "SELECT SUM(amount) AS amount FROM cashentry WHERE actualdate < '" + req.params.before + "'";
      pool.getConnection(function(err, connection) {
    		if (err) {
    	  		console.log(' Error getting mysql_pool connection: ' + err);
    	  		throw err;
    	  }
      connection.query(sqlStr,function(err,rows){
          var result = [];
          if(!err) {
            for (var i = 0; i < rows.length; i++) {
              result.push({id: 1, amount: rows[i].amount, actualdate:'2014-01-01', description: 'description', categoryid: 1, categoryname: 'category name', ownerid:1, ownername:'name'});
            }
            res.json(result);


          }
      });
   });
      
});


app.get("/v1/cashentries/:filters", function(req, res){


      var sqlStr = "select cashentry.id,actualdate,amount,ownerid,categoryid,ownerentry.name as ownername, categoryentry.name as categoryname, description from cashentry,ownerentry,categoryentry where (ownerid=amdb.ownerentry.id) and (categoryid=amdb.categoryentry.id)" + " and " + req.params.filters;
      console.log(sqlStr);
      pool.getConnection(function(err, connection) {
    		if (err) {
    	  		console.log(' Error getting mysql_pool connection: ' + err);
    	  		throw err;
    	  	}
      connection.query(sqlStr ,function(err,rows){
          if(!err) {
              res.json(rows);
          }
      });
   });
      
});

app.get("/v1/cashentry/:id", function(req, res){

	var sqlStr = "SELECT * FROM cashentry WHERE id=" + req.params.id;
	console.log(sqlStr);
    pool.getConnection(function(err, connection) {
  		if (err) {
  	  		console.log(' Error getting mysql_pool connection: ' + err);
  	  		throw err;
  	  	}
      
      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      });
      
});

app.get("/v1/categoryentry/:id", function(req, res){
	var sqlStr = "SELECT * FROM categoryentry WHERE id=" + req.params.id;
	console.log(sqlStr);
    pool.getConnection(function(err, connection) {
  		if (err) {
  	  		console.log(' Error getting mysql_pool connection: ' + err);
  	  		throw err;
  	  	}
    
      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });
   });   
});

app.get("/v1/categoryentries", function(req, res){

      req.setEncoding('utf8');
      var sqlStr = "SELECT * FROM categoryentry ORDER BY name";
      console.log(sqlStr);
      pool.getConnection(function(err, connection) {
    		if (err) {
    	  		console.log(' Error getting mysql_pool connection: ' + err);
    	  		throw err;
    	  	}
      
      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      });
      
});

app.get("/v1/keywordentries", function(req, res){
    
	var sqlStr = "SELECT * FROM keywordentry";
	console.log(sqlStr);
    pool.getConnection(function(err, connection) {
  		if (err) {
  	  		console.log(' Error getting mysql_pool connection: ' + err);
  	  		throw err;
  	  	}
      connection.query("select * from keywordentry",function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      });
      
});

app.get("/v1/ownerentries", function(req, res){
    
	var sqlStr = "SELECT * FROM ownerentry";
	console.log(sqlStr);
    pool.getConnection(function(err, connection) {
  		if (err) {
  	  		console.log(' Error getting mysql_pool connection: ' + err);
  	  		throw err;
  	  	}
      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

   
      });
      
});

app.get("/v1/ownerentry/:id", function(req, res){

    var sqlStr = "SELECT * FROM ownerentry WHERE id=" + req.params.id;
	console.log(sqlStr);
    pool.getConnection(function(err, connection) {
  		if (err) {
  	  		console.log(' Error getting mysql_pool connection: ' + err);
  	  		throw err;
  	  	}
	   
      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

 
      });
      
});

app.get("/v1/keywordentry/:id", function(req, res){

	var sqlStr ="SELECT * FROM keywordentry WHERE id=" + req.params.id;
	console.log(sqlStr);
    pool.getConnection(function(err, connection) {
  		if (err) {
  	  		console.log(' Error getting mysql_pool connection: ' + err);
  	  		throw err;
  	  	}
     connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      });
      
});


app.post('/v1/addcashentry', function (req, res) {
  req.setEncoding('utf8');
  var ownerid = req.body.ownerid;
  var categoryid = req.body.categoryid;
  var amount = req.body.amount;
  var description = req.body.description;
  var actualdate = req.body.actualdate;
  var sqlStr = "INSERT INTO cashentry (amount,description,actualdate,ownerid,categoryid) VALUES (" + amount + ",'added manually','"  + actualdate + "'," + ownerid + "," + categoryid + ")";
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}
      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      });
      
});

app.post('/v1/addcategoryentry', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;
  var sqlStr = "INSERT INTO categoryentry (name) VALUES ('" + name + "')";
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}
  
      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      });
      
});

app.post('/v1/addownerentry', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;
  var sqlStr = "INSERT INTO ownerentry (name) VALUES ('" + name + "')";
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}

      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

  
      });
      
});

app.post('/v1/addkeywordentry', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;
  var categoryid = req.body.categoryid;
  var sqlStr = "INSERT INTO keywordentry (name,categoryid) VALUES ('" + name + "'," + categoryid + ")";
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}

      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      });
      
});


app.post('/v1/updatecashentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var ownerid = req.body.ownerid;
  var categoryid = req.body.categoryid;
  var amount = req.body.amount;
  var description = req.body.description;
  var actualdate = req.body.actualdate.substring(0,10);

  var sqlStr = "UPDATE cashentry SET amount=" + amount + ",description='" + description +
  "',actualdate='" + actualdate + "',ownerid=" + ownerid + ",categoryid=" + categoryid +
  " WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}

      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

 
      });
      
});

app.post('/v1/updateestimateentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var amount = req.body.amount;

  var sqlStr = "UPDATE estimateentry SET amount=" + amount +
  " WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}

      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

 
      });
      
});


app.post('/v1/updatecategoryentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;

  var sqlStr = "UPDATE categoryentry SET name='" + name + "' WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}

      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

 
      });
      
});

app.post('/v1/updateownerentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;

  var sqlStr = "UPDATE ownerentry SET name='" + name + "' WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}

      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

    
      });
      
});

app.post('/v1/updatekeywordentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;
  var categoryid = req.body.categoryid;

  var sqlStr = "UPDATE keywordentry SET name='" + name + "', categoryid=" + categoryid + " WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}

      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

   
      });
      
});

app.post('/v1/deletecashentry/:id', function (req, res) {
  req.setEncoding('utf8');

  var sqlStr = "DELETE FROM cashentry WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}

      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json("[]");
          }
      });

      });
      
});

app.post('/v1/deletecategoryentry/:id', function (req, res) {
  req.setEncoding('utf8');

  var sqlStr = "DELETE FROM categoryentry WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}

      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json("[]");
          }
      });

      });
      
});

app.post('/v1/deleteownerentry/:id', function (req, res) {
  req.setEncoding('utf8');

  var sqlStr = "DELETE FROM ownerentry WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}

      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json("[]");
          }
      });

 
      });
      
});

app.post('/v1/deletekeywordentry/:id', function (req, res) {
  req.setEncoding('utf8');

  var sqlStr = "DELETE FROM keywordentry WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err, connection) {
		if (err) {
	  		console.log(' Error getting mysql_pool connection: ' + err);
	  		throw err;
	  	}

      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json("[]");
          }
      });

 
      });
      
});

app.get("/v1/estimateentries", function(req, res){
 
     var sqlStr = "SELECT * FROM estimateentry";
     console.log(sqlStr);
	 pool.getConnection(function(err, connection) {
	  		if (err) {
	  	  		console.log(' Error getting mysql_pool connection: ' + err);
	  	  		throw err;
	  	  	}
      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      });
      
});


//REPORTS
app.get("/v1/reports/sumbymonth", function(req, res){

	
	var sqlStr = "SELECT * FROM monthlytotals";
    console.log(sqlStr);
	 pool.getConnection(function(err, connection) {
	  		if (err) {
	  	  		console.log(' Error getting mysql_pool connection: ' + err);
	  	  		throw err;
	  	  	}
      connection.query(sqlStr,function(err,rows,columns){

          var result = [];
          if(!err) {
              for (var i = 0; i < rows.length; i++) {
                  var lastDayOfTheMonth = new Date(rows[i].yr, rows[i].mnth, 0);

                  result.push({id: 1, amount: rows[i].amount, actualdate:lastDayOfTheMonth, description: 'description', categoryid:1, categoryname:'name', ownerid:1, ownername:'name'});
              }


              res.json(result);
          }
      });

      });
      
});

app.get("/v1/reports/sumbyowner", function(req, res){

	var sqlStr = "SELECT * FROM totalsbyowner";
    console.log(sqlStr);
	 pool.getConnection(function(err, connection) {
	  		if (err) {
	  	  		console.log(' Error getting mysql_pool connection: ' + err);
	  	  		throw err;
	  	  	}
	

        connection.query(sqlStr,function(err,rows){

            var result = [];
            if(!err) {
                for (var i = 0; i < rows.length; i++) {
                    var lastDayOfTheMonth = new Date(2014, 1, 0);

                    result.push({id: 1, amount: rows[i].amount, actualdate:lastDayOfTheMonth, description: 'description', categoryid:1, categoryname:'name', ownerid:rows[i].ownerid, ownername:rows[i].name});
                }


                res.json(result);
              }
        });

        });
        
});

app.get("/v1/reports/sumbyyear/:year", function(req, res){

	

        var start = req.params.year + "-01-01";
        var stop = req.params.year + "-12-31";
        var sqlStr = "SELECT sum(amount) as amount FROM monthlytotals where actualdate>='" + start + "' AND actualdate<='" + stop + "'";
        console.log (sqlStr);

        
   	 pool.getConnection(function(err, connection) {
   	  		if (err) {
   	  	  		console.log(' Error getting mysql_pool connection: ' + err);
   	  	  		throw err;
   	  	  	}
        connection.query(sqlStr ,function(err,rows){

            var result = [];
            if(!err) {
              for (var i = 0; i < rows.length; i++) {
                  result.push({id: 1, amount: rows[i].amount, actualdate:start, description: 'description', categoryid:1, categoryname:'name', ownerid:1, ownername:'name'});
              }
              res.json(result);
            }
        });

        });
        
});

app.get("/v1/reports/sumbymonthbycategory", function(req, res){


	var sqlStr = "SELECT * FROM monthlytotalsbycategory";
    console.log(sqlStr);
	 pool.getConnection(function(err, connection) {
	  		if (err) {
	  	  		console.log(' Error getting mysql_pool connection: ' + err);
	  	  		throw err;
	  	  	}

        connection.query(sqlStr,function(err,rows){

            var result = [];
            if(!err) {
               for (var i = 0; i < rows.length; i++) {
                  var lastDayOfTheMonth = new Date(rows[i].yr, rows[i].mnth, 0);
                  result.push({id: 1, amount: rows[i].amount, actualdate:lastDayOfTheMonth, description: 'description', categoryid:rows[i].categoryid, categoryname:rows[i].name, ownerid:1, ownername:'name'});
               }
              res.json(result);
            }
        });

        });
        
});

app.get("/v1/reports/top10bycategory/:year/:sort", function(req, res){
        var year = req.params.year;
        var sort = req.params.sort;
        var sqlStr="SELECT categoryid, sum(amount) as amount,name,yr FROM monthlytotalsbycategory WHERE yr='" + year + "' GROUP BY categoryid ORDER BY amount " + sort + " LIMIT 10";
        console.log(sqlStr);
       
   	 pool.getConnection(function(err, connection) {
   	  		if (err) {
   	  	  		console.log(' Error getting mysql_pool connection: ' + err);
   	  	  		throw err;
   	  	  	}
        connection.query(sqlStr,function(err,rows){

            var result = [];
            if(!err) {
               for (var i = 0; i < rows.length; i++) {
                  var lastDayOfTheMonth = new Date(year, 1, 0);
                  result.push({id: 1, amount: rows[i].amount, actualdate:lastDayOfTheMonth, description: 'description', categoryid:rows[i].categoryid, categoryname:rows[i].name, ownerid:1, ownername:'name'});
               }
              res.json(result);
            }
        });


        });
        
});


app.get("/v1/reports/retainedbyyear/:year", function(req, res){
    var sqlStr = "SELECT sum(amount) as amount FROM monthlytotals where yr<=" + req.params.year;
    console.log (sqlStr);
	 pool.getConnection(function(err, connection) {
	  		if (err) {
	  	  		console.log(' Error getting mysql_pool connection: ' + err);
	  	  		throw err;
	  	  	}
    connection.query(sqlStr ,function(err,rows){
      var start = req.params.year + "-01-01"
        var result = [];
        if(!err) {
          for (var i = 0; i < rows.length; i++) {
              result.push({id: 1, amount: rows[i].amount, actualdate:start, description: 'description', categoryid:1, categoryname:'name', ownerid:1, ownername:'name'});
          }
          res.json(result);
        }
    });

    });
    
});

app.get("/v1/reports/gainedbyyear/:year", function(req, res){
    var sqlStr = "SELECT sum(amount) as amount FROM monthlytotalsbycategory where amount>=0 AND yr=" + req.params.year + " AND name != 'Transfer' AND name != 'Carry Over'";
    console.log (sqlStr);
	 pool.getConnection(function(err, connection) {
	  		if (err) {
	  	  		console.log(' Error getting mysql_pool connection: ' + err);
	  	  		throw err;
	  	  	}
    connection.query(sqlStr ,function(err,rows){
        var start = req.params.year + "-01-01";
        var result = [];
        if(!err) {
          for (var i = 0; i < rows.length; i++) {
              result.push({id: 1, amount: rows[i].amount, actualdate:start, description: 'description', categoryid:1, categoryname:'name', ownerid:1, ownername:'name'});
          }
          res.json(result);
        }
    });

    });
    
});

app.get("/v1/reports/spentbyyear/:year", function(req, res){
	var sqlStr="SELECT sum(amount) as amount FROM monthlytotalsbycategory where amount<0 AND yr=" + req.params.year + " AND name != 'Transfer' AND name != 'Carry Over'";
    console.log (sqlStr);
	 pool.getConnection(function(err, connection) {
	  		if (err) {
	  	  		console.log(' Error getting mysql_pool connection: ' + err);
	  	  		throw err;
	  	  	}
    connection.query(sqlStr ,function(err,rows){
    	    var start = req.params.year + "-01-01"
        var result = [];
        if(!err) {
          for (var i = 0; i < rows.length; i++) {
              result.push({id: 1, amount: rows[i].amount, actualdate:start, description: 'description', categoryid:1, categoryname:'name', ownerid:1, ownername:'name'});
          }
          res.json(result);
        }
    });

    });
    
});

app.get("/v1/reports/velocitybyyear/:year", function(req, res){
	var sqlStr="SELECT sum(amount) AS amount FROM monthlytotalsbycategory WHERE yr=" + req.params.year + " AND name != 'Transfer' AND name != 'Carry Over'";
    console.log (sqlStr);
	 pool.getConnection(function(err, connection) {
	  		if (err) {
	  	  		console.log(' Error getting mysql_pool connection: ' + err);
	  	  		throw err;
	  	  	}
    connection.query(sqlStr ,function(err,rows){
    	    var start = req.params.year + "-01-01"
        var result = [];
        if(!err) {
          for (var i = 0; i < rows.length; i++) {
              result.push({id: 1, amount: rows[i].amount, actualdate:start, description: 'description', categoryid:1, categoryname:'name', ownerid:1, ownername:'name'});
          }
          res.json(result);
        }
    });

    });
    
});

app.get("/v1/reports/velocitybymonth/:year/:month", function(req, res){
	var sqlStr="SELECT sum(amount) AS amount FROM monthlytotalsbycategory WHERE yr=" + req.params.year +  " AND mnth=" + req.params.month + " AND name != 'Carry Over'";
    console.log (sqlStr);
	 pool.getConnection(function(err, connection) {
	  		if (err) {
	  	  		console.log(' Error getting mysql_pool connection: ' + err);
	  	  		throw err;
	  	  	}
    connection.query(sqlStr ,function(err,rows){
    	    var start = req.params.year + "-01-01"
        var result = [];
        if(!err) {
          for (var i = 0; i < rows.length; i++) {
              result.push({id: 1, amount: rows[i].amount/req.params.month, actualdate:start, description: 'description', categoryid:1, categoryname:'name', ownerid:1, ownername:'name'});
          }
          res.json(result);
        }
    });

    });
    
});

//SNAPSHOTS
app.post('/v1/takesnapshot/:type', function (req, res) {
var type = req.params.type;
var dateFormat = require('dateformat');
var now = new Date();
var name = dateFormat(now, "yyyymmdd");
name += type;
console.log('mysqldump -h ' + dbhost + ' -uroot -p****** ' + dbname + '>' + dumplocation + '/'+ name);
const exec = require('child_process').exec;
var msg = "";
const child = exec('mysqldump -h ' + dbhost + ' -uroot -p' +dbpass + ' ' + dbname + '>' + dumplocation + '/' + name + '.sql', (error, stdout, stderr) => {
	  if (error) {
	    console.error(`exec error: ${error}`);
	    msg = error;
	    return;
	  }
	  console.log(`stdout: ${stdout}`);
	  msg = stdout;
	});
res.send(msg);
});

app.post('/v1/restoresnapshot/:name', function (req, res) {
	var name = req.params.name;
	console.log('mysql -h ' + dbhost + ' -uroot -p****** ' + dbname + '<' + dumplocation + '/'+ name);
	const exec = require('child_process').exec;
	var msg = "";
	const child = exec('mysql -h ' + dbhost + ' -uroot -p' +dbpass + ' ' + dbname + '<' + dumplocation + '/' + name , (error, stdout, stderr) => {
		  if (error) {
		    console.error(`exec error: ${error}`);
		    msg = error;
		    return;
		  }
		  console.log(`stdout: ${stdout}`);
		  msg = stdout;
		});
	res.send(msg);
	});

app.get("/v1/snapshotentries", function(req, res){
	var result = [];
	fs.readdir(dumplocation, (err, files) => {
	  files.forEach(file => {
	    //console.log(file);
	    result.push({id: 1, name: file});
	  });
	  res.json(result);
	})   
          
});

app.post('/v1/deletesnapshotentry/:name', function (req, res) {

	  var fname = dumplocation + "/" + req.params.name;
	  
	  fs.stat(fname, function (err, stats) {
		   //console.log(stats);

		   if (err) {
		       return console.error(err);
		   }

		   fs.unlink(fname,function(err){
		        if(err) return console.log(err);
		        console.log('snapshot deleted successfully');
		   });  
		});
	  res.send("done");
	  
	      
});

var options = {
	key: fs.readFileSync("mockserver.key"),
    cert: fs.readFileSync("mockserver.crt")
}

var server = https.createServer(options, app);
console.log("Server running on " + port);
server.listen(port);
