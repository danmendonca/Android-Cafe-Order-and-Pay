var express = require('express');
var router = express.Router();


var db = require('../models');

/* GET users listing. */
router.get('/', function(req, res, next) {
  db.costumer.findAll({
    attributes: ['name', 'username']
  }).then(function(users) {
    res.setHeader('Content-Type', 'application/json');
res.end(JSON.stringify(users || {}, null, 2));
  console.log(users)
})
  
});

module.exports = router;
