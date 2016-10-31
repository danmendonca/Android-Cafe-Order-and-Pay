module.exports = function (sequelize, DataTypes) {
    var Voucher = sequelize.define('voucher', {
        type: {
            type: DataTypes.INTEGER,
            allowNull: false
        },
        isused: {
            type: DataTypes.BOOLEAN,
            allowNull: false
        },
        signature: {
            type: DataTypes.TEXT,
            allowNull: false,
            unique: 'VOUCHER_KEY_UNIQUE'
        }
    }, { tablename: 'voucher' });
    return Voucher;
}