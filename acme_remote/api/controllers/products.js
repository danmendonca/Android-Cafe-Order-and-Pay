'use strict';

var models = require('../../models');
var Product = models.product;

module.exports = {
    getProducts: getProducts,
    createProduct: createProduct
};


/**
 * returns an array of products in json format, with all products if no lastUpdatedAt param with date is given,
 * or all modified/added products since that date
 * 
 * @param {any} req
 * @param {any} res
 */
function getProducts(req, res) {
    var lastUpdatedAt = req.swagger.params.lastUpdatedAt.value;
    if(!lastUpdatedAt){
        lastUpdatedAt = new Date(1900, 1);
    }
    Product.findAll({
        where: {
            updatedAt: {
                $gt: lastUpdatedAt
            }
        }
    }).then(function (values) {
    	var prods = {};
    	prods['products'] = values;
        res.json(prods);
    });
}


/**
 * 
 * 
 * @param {any} req
 * @param {any} res
 */
function createProduct(req, res) {

    var productParam = req.swagger.params.productParam.value;
    var uprice = productParam.unitprice
    var name = productParam.name;
    var active = productParam.active;   
    if (!name){
        res.status(403);
        throw "invalid product name";
    }
    if (!active){
        res.status(403);
        throw "active param not set";
    }
    if (!uprice){
        res.status(403);
        throw "invalid unitprice param";
    }

    Product.create({
        name: name,
        active: active,
        unitprice: uprice
    }).then(function (value) {
        res.json(value);
    }).catch(function (err) {
        res.status(403);
        res.end(JSON.stringify(err));
    });
}
