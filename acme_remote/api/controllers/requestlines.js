'use strict';

var models = require('../../models');
var Sequelize = require('sequelize');
var Request = models.request;
var RequestLine = models.requestline;
var Voucher = models.voucher;

module.exports = {
    getRequestlines: getRequestlines,
};


function getRequestlines(req, res){
    var requestId = req.swagger.params.requestId.value;
    var linesPromise = RequestLine.findAll({
        where: {
            requestId: requestId
        }
    });
    var vouchersPromise = Voucher.findAll({
        where: {
            requestId : requestId
        }
    })

    linesPromise.then((requestlines) =>{
        vouchersPromise.then((vouchers) => {
            var ans = {};
            ans.requestlines = requestlines;
            ans.vouchers = vouchers;
            var ansJson = JSON.stringify(ans);
            console.log(ansJson);
            res.json(ans);
        })
    });
}