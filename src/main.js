// import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import lodash from 'lodash'
//导入全局样式表
import '@/assets/css/global.css'
import '@/assets/theme/index.css'
import '@/assets/icon/iconfont.css'

import { formatTime } from "./utils/time";

// 高德地图api
import AMap from 'vue-amap';
Vue.use(AMap);

// 初始化vue-amap
AMap.initAMapApiLoader({
    // 申请的高德key
    key: '8a5e7290c1f6cafa60910d037d5511f4',
});

Vue.config.productionTip = false

Object.defineProperty(Vue.prototype, '$_', { value: lodash })


Vue.directive('title', function(el, binding) {
        document.title = el.dataset.title
    })
    // 格式话时间
Vue.filter('format', formatTime)

new Vue({
    el: '#app',
    router,
    store,
    template: '<App/>',
    components: { App }
})