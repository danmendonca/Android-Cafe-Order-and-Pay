module.exports = function(sequelize, DataTypes){
    var RequestLine = sequelize.define('requestline', {
        quantity : {
            type: DataTypes.INTEGER,
            allowNull: false
        },
        unitprice: {
            type: DataTypes.FLOAT,
            allowNull: false
        }
    }, {tableName: 'requestline'});

    return RequestLine;
}