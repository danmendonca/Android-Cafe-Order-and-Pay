'use strict';

var models = require('../../models');
var Sequelize = require('sequelize');
var Costumer = models.costumer;
var Voucher = models.voucher;
var Request = models.request;
var RequestLine = models.requestline;
var BlackList = models.blacklist;

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
    var reqVouchers = req.swagger.params.request.value.vouchers;
    var rls = req.swagger.params.request.value.requestlines;
    var ErrorResponse;
    var answered = false;

    //TODO costumer validation
    //TODO generate voucher for every 100€ spent
    if (cUuid && cPin && reqVouchers.length <= 3) {
        var rlines = [];
        var oldLines = [];

        //Find costumer requests
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
                            var voucherPromises = reqVouchers.map(function (rvoucher) {
                                return Voucher.findOne({
                                    where: {
                                        costumerUuid: cUuid,
                                        isused: false,
                                        id: rvoucher.id
                                    }
                                }).then(function (validVoucher) {
                                    if (validVoucher) {
                                        validVoucher.update({
                                            isused: true,
                                            requestId: request.id
                                        })
                                    }
                                })
                            }); //end voucherPromises

                            var newLinesPromises = rls.map(function (rl) {
                                return RequestLine.create({
                                    quantity: rl.quantity,
                                    unitprice: rl.product.unitprice,
                                    productId: rl.product.id,
                                    requestId: request.id
                                })
                                    .then(function (rl_) {
                                        rlines.push(rl_);
                                    })
                            }); //end newLinesPromises

                            Promise.all(newLinesPromises).then(function () {
                                var totalCost = rls.reduce(function (acc, cur) {
                                    return acc + cur.quantity * cur.product.unitprice;
                                }, 0);
                                console.log("totalCost: " + totalCost.toString());

                                if (totalCost > 20) {
                                    //new voucher
                                    var vKey = Math.random().toString();
                                    var vType = getRandomizer(1, 2);
                                    //var byteA = getInt32Bytes(vType);
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

                                Promise.all(voucherPromises).then(function () {
                                    //stuff?
                                    answered = true;
                                    request.dataValues.pin = cPin;
                                    console.log(JSON.stringify(request));
                                    res.json(request);
                                })
                            })
                        });
                })
            });
    }

    else if (!answered) {
        ErrorResponse.message = "Cannot complete operation";
        res.statusCode = 403;
        res.end(JSON.stringify(ErrorResponse));
    }
}

function getInt32Bytes(x) {
    var bytes = [];
    var i = 4;
    do {
        bytes[--i] = x & (255);
        x = x >> 8;
    } while (i)
    return bytes;
}

function getRandomizer(bottom, top) {
        return Math.floor( Math.random() * ( 1 + top - bottom ) ) + bottom;
}