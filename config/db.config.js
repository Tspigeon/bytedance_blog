// 数据库配置文件
module.exports = {
  host: 'localhost',
  user: 'root', // 固定用户名
  password: 'root', // 固定密码
  database: 'soultree', // 固定数据库名
  port: 3306, // 默认MySQL端口
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
};