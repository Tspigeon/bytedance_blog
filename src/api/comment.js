import request from '@/request'

//根据文章id获取评论列表
export function getCommentsByArticle(id) {
    return request({
        url: `/comments/article/${id}`,
        method: 'get'
    })
}
//上传评论功能
export function publishComment(comment, token) {
    return request({
        headers: { 'Authorization': token },
        url: '/comments/create/change',
        method: 'post',
        data: comment
    })
}
//删除评论
export function deleteCommentById(id) {
    return request({
        headers: {
            'Content-Type': 'text/plain'
        },
        url: '/comments/delete',
        method: 'post',
        data: id
    })
}