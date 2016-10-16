/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('request', {
    requestid: {
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true
    },
    costumerid: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'costumer',
        key: 'costumerid'
      }
    }
  }, {
    tableName: 'request'
  });
};
