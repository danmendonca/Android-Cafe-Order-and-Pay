'use strict';

var models = require('../../models');
var Costumer = models.costumer;
var Request = models.request;
var RequestLine = models.requestline;
var Voucher = models.voucher;

var env = process.env.NODE_ENV || "development";

module.exports = {
    getCostumers: getCostumers,
    createUser: createUser,
    logMe: logMe,
    getCostumerRequests: getCostumerRequests
};

//sql costumer insertion INSERT INTO public.costumer VALUES ('daniel', 'dm', 'pass', '1234', '123456789', current_date, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', now(), now());

/**
 * 
 * 
 * @param {any} req
 * @param {any} res
 */
function getCostumers(req, res) {
    Costumer.findAll({
        attributes: ["username", "uuid", "name", "creditcarddate"]
    }).then(function (costumers) {
        var ctmrs = {};
        //ctmrs['costumers'] = costumers;
        sendResponse(res, costumers, 200);
    });
}

/**
 * 
 * 
 * @param {any} req
 * @param {any} res
 */
function createUser(req, res) {
    var generatedPin = Math.floor(1000 + Math.random() * 9000);
    var myCostumer = req.swagger.params.registerParam.value;
    Costumer.create({
        name: myCostumer.name,
        username: myCostumer.username,
        password: myCostumer.password,
        pin: generatedPin.toString(),
        creditcardnumber: myCostumer.creditcardnumber,
        creditcarddate: myCostumer.creditcarddate
    }).then(function (value) {
        sendResponse(res, value, 200);
    })
        .catch(function (err) {
            console.log(err);
            sendResponse(res, "Failed to create user", 403);
        })
}

/**
 * 
 * 
 * @param {any} req
 * @param {any} res
 */
function logMe(req, res) {
    var login = req.swagger.params.loginParam.value;

    var ErrorResponse = {};

    Costumer.findOne({
        where: {
            username: login.username,
            password: login.password
        }
    }).then((validC) => {
        if (validC)
            sendResponse(res, validC, 200);
        else {
            ErrorResponse.message = "Invalid Login";
            sendResponse(res, ErrorResponse, 403);
        }
    });

}

/**
 * 
 * 
 * @param {any} req
 * @param {any} res
 */
function getCostumerRequests(req, res) {

    var ErrorResponse = {};

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
                        attributes: ['id', 'type', 'isused', 'signature']
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
                        sendResponse(res, Consult, 200);
                    })
                });
            }
            else {
                ErrorResponse.message = "Invalid User";
                sendResponse(res, ErrorResponse, 403);
            }
        })
    }
    else {
        ErrorResponse.message = "Failed to verify";
        sendResponse(res, ErrorResponse, 403);
    }
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