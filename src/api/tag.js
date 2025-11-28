import request from '@/request'

//查询所有标签
export function getAllTags() {
    return request({
        url: '/tags',
        method: 'get',
    })
}

//查询所有标签信息
export function getAllTagsDetail() {
    return request({
        url: '/tags/detail',
        method: 'get',
    })
}

//获取最热标签
export function getHotTags() {
    return request({
        url: '/tags/hot',
        method: 'get',
    })
}

// 根据id查询标签细节
export function getTagDetail(id) {
    return request({
        url: `/tags/detail/${id}`,
        method: 'get',
    })
}