'use strict';

var models = require('../../models');
var Sequelize = require('sequelize');
var Costumer = models.costumer;
var Voucher = models.voucher;
var Request = models.request;
var RequestLine = models.requestline;
var Product = models.product;
var BlackList = models.blacklist;

var voucherType3Price = 100;

//Signatures
var crypto = require('crypto');
var keypair = require('keypair');
var pair = keypair(368);
console.log(pair);
var privateKey = pair.private;
var publicKey = pair.public;
const sign = crypto.createSign('sha1');

module.exports = {
    getCostumerRequests: getCostumerRequests,
    createRequest: createRequest
};

function getCostumerRequests(req, res) {
    var pinLogin = req.swagger.params.pinLogin.value;
    var cUuid = pinLogin.uuid;
    var cPin = pinLogin.pin;
    if (cUuid && cPin) {
        var isValid = false;
        Costumer.count({
            where: {
                uuid: cUuid,
                pin: cPin
            }
        }).then(function (c) {
            if (c == 1) {
                Request.findAll({
                    where: {
                        costumerUuid: cUuid
                    }
                }).then(function (requests) {
                    Voucher.findAll({
                        attributes: ['id', 'type', 'isused']
                        ,
                        where: {
                            costumerUuid: cUuid,
                            isused: false
                        }
                    }).then(function (unusedVouchers) {
                        var Consult = {};
                        Consult['requests'] = requests;
                        if (unusedVouchers && unusedVouchers.length > 0)
                            Consult['vouchers'] = unusedVouchers;
                        res.json(Consult);
                        //console.log(JSON.stringify(Consult));
                    })
                });
            }
            else {
                var ErrorResponse = {};
                ErrorResponse.message = "Invalid User";
                res.statusCode = 403;
                res.json(ErrorResponse);
            }
        })
    }
    else {
        var error = {};
        error.message = "Invalid User";
        res.statusCode = 403;
        res.end(JSON.stringify(error));
    }
}

/**
 * 
 * 
 * @param {any} req
 * @param {any} res
 */
function createRequest(req, res) {
    var cUuid = req.swagger.params.request.value.costumerUuid;
    var reqVouchers = req.swagger.params.request.value.requestvouchers;
    var rls = req.swagger.params.request.value.requestlines;
    var ErrorResponse = {};
    var answered = false;

    //TODO costumer validation
    //TODO generate voucher for every 100€ spent
    if (cUuid && reqVouchers.length <= 3) {
        var rlines = [];
        var oldLines = [];

        var isValid = false;
        Costumer.count({
            where: {
                uuid: cUuid
            }
        }).then(function (c) {
            if (c == 1) {
                //Find costumer requests
                Request.findAll({
                    where: {
                        costumerUuid: cUuid
                    }
                })
                    .then(function (cRequestHistory) { //get lines from old requests
                        var oldLinesPromises = getOldRequestLines(cRequestHistory, oldLines);
                        Promise.all(oldLinesPromises).then(function () {
                            //create the new request
                            Request.create({
                                costumerUuid: cUuid,
                                number: 0
                            })
                                .then(function (request) {
                                    var voucherPromises = useVouchers(request, reqVouchers);

                                    Promise.all(voucherPromises).then(function () {
                                        //stuff?
                                        answered = true;
                                        console.log(JSON.stringify(request));

                                        BlackList.count({
                                            where: {
                                                costumerUuid: request.costumerUuid
                                            }
                                        }).then((isListed) => {
                                            if (isListed == 0) {
                                                var newLinesPromises = makeRequestLine(request, rls, rlines);
                                                Promise.all(newLinesPromises).then(function (param) {
                                                    voucherCreation(cUuid, oldLines, rlines);
                                                    res.json(request);
                                                });
                                            }
                                            else {
                                                ErrorResponse.message = "BlackList Costumer";
                                                res.statusCode = 403;
                                                res.json(ErrorResponse);
                                            }
                                        })
                                            .catch(() => {
                                                ErrorResponse.message = "BlackList Costumer";
                                                res.statusCode = 403;
                                                res.json(ErrorResponse);
                                            })
                                    })
                                        .catch((msg) => {
                                            ErrorResponse.message = "BlackList Costumer";
                                            res.statusCode = 403;
                                            res.json(ErrorResponse);
                                        })
                                });
                        });
                    });
            }
            else {
                ErrorResponse = {};
                ErrorResponse.message = "Invalid User";
                res.statusCode = 403;
                res.json(ErrorResponse);
            }
        });

    }

    else if (!answered) {
        ErrorResponse.message = "Cannot complete operation";
        res.statusCode = 403;
        res.end(JSON.stringify(ErrorResponse));
    }
}

/**
 * generates a number between @bottom and @top
 * 
 * @param {number} bottom - inclusive integer that is the lowest possible
 * @param {number} top - inclusive integer that is the highest possible
 * @returns an integer
 */
function getRandomizer(bottom, top) {
    return Math.floor(Math.random() * (1 + top - bottom)) + bottom;
}


/**
 * 
 * 
 * @param {any} oldRequests
 * @param {any} oldLines
 * @returns
 */
function getOldRequestLines(oldRequests, oldLines) {
    return oldRequests.map(function (oldR) {
        return new Promise(function (resolve, reject) {
            RequestLine.findAll({
                where: {
                    requestId: oldR.id
                }
            }).then(function (rls) {
                for (var i = 0; i < rls.length; i++) {
                    oldLines.push(rls[i]);
                }
                resolve(rls);
            })
        });
    });
}

/**
 * adds in the db, the request lines of a new request
 * 
 * @param {any} request - a request to associate the request lines
 * @param {any} rls - an array of request lines to add
 * @param {any} addedLines - an array to store the promises
 * @returns [Promise]
 */
function makeRequestLine(request, rls, addedLines) {
    return rls.map(function (rl) {

        return new Promise(function (resolve, reject) {

            Product.findOne({
                where: {
                    id: rl.productId,
                    active: true
                }
            })
                .then(function (value) {
                    if (value) {
                        var prdct = value.dataValues;
                        RequestLine.create({
                            quantity: rl.quantity,
                            unitprice: prdct.unitprice,
                            productId: prdct.id,
                            requestId: request.id
                        })
                            .then((added) => {
                                addedLines.push(added);
                                resolve(added);
                            })
                    }
                    else
                        reject(value);
                });
        });
    });
}

function useVouchers(request, rvs) {
    return rvs.map(function (rvoucher) {
        return new Promise(function (resolve, reject) {
            Voucher.findOne({
                where: {
                    costumerUuid: request.costumerUuid,
                    isused: false,
                    id: rvoucher.id
                }
            }).then(function (validVoucher) {
                if (validVoucher) {
                    validVoucher.update({
                        isused: true,
                        requestId: request.id
                    }).then((updated) => {
                        resolve(updated);
                    })
                }
                else {
                    //blacklist
                    BlackList.create({
                        costumerUuid: request.costumerUuid
                    }).then(function (blacklist) {
                        reject(blacklist);
                        //throw "Invalid Voucher usage causes ban";
                    })
                }
            })
        })
    }); //end voucherPromises
}

/**
 * creates and add the vouchers of a new request, if there's need to
 * 
 * @param {any} cUuid - costumer Uuid
 * @param {any} oldLines - request lines from older requests
 * @param {any} newLines - the lines of the recent added request
 */
function voucherCreation(cUuid, oldLines, newLines) {
    var totalCost = newLines.reduce(function (acc, cur) {
        return acc + cur.quantity * cur.unitprice;
    }, 0);

    //random voucher creation
    if (totalCost > 20) createRandomVoucher(cUuid);

    var spentBefore = oldLines.reduce(function (acc, cur) {
        return acc + cur.quantity * cur.unitprice;
    }, 0);
    var spentSinceLast100 = spentBefore % voucherType3Price;
    spentSinceLast100 += totalCost;
    
    if (spentSinceLast100 > voucherType3Price) createDiscountVoucher(cUuid);
}

function getVoucherSignature(v)
{
	sign.update(v.id + ' ' + v.cUuid + ' ' + v.type, 'sha1');
	return sign.sign(privateKey);
}

function verifyVoucherSignature(v)
{
	var verifier = crypto.createVerify('sha1');
	verifier.update(v.id + ' ' + v.cUuid + ' ' + v.type, 'sha1')
	return verifier.verify(publicKey, v.signature);
}

/**
 * creates a voucher of type 1 or 2
 * 
 * @param {any} cUuid - costumer uuid to associate the voucher with
 */
function createRandomVoucher(cUuid) {
    Voucher.create({
        costumerUuid: cUuid,
        type: getRandomizer(1, 2),
        signature: "",
        isused: false
	}).then((v) => {
		v.update({
			signature: getVoucherSignature(v)
		})
	});
}

/**
 * Creates a voucher of type 3 and associates it with a costumer
 * 
 * @param {any} cUuid - costumer uuid to associate the voucher with
 */
function createDiscountVoucher(cUuid) {
    Voucher.create({
        costumerUuid: cUuid,
        type: 3,
        signature: "",
        isused: false
    }).then((v) => {
		v.update({
			signature: getVoucherSignature(v)
		})
	});
}
