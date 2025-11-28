import request from '@/request'

//登录接口
export function login(account, password) {
    const data = {
        account,
        password
    }
    return request({
        url: '/login',
        method: 'post',
        data
    })
}

//退出登录接口
export function logout(token) {
    return request({
        headers: { 'Authorization': token },
        url: '/logout',
        method: 'get'
    })
}

//注册接口
export function register(account, nickname, password) {
    const data = {
        account,
        nickname,
        password
    }
    return request({
        url: '/register',
        method: 'post',
        data
    })
}

//获取当前用户信息
export function getUserInfo(token) {
    return request({
        headers: { 'Authorization': token },
        url: '/users/currentUser',
        method: 'get'
    })
}

//修改用户信息
export function userModify(data) {
    return request({
        url: '/users/modify',
        method: 'post',
        data: JSON.stringify(data),
    })
}