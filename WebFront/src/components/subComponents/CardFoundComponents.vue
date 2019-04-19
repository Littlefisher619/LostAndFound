<template>
  <div id="cardfound">
		<input type="file">
		<button class="post" @click="postImage">提交</button>
  </div>
</template>

<script>
import { Toast } from "mint-ui";
import $ from 'jquery'

export default {
  data() {
    return {
    };
  },
  created() {
  },
  methods: {  
 		postImage(){
 			if($("input").get(0).files[0].size > 0){
 				 $.ajax({
	        //请求地址
	        url: "https://aip.baidubce.com/oauth/2.0/token", 
	        //请求方式
	        type: "POST", 
	        // 返回的数据类型，设置为JSONP方式
	        dataType: "jsonp", 
	        //指定一个查询参数名称来覆盖默认的 jsonp 回调参数名 callback，默认callback
	        jsonp: 'callback', 
	        //设置回调函数名(自定义)
	        jsonpCallback: 'handleResponse', 
	        //传递的值
	        data: { 
	            grant_type: "client_credentials",
	            client_id: "4m8TiDkvP6vBhfjYHyw8cClR",
	            client_secret: "9Z8yoqRympDuHIbCd07WV3PXH0yGsprV"
	        },
	        //请求成功
	        success: function (data) {
	            //请求的数据
							console.log(data)
	        }
	    });
 			}
 		}
  }
}
</script>

<style lang="scss" scoped>

</style>
