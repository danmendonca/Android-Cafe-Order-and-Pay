'use strict';

var url = require('url');


var Login = require('./LoginService');


module.exports.costumersPOST = function costumersPOST (req, res, next) {
  Login.costumersPOST(req.swagger.params, res, next);
};
