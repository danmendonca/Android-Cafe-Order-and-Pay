'use strict';

var models = require('../../models');
var Sequelize = require('sequelize');
var Costumer = models.costumer;
var BlackList = models.blacklist;

module.exports = {
    getBlacklisted: getBlacklisted,
    addToBlacklist: addToBlacklist
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