'use strict';

var url = require('url');


var Costumer = require('./CostumerService');


module.exports.blacklistPOST = function blacklistPOST (req, res, next) {
  Costumer.blacklistPOST(req.swagger.params, res, next);
};

module.exports.costumerMakerequestPOST = function costumerMakerequestPOST (req, res, next) {
  Costumer.costumerMakerequestPOST(req.swagger.params, res, next);
};
