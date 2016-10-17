var fs = require('fs')
    , path = require('path')
    , lodash = require('lodash')
    , Sequelize = require('sequelize'),
    db= {};
var sequelize = new Sequelize('postgres://postgres:admin@localhost:5432/AcmeCafeDB');


sequelize
  .authenticate()
  .then(function(err) {
    console.log('Connection has been established successfully.');
  })
  .catch(function (err) {
    console.log('Unable to connect to the database:', err);
  });



fs
  .readdirSync(__dirname)
  .filter(function(file) {
	return (file.indexOf('.') !== 0) && (file !== 'index.js')
   })
   .forEach(function(file) {
   	 var model = sequelize.import(path.join(__dirname, file))
	 db[model.name] = model
   })

module.exports = lodash.extend({
	sequelize: sequelize, 
	Sequelize: Sequelize
}, db)