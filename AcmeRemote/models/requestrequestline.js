/* jshint indent: 2 */

module.exports = function(sequelize, DataTypes) {
  return sequelize.define('requestrequestline', {
    requestid: {
      type: DataTypes.INTEGER,
      allowNull: false,
      primaryKey: true,
      references: {
        model: 'null',
        key: 'null'
      }
    },
    requestlineid: {
      type: DataTypes.INTEGER,
      allowNull: false,
      references: {
        model: 'requestline',
        key: 'requestlineid'
      }
    }
  }, {
    tableName: 'requestrequestline'
  });
};
