var fs = require('fs')
  , path = require('path')
  , lodash = require('lodash')
  , Sequelize = require('sequelize'),
  db = {};
var sequelize = new Sequelize('postgres://user:password@localhost:5432/AcmeCafeDB');


sequelize
  .authenticate()
  .then(function (err) {
    console.log('Connection has been established successfully.');
  })
  .catch(function (err) {
    console.log('Unable to connect to the database:', err);
  });



//load each file in this directory and get the exported variable
fs
  .readdirSync(__dirname)
  .filter(function (file) {
    return (file.indexOf('.') !== 0) && (file !== 'index.js')
  })
  .forEach(function (file) {
    var model = sequelize.import(path.join(__dirname, file))
    db[model.name] = model
  });

//many to many
sequelize.models.product.belongsToMany(sequelize.models.request, {through: sequelize.models.requestline});
sequelize.models.request.belongsToMany(sequelize.models.product, {through: sequelize.models.requestline});

//one to many
sequelize.models.costumer.hasMany(sequelize.models.request);
sequelize.models.costumer.hasMany(sequelize.models.voucher);

sequelize.models.request.hasMany(sequelize.models.voucher);
sequelize.models.blacklist.hasMany(sequelize.models.costumer);

sequelize.sync();

module.exports = lodash.extend({
  sequelize: sequelize,
  Sequelize: Sequelize
}, db)