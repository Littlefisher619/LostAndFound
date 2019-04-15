import VueRouter from 'vue-router'

// 导入对应的路由组件
import HomeContainer from './components/tabbar/HomeContainer.vue'
import MemberContainer from './components/tabbar/MemberContainer.vue'
import LostComponents from './components/subComponents/LostComponents.vue'
import FoundComponents from './components/subComponents/FoundComponents.vue'
import MapComponents from './components/subComponents/MapComponents.vue'
import SingleMsgComponents from './components/subComponents/SingleMsgComponents.vue'

// 3. 创建路由对象
var router = new VueRouter({
  routes: [ // 配置路由规则
    { path: '/', redirect: '/home' },
    { path: '/home', component: HomeContainer },
    { path: '/member', component: MemberContainer },
    { path: '/lost', component: LostComponents },
    { path: '/found', component: FoundComponents },
    { path: '/map', component: MapComponents },
    { path: '/found/:index', component: SingleMsgComponents },
    { path: '/lost/:index', component: SingleMsgComponents }
  ],
  linkActiveClass: 'mui-active' // 覆盖默认的路由高亮的类，默认的类叫做 router-link-active
})

// 把路由对象暴露出去
export default router