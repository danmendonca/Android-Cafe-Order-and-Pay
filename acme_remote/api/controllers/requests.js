'use strict';

var models = require('../../models');
var Sequelize = require('sequelize');
var Costumer = models.costumer;
var Voucher = models.voucher;
var Request = models.request;
var RequestLine = models.requestline;

var voucherType3Price = 100;

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
    var cUuid = req.swagger.params.request.value.costumerUuid;
    var cPin = req.swagger.params.request.value.pin;
    var ErrorResponse;

    //TODO costumer validation
    //TODO generate voucher for every 100€ spent
    if (cUuid && cPin) {
        var rls = req.swagger.params.request.value.requestlines;
        var rlines = [];
        var oldLines = [];

        Request.findAll({
            where: {
                costumerUuid: cUuid
            }
        })
            .then(function (cRequestHistory) { //get lines from old requests
                var oldLinesPromises = cRequestHistory.map(function (oldR) {
                    return RequestLine.findAll({
                        where: {
                            requestId: oldR.id
                        }
                    }).then(function (oldRLines) {
                        for (var i = 0; i < oldRLines.length; i++) {
                            oldLines.push(oldRLines[i]);
                        }
                    })
                });

                Promise.all(oldLinesPromises).then(function () {
                    //create the new request
                    Request.create({
                        costumerUuid: cUuid,
                        number: 0
                    })
                        .then(function (request) {
                            //request.dataValues.requestlines = [];
                            //new way
                            var newLinesPromises = rls.requestlines.map(function (rl) {
                                return RequestLine.create({
                                    quantity: rl.quantity,
                                    unitprice: rl.product.unitprice,
                                    productId: rl.product.id,
                                    requestId: request.id
                                })
                                    .then(function (rl_) {
                                        rlines.push(rl_);
                                    })
                            });

                            Promise.all(newLinesPromises).then(function () {
                                var totalCost = rls.requestlines.reduce(function (acc, cur) {
                                    return acc + cur.quantity * cur.product.unitprice;
                                }, 0);
                                console.log("totalCost: " + totalCost.toString());

                                if (totalCost > 20) {
                                    //new voucher
                                    var vKey = Math.random().toString();
                                    var vType = Math.floor(Math.random() + 1);
                                    //var byteA = getInt64Bytes(vType);
                                    Voucher.create({
                                        costumerUuid: cUuid,
                                        type: vType,
                                        key: vKey,
                                        isused: false,
                                        number: 0
                                    }).then(function (result) { });
                                }

                                var spentBefore = oldLines.reduce(function (acc, cur) {
                                    return acc + cur.quantity * cur.unitprice;
                                }, 0);
                                var spentSinceLast100 = spentBefore % voucherType3Price;

                                spentSinceLast100 += totalCost;
                                if (spentSinceLast100 > voucherType3Price) {
                                    var vKey = Math.random().toString();
                                    Voucher.create({
                                        costumerUuid: cUuid,
                                        type: 3,
                                        key: vKey,
                                        isused: false,
                                        number: 0
                                    })
                                }

                                request.dataValues.pin = cPin;
                                console.log(JSON.stringify(request));
                                res.json(request);
                            })
                        });
                })
            })
    }
    else {
        ErrorResponse.message = "Cannot complete operation";
        res.statusCode = 403;
        res.end(JSON.stringify(ErrorResponse));
    }
}

function getInt64Bytes(x) {
    var bytes = [];
    var i = 4;
    do {
        bytes[--i] = x & (255);
        x = x >> 8;
    } while (i)
    return bytes;
}