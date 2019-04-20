// 入口文件
import Vue from 'vue'
// 1.1 导入路由的包
import VueRouter from 'vue-router'
// 1.2 安装路由
Vue.use(VueRouter)

// 注册 vuex
import Vuex from 'vuex'
Vue.use(Vuex)

var store = new Vuex.Store({
  state: { // this.$store.state.***
    tmpLatlng: {lng: 119.204124, lat: 26.064756},  // 地图组件中定位坐标
    tmpObj: {},  // 查看单件物品的临时存储信息
    singleMsgLatlng: {lng: -1, lat: -1},  // 保存单件物品的坐标
    updateUUID: ""  //进行修改操作时临时存储uuid
  },
  mutations: { // this.$store.commit('方法的名称', '按需传递唯一的参数')
    updateTmpLatlng(state, obj){
    	state.tmpLatlng.lng = obj.lng;
    	state.tmpLatlng.lat = obj.lat;
    	//console.log(state.tmpLatlng);
    },
    updateTmpObj(state, obj){
    	state.tmpObj = obj;
    	//console.log(state.tmpObj);
    },
    updateSingleMsgLatlng(state, obj){
    	state.singleMsgLatlng.lng = obj.lng;
    	state.singleMsgLatlng.lat = obj.lat;
    },
    updateUUID(state,uuid){
    	state.updateUUID = uuid;
    }
  },
  getters: { // this.$store.getters.***
    
  }
})

// 导入格式化时间的插件
import moment from 'moment'
// 定义全局的过滤器
Vue.filter('dateFormat', function (dataStr, pattern = "YYYY-MM-DD") {
  return moment(dataStr).format(pattern)
})

// 2.1 导入 vue-resource
import VueResource from 'vue-resource'
// 2.2 安装 vue-resource
Vue.use(VueResource)
// 设置请求的根路径
Vue.http.options.root = 'http://47.107.171.219:5000';
// 全局设置 post 时候表单数据格式组织形式   application/x-www-form-urlencoded
Vue.http.options.emulateJSON = true;


// 导入 MUI 的样式
import './lib/mui/css/mui.min.css'
// 导入扩展图标样式
import './lib/mui/css/icons-extra.css'

import $ from 'jquery'
import 'bootstrap/css/bootstrap.css'

// 导入 Mint-UI 中的组件   
import MintUI from 'mint-ui'
Vue.use(MintUI)
import 'mint-ui/lib/style.css'

// 导入swiper
import VueAwesomeSwiper from 'vue-awesome-swiper'
import 'swiper/dist/css/swiper.css'
Vue.use(VueAwesomeSwiper)

// 导入 ElementUI 
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI);

// 1.3 导入自己的 router.js 路由模块
import router from './router.js'


// 导入 App 根组件
import app from './App.vue'

var vm = new Vue({
  el: '#app',
  render: c => c(app),
  router, // 1.4 挂载路由对象到 VM 实例上
  store // 挂载 store 状态管理对象
})