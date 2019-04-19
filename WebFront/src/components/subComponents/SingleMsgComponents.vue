<template>
  <div id="singleMsg">
  	<div class="well well-sm">
  		<h3>{{singleMsg.obj}}</h3>
    	<h4>发布时间：{{singleMsg.timestamp*1000 | dateFormat}}</h4>
  	</div>
  	<fieldset>
  		<legend>物品详情:</legend>
  		<div class="msg-item"><i class="glyphicon glyphicon-user"></i><span class="msg-title">发布人：</span><span>{{singleMsg.realname}}</span></div>
  		<div class="msg-item"><i class="glyphicon glyphicon-earphone"></i><span class="msg-title">联系电话：</span><span>{{singleMsg.telephone}}</span></div>
  		<div class="msg-item"><i class="glyphicon glyphicon-paperclip"></i><span class="msg-title">{{ eventFlag }}地点：</span><span>{{singleMsg.locationdesc}}</span><span class="singleLatlng" v-text=" '物品大致坐标：( ' + singleMsg.latlngy + ' , ' + singleMsg.latlngx + ' )' "></span></div>
  		<div class="msg-item"><i class="glyphicon glyphicon-file"></i><span class="msg-title">详细信息：</span><span>{{singleMsg.objdesc}}</span></div>
  		<div class="msg-item"><i class="glyphicon glyphicon-calendar"></i><span class="msg-title">{{ eventFlag }}时间 ：</span><span>{{singleMsg.timedesc}}</span></div>
  		<button class="btn btn-primary mapBtn" @click="goMap">查看地图</button>
  	</fieldset>
  	<button class="btn btn-primary removeBtn" @click="remove">删除本条信息</button>
  	<button class="btn btn-primary updateBtn" @click="update">修改本条信息</button>
  	<div class="input-group checkForm">
		  <input type="text" class="form-control form-text" placeholder="请输入对应的uuid" v-model="temUUID">
		  <button class="btn btn-primary checkBtn" @click="checkuuid">提交</button>
		  <button class="btn btn-primary backBtn" @click="hideForm">返回</button>
		</div>
  </div>
</template>

<script>
	import { Toast } from "mint-ui";
	import $ from 'jquery'
	
	export default {
	  data() {
	    return {
	    	singleMsg: {},
	    	eventFlag: "",
	    	temUUID: "",
	    	flag: 0   //默认删除情况
	    };
	  },
	  created() {
	  	this.render();
	  	this.eventFlag = this.$route.path.indexOf("found") != -1 ? "拾取" : "丢失";
	  },
	  methods: {
	    render(){
	    	this.singleMsg = this.$store.state.tmpObj;
	    },
	    goMap(){
	    	this.$store.commit("updateSingleMsgLatlng", {lng: this.singleMsg.latlngy, lat: this.singleMsg.latlngx});
    		this.$router.push("/map"); 
	    },
	    remove(){
	    	$(".checkForm").show();  	
	    	this.flag = 0;
	    },
	    update(){
	    	$(".checkForm").show();  	
	    	this.flag = 1; 
	    },
	    hideForm(){
	    	$(".checkForm").hide();
	    	this.temUUID = "";
	    },
	    checkuuid(){
	    	if(this.temUUID.trim().length){
	    		// 只要在all请求时返回uuid,就可以在客户端直接比较，没必要请求
	    		if(this.$route.path.indexOf("found") != -1){
	    			this.$http.post("found/getbyuuid", JSON.stringify({uuid: this.temUUID}),{
		          headers: {
		            contentType: "application/json"
		          }})
			  		.then(response => {
			  			console.log(response);
				        if (response.body.length != 0) {
				        	if(this.flag === 0){
				        		//删除
				        		this.$http.post("found/remove", JSON.stringify({uuid: this.temUUID}),{
						          headers: {
						            contentType: "application/json"
						          }})
							  		.then(response => {
							  			console.log(response);
								        if (response.body.status === "success") {
								        		Toast("删除成功");
								        		this.temUUID = "";
	    											$(".checkForm").hide();
	    											this.$router.push("/found"); 
								        } else {
								          Toast("删除失败");
								        }
						        })
				          }else if(this.flag === 1){
				          	//更新
				          	this.$router.push(this.$route.path + "/update"); 
				          	this.$store.commit("updateUUID", this.temUUID);
				          }
				        } else {
				          Toast("uuid不存在");
				        }
		        })
	    		}else{
	    			this.$http.post("lost/getbyuuid", JSON.stringify({uuid: this.temUUID}),{
		          headers: {
		            contentType: "application/json"
		          }})
			  		.then(response => {
			  			console.log(response);
				        if (response.body.length != 0) {
				        	if(this.flag === 0){
				        		// 删除
				        		this.$http.post("lost/remove", JSON.stringify({uuid: this.temUUID}),{
						          headers: {
						            contentType: "application/json"
						          }})
							  		.then(response => {
							  			console.log(response);
								        if (response.body.status === "success") {
								        		Toast("删除成功");
								        		this.temUUID = "";
	    											$(".checkForm").hide();
	    											this.$router.push("/lost"); 
								        } else {
								          Toast("删除失败");
								        }
						        })
							  	}else if(this.flag === 1){
							  		//更新
							  		
							  		this.$router.push(this.$route.path + "/update"); 
							  	}
				        } else {
				          Toast("uuid不存在");
				        }
		        })
	    		} 
	    	}else{
	    		Toast("提交内容不得为空");
	    	}
	    }
	  }
 };
</script>

<style lang="scss" scoped>
#singleMsg{
	padding: 0 20px;
	
	.well{
		font-family: "microsoft yahei";
		height: 100px;
		margin: 20px 0;
		padding-top: 0;
		
		h3{
			font-family: "microsoft yahei";
			font-size: 22px;
			font-weight: bold;
			margin-bottom: 20px;
		}
		h4{
			font-family: "microsoft yahei";
			font-size: 14px;
		}
	}
	fieldset{
		position: relative;
    padding: 10px 20px;
    margin: 0 2px;
    border: 1px solid silver;
     
    legend {
    	font-family: "microsoft yahei";
      border: 0;
      width: auto;
      font-weight: bold;
    }
    .msg-item{
    	margin-bottom: 10px;
    	.glyphicon{
	    	display: inline-block;
	    	margin-right: 15px;
	    	color: gray;
	    }
	    span{
	    	display: inline-block;
	    	margin-right: 15px;
	    }
	     .singleLatlng{
	     	 margin-left: 20px;
	     	 color: gray;
	     }
	     .msg-title{
	    	font-weight: bold;
	    }
    }
    .mapBtn{
    	position: absolute;
    	left: 30%;
    	bottom: 20px;
    } 
  }
  .removeBtn{
  	display: inline-block;
  	margin-top: 30px;
  	margin-right: 30px;
  }
  .updateBtn{
  	display: inline-block;
  	margin-top: 30px;
  }
  .checkForm{
  	display: none;
  	position: absolute;
  	width: 40%;
  	left: 30%;
  	top: 35%;
  	padding: 10px 20px;
  	border: 1px solid rgba(51,122,183,0.5);
  	background: white;
  	
  	.form-control{
  		width: 65%;
  		display: inline-block;
  	}
  	.btn{
  		display: inline-block;
  		width: 14%;
  		height: 35px;
  		margin-left: 10px;
  		text-align: center;
  	}
  }
}
</style>
