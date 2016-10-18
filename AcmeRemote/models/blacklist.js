module.exports = function (sequelize, DataTypes) {
    var Blacklist = sequelize.define('blacklist', {
        timestamp: {
            type: DataTypes.DATE,
            allowNull: false
        }
    }, { tablename: 'blacklist' });
    return Blacklist;
}