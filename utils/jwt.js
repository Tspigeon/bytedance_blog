// JWT工具模块
const jwt = require('jsonwebtoken');

// JWT配置
const JWT_SECRET = process.env.JWT_SECRET || 'your_jwt_secret_key'; // 生产环境中应使用环境变量
const JWT_EXPIRES_IN = '1h'; // token有效期1小时

/**
 * 生成JWT token
 * @param {Object} payload - 要编码的用户信息
 * @returns {string} 生成的token
 */
function generateToken(payload) {
  try {
    const token = jwt.sign(payload, JWT_SECRET, { expiresIn: JWT_EXPIRES_IN });
    return token;
  } catch (error) {
    console.error('生成token失败:', error);
    throw new Error('生成token失败');
  }
}

/**
 * 验证JWT token
 * @param {string} token - 要验证的token
 * @returns {Object} 解码后的用户信息
 */
function verifyToken(token) {
  try {
    const decoded = jwt.verify(token, JWT_SECRET);
    return decoded;
  } catch (error) {
    // 处理不同类型的token错误
    if (error.name === 'TokenExpiredError') {
      throw new Error('token已过期');
    } else if (error.name === 'JsonWebTokenError') {
      throw new Error('无效的token');
    }
    throw new Error('token验证失败');
  }
}

/**
 * 从请求头中提取token
 * @param {Object} req - Express请求对象
 * @returns {string|null} 提取的token或null
 */
function extractTokenFromHeader(req) {
  const authHeader = req.headers.authorization;
  if (!authHeader) return null;
  
  // Bearer token格式
  const [bearer, token] = authHeader.split(' ');
  if (bearer !== 'Bearer' || !token) return null;
  
  return token;
}

module.exports = {
  generateToken,
  verifyToken,
  extractTokenFromHeader,
  JWT_EXPIRES_IN
};