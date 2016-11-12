'use strict';

var models = require('../../models');
var Sequelize = require('sequelize');
var Costumer = models.costumer;
var BlackList = models.blacklist;

var env = process.env.NODE_ENV || "development";

module.exports = {
    getBlacklisted: getBlacklisted,
    addToBlacklist: addToBlacklist,
    removeFromBl: removeFromBl
};

function getBlacklisted(req, res) {
    BlackList.findAll().then((values) => {
        var blacklisted = {};
        blacklisted['blacklisted'] = values;
        res.json(blacklisted);
    });
}

function addToBlacklist(req, res) {
    var toBlacklist = req.swagger.params.blacklist.value; 
    var cUuid = toBlacklist.costumerUuid;
    BlackList.count({
        where: {
            costumerUuid: cUuid
        }
    }).then((c) => {
        if (c == 0) {
            BlackList.create({
                costumerUuid: cUuid
            }).then((bl) => {
                res.json(bl);
            });
        }
        else {
            var ErrorResponse = {};
            ErrorResponse.message = 'Already in blacklist';
            res.statusCode = 403;
            res.json(ErrorResponse);
        }
    })
}


/**
 * 
 * 
 * @param {any} req
 * @param {any} res
 */
function removeFromBl(req, res){
    var ErrorResponse = {};
    var ResponseMessage = {};
    var cUuid = req.swagger.params.cuuid.value;

    BlackList.destroy({
        where : {
            costumerUuid : cUuid
        }
    }).then((affected) => {
        if(affected == 0){
            ErrorResponse.message = "Costumer not found in blacklist";
            sendResponse(res, ErrorResponse, 404);
            return;
        }

        ResponseMessage.message = "Removed from blacklist";
         sendResponse(res, ResponseMessage, 200);
    })    
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