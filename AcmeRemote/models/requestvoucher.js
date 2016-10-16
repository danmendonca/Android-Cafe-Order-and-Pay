/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('requestvoucher', {
    voucherid: {
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'null',
        key: 'null'
      }
    },
    requestid: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'request',
        key: 'requestid'
      }
    }
  }, {
    tableName: 'requestvoucher'
  });
};
