var express = require("express");

var https = require("https");

var fs = require("fs");

var mysql = require ("mysql");

var bodyParser = require ("body-parser");

var app = express();

var filters = "";

const port = 6001;


app.use(bodyParser.urlencoded({
  extended: true
}));
app.use(bodyParser.json())

app.get("/", function(req, res){

res.send("<h1>hello<h2>");

});
app.get("/v1/cashentries", function(req, res){
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);
      var sqlStr = "select cashentry.id,actualdate,amount,ownerid,categoryid,ownerentry.name as ownername, categoryentry.name as categoryname,description from cashentry,ownerentry,categoryentry where (ownerid=amdb.ownerentry.id) and (categoryid=amdb.categoryentry.id)";
      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
  });
});

app.get("/v1/reports/sumbyrange/:before/:after", function(req, res){
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);
      var sqlStr = "SELECT SUM(amount) FROM cashentry WHERE actualdate>='" + req.params.after + "' AND actualdate <= '" + req.params.before + "'";
      connection.query(sqlStr,function(err,rows){
          connection.release();
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
});

app.get("/v1/reports/sumupto/:before", function(req, res){
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);
      var sqlStr = "SELECT SUM(amount) AS amount FROM cashentry WHERE actualdate < '" + req.params.before + "'";
      connection.query(sqlStr,function(err,rows){
          connection.release();
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
});


app.get("/v1/cashentries/:filters", function(req, res){
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      var sqlStr = "select cashentry.id,actualdate,amount,ownerid,categoryid,ownerentry.name as ownername, categoryentry.name as categoryname, description from cashentry,ownerentry,categoryentry where (ownerid=amdb.ownerentry.id) and (categoryid=amdb.categoryentry.id)" + " and " + req.params.filters;
      console.log(sqlStr);
      connection.query(sqlStr ,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
  });
});

app.get("/v1/cashentry/:id", function(req, res){
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);
      console.log('look up id is' + req.params.id);

      connection.query("SELECT * FROM cashentry WHERE id=" + req.params.id,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
  });
});

app.get("/v1/categoryentry/:id", function(req, res){
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);
      console.log('look up id is' + req.params.id);

      connection.query("SELECT * FROM categoryentry WHERE id=" + req.params.id,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
  });
});

app.get("/v1/categoryentries", function(req, res){
  pool.getConnection(function(err,connection){

      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);
      req.setEncoding('utf8');
      var sqlStr = "SELECT * FROM categoryentry";
      //console.log(sqlStr);
      //var options = { sql: sqlStr, nestTables: true };
      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
  });
});

app.get("/v1/keywordentries", function(req, res){
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query("select * from keywordentry",function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
  });
});

app.get("/v1/ownerentries", function(req, res){
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query("select * from ownerentry",function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
  });
});

app.get("/v1/ownerentry/:id", function(req, res){
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);
      console.log('look up id is' + req.params.id);

      connection.query("SELECT * FROM ownerentry WHERE id=" + req.params.id,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
  });
});

app.get("/v1/keywordentry/:id", function(req, res){
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);
      console.log('look up id is' + req.params.id);

      connection.query("SELECT * FROM keywordentry WHERE id=" + req.params.id,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
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
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

});

app.post('/v1/addcategoryentry', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;
  var sqlStr = "INSERT INTO categoryentry (name) VALUES ('" + name + "')";
  console.log(sqlStr);
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

});

app.post('/v1/addownerentry', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;
  var sqlStr = "INSERT INTO ownerentry (name) VALUES ('" + name + "')";
  console.log(sqlStr);
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

});

app.post('/v1/addkeywordentry', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;
  var categoryid = req.body.categoryid;
  var sqlStr = "INSERT INTO keywordentry (name,categoryid) VALUES ('" + name + "'," + categoryid + ")";
  console.log(sqlStr);
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
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
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

});

app.post('/v1/updateestimateentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var amount = req.body.amount;

  var sqlStr = "UPDATE estimateentry SET amount=" + amount +
  " WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

});


app.post('/v1/updatecategoryentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;

  var sqlStr = "UPDATE categoryentry SET name='" + name + "' WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

});

app.post('/v1/updateownerentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;

  var sqlStr = "UPDATE ownerentry SET name='" + name + "' WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

});

app.post('/v1/updatekeywordentry/:id', function (req, res) {
  req.setEncoding('utf8');
  var name = req.body.name;
  var categoryid = req.body.categoryid;

  var sqlStr = "UPDATE keywordentry SET name='" + name + "', categoryid=" + categoryid + " WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

});

app.post('/v1/deletecashentry/:id', function (req, res) {
  req.setEncoding('utf8');

  var sqlStr = "DELETE FROM cashentry WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json("[]");
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

});

app.post('/v1/deletecategoryentry/:id', function (req, res) {
  req.setEncoding('utf8');

  var sqlStr = "DELETE FROM categoryentry WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json("[]");
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

});

app.post('/v1/deleteownerentry/:id', function (req, res) {
  req.setEncoding('utf8');

  var sqlStr = "DELETE FROM ownerentry WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json("[]");
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

});

app.post('/v1/deletekeywordentry/:id', function (req, res) {
  req.setEncoding('utf8');

  var sqlStr = "DELETE FROM keywordentry WHERE id=" + req.params.id;
  console.log(sqlStr);
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query(sqlStr,function(err,rows){
          connection.release();
          if(!err) {
              res.json("[]");
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
});

});

app.get("/v1/estimateentries", function(req, res){
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query("select * from estimateentry",function(err,rows){
          connection.release();
          if(!err) {
              res.json(rows);
          }
      });

      connection.on('error', function(err) {
            res.json({"code" : 100, "status" : "Error in connection database"});
            return;
      });
  });
});

app.get("/v1/reports/sumbymonth", function(req, res){
  pool.getConnection(function(err,connection){
      if (err) {
        console.log('cannot connect ');
        res.json({"code" : 100, "status" : "Error in connection database"});
        return;
      }

      console.log('connected as id ' + connection.threadId);

      connection.query("SELECT * FROM monthlytotals",function(err,rows,columns){
          connection.release();
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
});

app.get("/v1/reports/sumbyowner", function(req, res){
    pool.getConnection(function(err,connection){
        if (err) {
          console.log('cannot connect ');
          res.json({"code" : 100, "status" : "Error in connection database"});
          return;
        }

        console.log('connected as id ' + connection.threadId);

        connection.query("SELECT * FROM totalsbyowner",function(err,rows){
            connection.release();
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
});

app.get("/v1/reports/sumbyyear/:year", function(req, res){
    pool.getConnection(function(err,connection){
        if (err) {
          console.log('cannot connect ');
          res.json({"code" : 100, "status" : "Error in connection database"});
          return;
        }

        console.log('connected as id ' + connection.threadId);
        var start = req.params.year + "-01-01";
        var stop = req.params.year + "-12-31";
        var sqlStr = "SELECT sum(amount) as amount FROM cashentry where actualdate>='" + start + "' AND actualdate<='" + stop + "'";
        console.log (sqlStr);
        connection.query(sqlStr ,function(err,rows){
            connection.release();
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
});

app.get("/v1/reports/sumbymonthbycategory", function(req, res){
    pool.getConnection(function(err,connection){
        if (err) {
          console.log('cannot connect ');
          res.json({"code" : 100, "status" : "Error in connection database"});
          return;
        }

        console.log('connected as id ' + connection.threadId);

        connection.query("SELECT * FROM monthlytotalsbycategory",function(err,rows){
            connection.release();
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
});

app.get("/v1/reports/top10bycategory/:year/:sort", function(req, res){
    pool.getConnection(function(err,connection){
        if (err) {
          console.log('cannot connect ');
          res.json({"code" : 100, "status" : "Error in connection database"});
          return;
        }

        console.log('connected as id ' + connection.threadId);
        var year = req.params.year;
        var sort = req.params.sort;
        sqlStr="SELECT categoryid, amount,name,yr FROM monthlytotalsbycategory WHERE yr='" + year + "' GROUP BY categoryid, amount,name,yr ORDER BY amount " + sort + " LIMIT 10";
        console.log(sqlStr);
        connection.query(sqlStr,function(err,rows){
            connection.release();
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
});

app.get("/v1/testgcdwrite", function(req, res){

  const Datastore = require('@google-cloud/datastore');
  const projectId = 'sefa-am-project';
  const datastore = Datastore({
    projectId: projectId
  });

  const kind = 'Task';
  const name = 'sampletask1';
  const taskKey = datastore.key([kind, name]);
  const task = {
    key: taskKey,
    data: {
      description: 'Buy milk'
    }
  };

  datastore.save(task).then(() => {
    console.log(`Saved ${task.key.name}: ${task.data.description}`);
  })
  .catch((err) => {
    console.error('ERROR:', err);
  });
});


var pool      =    mysql.createPool({
    connectionLimit : 100, //important
    host : '172.18.0.1',
    user : 'amdbuser',
    password : 'amdb',
    database : 'amdb',
    port : 6000,
    debug : false
});


var options = {

key: fs.readFileSync("mockserver.key"),

cert: fs.readFileSync("mockserver.crt")

}

var server = https.createServer(options, app);

console.log("Server running on " + port);

server.listen(port);
