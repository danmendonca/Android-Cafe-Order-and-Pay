'use strict';

var url = require('url');


var Request = require('./RequestService');


module.exports.costumerMakerequestPOST = function costumerMakerequestPOST (req, res, next) {
  Request.costumerMakerequestPOST(req.swagger.params, res, next);
};
