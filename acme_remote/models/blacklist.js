module.exports = function (sequelize, DataTypes) {
    var Blacklist = sequelize.define('blacklist', {    }, { tablename: 'blacklist' });
    return Blacklist;
}