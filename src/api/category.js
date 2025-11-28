import request from '@/request'

///获取类别列表
export function getAllCategorys() {
    return request({
        url: '/categorys',
        method: 'get',
    })
}

///获取所有类别信息
export function getAllCategorysDetail() {
    return request({
        url: '/categorys/detail',
        method: 'get',
    })
}

//根据id查找类别信息
export function getCategoryDetail(id) {
    return request({
        url: `/categorys/detail/${id}`,
        method: 'get',
    })
}