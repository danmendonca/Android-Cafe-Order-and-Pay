'use strict';

var models = require('../../models');
var Costumer = models.costumer;
var Voucher = models.voucher;
var Request = models.request;
var RequestLine = models.requestline;

module.exports = {
    getCostumerRequests: getCostumerRequests,
    createRequest: createRequest
};

function getCostumerRequests(req, res) {
    if (req.swagger.params.costumer.value.uuid && req.swagger.params.costumer.value.pin) {
        Request.findAll({
            where: {
                costumerUuid: req.swagger.params.costumer.value.uuid
            }
        }).then(function (requests) {
            res.json(requests);
        });
    }
    else {
        var error;
        error.message = "Invalid User";
        res.statusCode = 403;
        res.end(JSON.stringify(error));
    }
}

function createRequest(req, res) {

    if (req.swagger.params.request.value.costumerUuid) {
        var rls = req.swagger.params.request.value.requestlines;
        Request.create({
            costumerUuid: req.swagger.params.request.value.costumerUuid,
            number: 0
        }).then(function (request) {
            var promises = [];
            request.dataValues.requestlines = [];
            rls.requestlines.forEach(function (rl) {
                promises.push(
                    RequestLine.create({
                        quantity: rl.quantity,
                        unitprice: rl.product.unitprice,
                        productId: rl.product.id,
                        requestId: request.id
                    }).then(function (rl_) {
                        request.dataValues.requestlines.push(rl_);
                    })
                );
            });
            Promise.all(promises).then(function () {
                res.json(request);
            })
        });
    }
    else {
        var error;
        error.message = "Cannot complete operation";
        res.statusCode = 403;
        res.end(JSON.stringify(error));
    }
}