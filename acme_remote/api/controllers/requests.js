'use strict';

var models = require('../../models');
var Sequelize = require('sequelize');
var env = process.env.NODE_ENV || "development";

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
    createRequest: createRequest,
    getPublicKey: getPublicKey
};

/**
 * Route call to create a new Request
 * @param {any} req
 * @param {any} res
 */
function createRequest(req, res) {
    //Params
    var cUuid = req.swagger.params.request.value.costumerUuid;
    var reqVouchers = req.swagger.params.request.value.requestvouchers;
    var reqLines = req.swagger.params.request.value.requestlines;

    //Responses
    var RequestResponse = {};
    var ErrorResponse = {};

    //Response params
    var insertedLines = [];
    var insertedVouchers = [];
    var requestNumber = 0;

    //aux
    var oldLines = [];

    if (reqVouchers.length >= 3) {
        //too many vouchers
        ErrorResponse.message = "Too many vouchers";
        sendResponse(res, ErrorResponse, 403);
        return;
    }

    //costumer validation
    costumerValidation(cUuid).then((exist) => {
        if (exist) {
            //costumer exists
            BlackList.count({
                where: {
                    costumerUuid: cUuid
                }
            })
                .then((blacklisted) => {
                    if (blacklisted == 0) {
                        //not blacklisted
                        var numberWrapper = {};
                        numberWrapper.requestNumber = 0;
                        getAllReqAndLines(cUuid, oldLines, numberWrapper).then(() => {
                            //oldLines retrieved
                            Request.create({
                                costumerUuid: cUuid,
                                number: numberWrapper.requestNumber
                            })
                                .then((request) => {
                                    var voucherPromises = useVouchers(request, reqVouchers, insertedVouchers);
                                    Promise.all(voucherPromises).then(valuesVPromises => {
                                        var linesPromises = makeRequestLine(request, reqLines, insertedLines);
                                        Promise.all(linesPromises).then(() => {
                                            voucherCreation(cUuid, oldLines, insertedLines);
                                            RequestResponse.id = request.id;
                                            RequestResponse.number = request.number;
                                            RequestResponse.requestLines = insertedLines;
                                            RequestResponse.requestVouchers = insertedVouchers;
                                            RequestResponse.createdAt = request.createdAt;
                                            sendResponse(res, RequestResponse, 200);
                                        })
                                    }, reason => {
                                        ErrorResponse.message = "Invalid voucher - blacklisted";
                                        sendResponse(res, ErrorResponse, 403);
                                    })
                                        .catch(() => {
                                            ErrorResponse.message = "Invalid voucher - blacklisted";
                                            sendResponse(res, ErrorResponse, 403);
                                        })
                                })
                        })
                    }
                    else {
                        //blacklisted
                        ErrorResponse.message = "Costumer is in blacklist";
                        sendResponse(res, ErrorResponse, 403);
                        return;
                    }
                })
        }
        else {
            //costumer doesn't exist
            ErrorResponse.message = "Costumer doesn't exist";
            sendResponse(res, ErrorResponse, 403);
            return;
        }

    })
        .catch((error) => {
            //failed to get costumer info
            ErrorResponse.message = error;
            sendResponse(res, ErrorResponse, 403);
            return;
        })
}


/**
 * Checks if a costumer exists, is not blacklisted and its credit card is valid in this moment
 * 
 * @param {any} cUuid - costumer unique identifier
 * @returns Promise
 */
function costumerValidation(cUuid) {
    return new Promise(function (resolve, reject) {
        Costumer.findOne({
            where: {
                uuid: cUuid
            }
        }).then((c) => {
            if (c) {
                if (c.creditcarddate >= new Date())
                    resolve(c);
                else {
                    BlackList.create({
                        costumerUuid: cUuid
                    }).then(() => { reject("Invalid credit card"); })
                }
            }
            else
                reject("Costumer not found");
        })
            .catch((e) => {
                reject("Invalid Costumer uuid");
            })
    });
}



/**
 * 
 * @param {any} res
 * @param {any} varToJson
 * @param {any} responseCode
 */
function sendResponse(res, varToJson, responseCode) {

    if (env == "development") {
        var stringified = JSON.stringify(varToJson);
        console.log("\nResponse code: " + responseCode + "\nJson: \n" + stringified + "\n");
    }
    res.statusCode = responseCode;
    res.json(varToJson);
}


/**
 * Gets all requests of a costumer given its uuid, saves the requests requestlines in oldLines and
 * sets the reqNumber +1 higher than the highest found
 * 
 * @param {any} cUuid - costumer Uuid
 * @param {any} oldLines - an array to store the requests requestlines
 * @param {any} reqNumber - the number to store the one after the highest request.number found
 * @returns Promise
 */
function getAllReqAndLines(cUuid, oldLines, reqNumberWrapper) {
    reqNumberWrapper.requestNumber = 0;
    return new Promise(function (resolve, reject) {
        Request.findAll({
            where: {
                costumerUuid: cUuid
            }
        }).then((oldReqs) => {
            oldReqs.forEach((request) => {
                if (request.number > reqNumberWrapper.requestNumber)
                    reqNumberWrapper.requestNumber = request.number;
            });
            reqNumberWrapper.requestNumber++;

            Promise.all(getOldRequestLines(oldReqs, oldLines))
                .then(() => {
                    resolve(true);
                })
                .catch((e) => {
                    reject(e);
                })
        })
    })
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
 * Gets all request lines of requests in a request array and stores them in oldLines 
 * @param {any} oldRequests an array of old requests
 * @param {any} oldLines an array to add the lines retrieved from all old requests
 * @returns Promise array
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
 * @returns Promise array
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
                    else reject(value);
                });
        });
    });
}

/**
 * 
 * 
 * @param {any} request
 * @param {any} rvs
 * @param {any} insertedVouchers
 * @returns
 */
function useVouchers(request, rvs, insertedVouchers) {
    return rvs.map(function (rvoucher) {
        return new Promise(function (resolve, reject) {
            Voucher.findOne({
                where: {
                    costumerUuid: request.costumerUuid,
                    isused: false,
                    id: rvoucher.id
                }
            }).then(function (validVoucher) {

                //if (validVoucher && sigVerifier(rvoucher))
                if (validVoucher) { // TODO verify signature
                    validVoucher.update({
                        isused: true,
                        requestId: request.id
                    }).then((updated) => {
                        insertedVouchers.push(updated);
                        var voucherParam = {};
                        voucherParam.id = updated.id;
                        voucherParam.type = updated.type;
                        voucherParam.signature = updated.signature;
                        resolve(updated);
                    })
                }
                else {
                    reject("NotFound");
                    //blacklist
                    BlackList.create({
                        costumerUuid: request.costumerUuid
                    }).then((b) => { });
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
    if (totalCost >= 20) createRandomVoucher(cUuid);

    var spentBefore = oldLines.reduce(function (acc, cur) {
        return acc + cur.quantity * cur.unitprice;
    }, 0);
    var spentSinceLast100 = spentBefore % voucherType3Price;
    spentSinceLast100 += totalCost;

    if (spentSinceLast100 >= voucherType3Price) createDiscountVoucher(cUuid);
}

/**
 * 
 * 
 * @param {any} v
 * @returns
 */
function getVoucherSignature(v) {
    sign.update(v.id + ' ' + v.cUuid + ' ' + v.type, 'sha1');
    return sign.sign(privateKey);
}

/**
 * 
 * 
 * @param {any} v
 * @returns
 */
function verifyVoucherSignature(v) {
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


function getPublicKey(req, res) {
    sendResponse(res, publicKey, 200);
}