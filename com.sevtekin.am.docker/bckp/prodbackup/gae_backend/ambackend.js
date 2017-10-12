var express = require("express");

var mysql = require ("mysql");

var bodyParser = require ("body-parser");


var app = express();

var filters = "";


var dbuser = process.env.dbuser;
var dbpass = process.env.dbpass;
var dbconn = process.env.dbconn;
var dbname = process.env.dbname;
var dbport = process.env.dbport;


const connection = mysql.createConnection({
      socketPath: '/cloudsql/' + dbconn,
      user: dbuser,
      password: dbpass,
      database: dbname,
      port: dbport
    });


app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(bodyParser.json())

app.get("/", function(req, res){

res.send("<h1>Hello<h2>");

});
app.get("/v1/cashentries", function(req, res){

      console.log("connecting to " + dbname + " using " + dbuser + "@" + dbconn);
      var sqlStr = "select cashentry.id,actualdate,amount,ownerid,categoryid,ownerentry.name as ownername, categoryentry.name as categoryname,description from cashentry,ownerentry,categoryentry where (ownerid=amdb.ownerentry.id) and (categoryid=amdb.categoryentry.id)";
      connection.connect(function(err) {
        if (err) {
          console.error('Error:- ' + err.stack);
          return;
        }

        console.log('Connected Id:- ' + connection.threadId);
    });
      connection.query(sqlStr,function(err,rows){
          if(!err) {
              console.log("Query ran");
              res.json(rows);
          }
      });
});

app.get("/v1/reports/sumbyrange/:before/:after", function(req, res){

      var sqlStr = "SELECT SUM(amount) FROM cashentry WHERE actualdate>='" + req.params.after + "' AND actualdate <= '" + req.params.before + "'";
      connection.query(sqlStr,function(err,rows){
          var result = [];
          if(!err) {
            for (var i = 0; i < rows.length; i++) {
              result.push({id: 1, amount: rows[i].amount, actualdate:'2014-01-01', description: 'description', categoryid: 1, categoryname: 'category name', ownerid:1, ownername:'name'});
            }
            res.json(result);

          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

app.get("/v1/reports/sumupto/:before", function(req, res){

      var sqlStr = "SELECT SUM(amount) AS amount FROM cashentry WHERE actualdate < '" + req.params.before + "'";
      connection.query(sqlStr,function(err,rows){
          var result = [];
          if(!err) {
            for (var i = 0; i < rows.length; i++) {
              result.push({id: 1, amount: rows[i].amount, actualdate:'2014-01-01', description: 'description', categoryid: 1, categoryname: 'category name', ownerid:1, ownername:'name'});
            }
            res.json(result);


          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});


app.get("/v1/cashentries/:filters", function(req, res){


      var sqlStr = "select cashentry.id,actualdate,amount,ownerid,categoryid,ownerentry.name as ownername, categoryentry.name as categoryname, description from cashentry,ownerentry,categoryentry where (ownerid=amdb.ownerentry.id) and (categoryid=amdb.categoryentry.id)" + " and " + req.params.filters;
      console.log(sqlStr);
      connection.query(sqlStr ,function(err,rows){
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

app.get("/v1/cashentry/:id", function(req, res){

      console.log('look up id is' + req.params.id);

      connection.query("SELECT * FROM cashentry WHERE id=" + req.params.id,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

app.get("/v1/categoryentry/:id", function(req, res){

    console.log('look up id is' + req.params.id);

      connection.query("SELECT * FROM categoryentry WHERE id=" + req.params.id,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

app.get("/v1/categoryentries", function(req, res){

      req.setEncoding('utf8');
      var sqlStr = "SELECT * FROM categoryentry";
      // console.log(sqlStr);
      // var options = { sql: sqlStr, nestTables: true };
      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

app.get("/v1/keywordentries", function(req, res){


      connection.query("select * from keywordentry",function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

app.get("/v1/ownerentries", function(req, res){


      connection.query("select * from ownerentry",function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

app.get("/v1/ownerentry/:id", function(req, res){

    console.log('look up id is' + req.params.id);

      connection.query("SELECT * FROM ownerentry WHERE id=" + req.params.id,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

app.get("/v1/keywordentry/:id", function(req, res){

    console.log('look up id is' + req.params.id);

      connection.query("SELECT * FROM keywordentry WHERE id=" + req.params.id,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});


app.post('/v1/addcashentry', function (req, res) {
  req.setEncoding('utf8');
  var ownerid = req.body.ownerid;
  console.log(ownerid);
  var categoryid = req.body.categoryid;
  console.log(categoryid);
  var amount = req.body.amount;
  console.log(amount);
  var description = req.body.description;
  console.log(description);
  var actualdate = req.body.actualdate;
  console.log(actualdate);
  var sqlStr = "INSERT INTO cashentry (amount,description,actualdate,ownerid,categoryid) VALUES (" + amount + ",'" + description + "','"  + actualdate + "'," + ownerid + "," + categoryid + ")";
  console.log(sqlStr);

      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });

});

app.post('/v1/addcategoryentry', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;
  var sqlStr = "INSERT INTO categoryentry (name) VALUES ('" + name + "')";
  console.log(sqlStr);


      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });

});

app.post('/v1/addownerentry', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;
  var sqlStr = "INSERT INTO ownerentry (name) VALUES ('" + name + "')";
  console.log(sqlStr);


      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });

});

app.post('/v1/addkeywordentry', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;
  var categoryid = req.body.categoryid;
  var sqlStr = "INSERT INTO keywordentry (name,categoryid) VALUES ('" + name + "'," + categoryid + ")";
  console.log(sqlStr);


      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
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


      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });

});

app.post('/v1/updateestimateentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var amount = req.body.amount;

  var sqlStr = "UPDATE estimateentry SET amount=" + amount +
  " WHERE id=" + req.params.id;
  console.log(sqlStr);


      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });

});


app.post('/v1/updatecategoryentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;

  var sqlStr = "UPDATE categoryentry SET name='" + name + "' WHERE id=" + req.params.id;
  console.log(sqlStr);


      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });

});

app.post('/v1/updateownerentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;

  var sqlStr = "UPDATE ownerentry SET name='" + name + "' WHERE id=" + req.params.id;
  console.log(sqlStr);


      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });

});

app.post('/v1/updatekeywordentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;
  var categoryid = req.body.categoryid;

  var sqlStr = "UPDATE keywordentry SET name='" + name + "', categoryid=" + categoryid + " WHERE id=" + req.params.id;
  console.log(sqlStr);


      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });

});

app.post('/v1/deletecashentry/:id', function (req, res) {
  req.setEncoding('utf8');

  var sqlStr = "DELETE FROM cashentry WHERE id=" + req.params.id;
  console.log(sqlStr);


      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json("[]");
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });

});

app.post('/v1/deletecategoryentry/:id', function (req, res) {
  req.setEncoding('utf8');

  var sqlStr = "DELETE FROM categoryentry WHERE id=" + req.params.id;
  console.log(sqlStr);


      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json("[]");
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });

});

app.post('/v1/deleteownerentry/:id', function (req, res) {
  req.setEncoding('utf8');

  var sqlStr = "DELETE FROM ownerentry WHERE id=" + req.params.id;
  console.log(sqlStr);


      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json("[]");
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });

});

app.post('/v1/deletekeywordentry/:id', function (req, res) {
  req.setEncoding('utf8');

  var sqlStr = "DELETE FROM keywordentry WHERE id=" + req.params.id;
  console.log(sqlStr);


      connection.query(sqlStr,function(err,rows){

          if(!err) {
              res.json("[]");
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });

});

app.get("/v1/estimateentries", function(req, res){


      connection.query("select * from estimateentry",function(err,rows){

          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

app.get("/v1/reports/sumbymonth", function(req, res){



      connection.query("SELECT * FROM monthlytotals",function(err,rows,columns){

          var result = [];
          if(!err) {
              for (var i = 0; i < rows.length; i++) {
                  var lastDayOfTheMonth = new Date(rows[i].yr, rows[i].mnth, 0);

                  result.push({id: 1, amount: rows[i].amount, actualdate:lastDayOfTheMonth, description: 'description', categoryid:1, categoryname:'name', ownerid:1, ownername:'name'});
              }


              res.json(result);
          }
      });

      connection.on('error', function(err) {
            res.json({"[AM][BACKEND]" : 100, "status" : "Error in connection database"});
            return;
      });
});

app.get("/v1/reports/sumbyowner", function(req, res){




        connection.query("SELECT * FROM totalsbyowner",function(err,rows){

            var result = [];
            if(!err) {
                for (var i = 0; i < rows.length; i++) {
                    var lastDayOfTheMonth = new Date(2014, 1, 0);

                    result.push({id: 1, amount: rows[i].amount, actualdate:lastDayOfTheMonth, description: 'description', categoryid:1, categoryname:'name', ownerid:rows[i].ownerid, ownername:rows[i].name});
                }


                res.json(result);
              }
        });

        connection.on('error', function(err) {
              res.json({"code" : 100, "status" : "Error in connection database"});
              return;
        });
});

app.get("/v1/reports/sumbyyear/:year", function(req, res){



        var start = req.params.year + "-01-01";
        var stop = req.params.year + "-12-31";
        var sqlStr = "SELECT sum(amount) as amount FROM cashentry where actualdate>='" + start + "' AND actualdate<='" + stop + "'";
        console.log (sqlStr);
        connection.query(sqlStr ,function(err,rows){

            var result = [];
            if(!err) {
              for (var i = 0; i < rows.length; i++) {
                  result.push({id: 1, amount: rows[i].amount, actualdate:start, description: 'description', categoryid:1, categoryname:'name', ownerid:1, ownername:'name'});
              }
              res.json(result);
            }
        });

        connection.on('error', function(err) {
              res.json({"code" : 100, "status" : err});
              return;
        });
});

app.get("/v1/reports/sumbymonthbycategory", function(req, res){




        connection.query("SELECT * FROM monthlytotalsbycategory",function(err,rows){

            var result = [];
            if(!err) {
               for (var i = 0; i < rows.length; i++) {
                  var lastDayOfTheMonth = new Date(rows[i].yr, rows[i].mnth, 0);
                  result.push({id: 1, amount: rows[i].amount, actualdate:lastDayOfTheMonth, description: 'description', categoryid:rows[i].categoryid, categoryname:rows[i].name, ownerid:1, ownername:'name'});
               }
              res.json(result);
            }
        });

        connection.on('error', function(err) {
              res.json({"code" : 100, "status" : "Error in connection database"});
              return;
        });
});

app.get("/v1/reports/top10bycategory/:year/:sort", function(req, res){
        var year = req.params.year;
        var sort = req.params.sort;
        sqlStr="SELECT categoryid, amount,name,yr FROM monthlytotalsbycategory WHERE yr='" + year + "' GROUP BY categoryid, amount,name,yr ORDER BY amount " + sort + " LIMIT 10";
        console.log(sqlStr);
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

        connection.on('error', function(err) {
              res.json({"code" : 100, "status" : "Error in connection database"});
              return;
        });
});

var projectid = process.env.projectid;
var dbinstance = process.env.dbinstance;

app.get("/v1/getbackups", function(req, res){

	
	
	var google = require('googleapis');
	var sqlAdmin = google.sqladmin('v1beta4');

	authorize(function(authClient) {	
		
		var request = {
				project: projectid,  
				instance: dbinstance, 
				auth: authClient,
		};
	
		var handlePage = function(err, response) {
		    if (err) {
		      console.error(err);
		      return;
		    }

		    var itemsPage = response['items'];
		    if (!itemsPage) {
		      return;
		    }
		    //for (var i = 0; i < itemsPage.length; i++) {
		    //  console.log(JSON.stringify(itemsPage[i], null, 2));
		    //}

		    if (response.nextPageToken) {
		      request.pageToken = response.nextPageToken;
		      sqlAdmin.backupRuns.list(request, handlePage);
		    }
		    res.json(itemsPage);
		  };

		  sqlAdmin.backupRuns.list(request, handlePage);
		});
	
	function authorize(callback) {
		  google.auth.getApplicationDefault(function(err, authClient) {
		    if (err) {
		      console.error('authentication failed: ', err);
		      return;
		    }
		    if (authClient.createScopedRequired && authClient.createScopedRequired()) {
		      var scopes = ['https://www.googleapis.com/auth/cloud-platform'];
		      authClient = authClient.createScoped(scopes);
		    }
		    callback(authClient);
		  });
		}
		
	// /
});


const PORT = process.env.PORT || 7080;
app.listen(PORT, () => {
  console.log('App listening on port %s', PORT);
});
