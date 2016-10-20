'use strict';

var url = require('url');


var Costumers = require('./CostumersService');


module.exports.costumerRequestsPOST = function costumerRequestsPOST (req, res, next) {
  Costumers.costumerRequestsPOST(req.swagger.params, res, next);
};

module.exports.costumersGET = function costumersGET (req, res, next) {
  Costumers.costumersGET(req.swagger.params, res, next);
};

module.exports.costumersPOST = function costumersPOST (req, res, next) {
  Costumers.costumersPOST(req.swagger.params, res, next);
};

module.exports.costumersRegisterPOST = function costumersRegisterPOST (req, res, next) {
  Costumers.costumersRegisterPOST(req.swagger.params, res, next);
};
