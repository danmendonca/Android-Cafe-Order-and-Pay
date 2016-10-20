'use strict';

var url = require('url');


var Requests = require('./RequestsService');


module.exports.costumerRequestsPOST = function costumerRequestsPOST (req, res, next) {
  Requests.costumerRequestsPOST(req.swagger.params, res, next);
};
