var express = require('express');
var router = express.Router();

/* GET menu listing. */
router.get('/', function(req, res, next) {
  /* reply with the current list of products */  
  res.send('respond with a resource');
});

module.exports = router;