// 用户模型
const bcrypt = require('bcryptjs');
const db = require('../utils/db.js');

class User {
  constructor(id, username, password = null) {
    this.id = id;
    this.username = username;
    this.password = password; // 仅在需要时设置，不返回给客户端
  }

  // 根据用户名查找用户
  static async findByUsername(account) {
    const sql = 'SELECT * FROM treesoul_admin WHERE username = ?';
    const rows = await db.query(sql, [account]);
    if (rows.length === 0) return null;
    
    const userData = rows[0];
    console.log("userData:", userData);
    return new User(
      userData.id,
      userData.username,
      userData.password,
    );
  }

  // 根据邮箱查找用户
  static async findByEmail(email) {
    const sql = 'SELECT * FROM treesoul_admin WHERE email = ?';
    const rows = await db.query(sql, [email]);
    if (rows.length === 0) return null;
    
    const userData = rows[0];
    return new User(
      userData.id,
      userData.username,
      userData.password,
    );
  }

  // 创建新用户
  static async create(username, email, password) {
    // 加密密码
    const hashedPassword = await bcrypt.hash(password, 10);
    
    const sql = `
      INSERT INTO treesoul_admin (username, password) 
      VALUES (?, ?)
    `;
    
    try {
      const result = await db.query(sql, [username, hashedPassword]);
      return new User(
        result.insertId,
        username,
        email,
        null, // 不返回密码
        new Date(),
        new Date()
      );
    } catch (error) {
      // 处理重复用户名或邮箱
      if (error.code === 'ER_DUP_ENTRY') {
        throw new Error('用户名或邮箱已存在');
      }
      throw error;
    }
  }

  // 验证密码
  async verifyPassword(password) {
    if (!this.password) return false;
    if(password == this.password) return true;
    // return await bcrypt.compare(password, this.password);
  }

  // 转换为可安全返回给客户端的数据
  toSafeObject() {
    return {
      id: this.id,
      username: this.username,
      email: this.email,
    };
  }
}

module.exports = User;