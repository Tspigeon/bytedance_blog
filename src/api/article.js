import request from '@/request'

//获取文章列表
export function getArticles(query, page) {
    return request({
        url: '/articles',
        method: 'post',
        data: {
            page: page.pageNumber,
            pageSize: page.pageSize,
            year: query.year,
            month: query.month,
            tagId: query.tagId,
            categoryId: query.categoryId
        }
    })
}

//获取我的文章
export function getMyArchives() {
    return request({
        url: '/articles/mylist',
        method: 'get'
    })
}

//获取最热文章列表
export function getHotArtices() {
    return request({
        url: '/articles/hot',
        method: 'post',
    })
}

// 获取最新文章列表
export function getNewArtices() {
    return request({
        url: '/articles/new',
        method: 'post'
    })
}

// 根据文章id查询文章详情
export function viewArticle(id) {
    return request({
        url: `/articles/view/${id}`,
        method: 'post'
    })
}

//上传文章
export function publishArticle(article, token) {
    return request({
        headers: { 'Authorization': token },
        url: '/articles/publish',
        method: 'post',
        data: article
    })
}

// 搜索文章
export function searchArticle(search) {
    return request({
        url: '/articles/search',
        method: 'post',
        data: { "search": search }
    })
}

//获取归档列表
export function listArchives() {
    return request({
        url: '/articles/listArchives',
        method: 'post'
    })
}

//删除文章
export function deleteArticleById(id) {
    return request({
        headers: {
            'Content-Type': 'text/plain'
        },
        url: '/articles/delete',
        method: 'post',
        data: id
    })
}

export function getArticleById(id) {
    return request({
        url: `/articles/${id}`,
        method: 'post'
    })
}