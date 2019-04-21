<template>
  <div id="updateCompo">
    <div class="cmt-container">
	    <h3>更新物品信息</h3>
	    <span class="hr"></span>
	    <div class="input-group input-group-sm">
		  <span class="input-group-addon glyphicon glyphicon-user" id="sizing-addon1"> 发布人</span>
		  <input type="text" class="form-control" placeholder="发布人" aria-describedby="sizing-addon1" v-model="originMsg.realname">
		</div>
		<div class="input-group input-group-sm">
		  <span class="input-group-addon glyphicon glyphicon-earphone" id="sizing-addon6"> 联系电话</span>
		  <input type="text" class="form-control" placeholder="联系电话" aria-describedby="sizing-addon6" v-model="originMsg.telephone">
		</div>
		<div class="input-group input-group-sm">
		  <span class="input-group-addon glyphicon glyphicon-book" id="sizing-addon2"> 物品名称</span>
		  <input type="text" class="form-control" placeholder="物品名称" aria-describedby="sizing-addon2" v-model="originMsg.obj">
		</div>
		<div class="input-group input-group-sm map-input">
		  <span class="input-group-addon glyphicon glyphicon-paperclip" id="sizing-addon4"> 详细地址</span>
		  <input type="text" class="form-control" placeholder="地点" aria-describedby="sizing-addon4" v-model="originMsg.locationdesc">
		  <span class="label label-default" v-text=" '当前坐标：( ' + this.$store.state.tmpLatlng.lng + ' , ' + this.$store.state.tmpLatlng.lat + ' )' "></span>
		  <router-link type="button" class="btn btn-primary" to="/map" tag="button">查看地图（选择坐标）</router-link>
		</div>
		<el-date-picker
	     v-model="originMsg.timedesc"
	     type="date"
	     placeholder="选择日期"
	     value-format="yyyy-MM-dd">
	  </el-date-picker>
	    <textarea placeholder="请输入拾到物品的详细情况" maxlength="120" v-model="originMsg.objdesc"></textarea>
	    <mt-button type="primary" size="large" @click="updatePost" class="post-btn">更新</mt-button>
	</div>
  </div>
</template>

<script>//2121a8e6-603d-11e9-99c7-00163e1077d1
import { Toast } from "mint-ui";
import $ from 'jquery'

export default {           
  data() {
    return {
      originMsg: {}
    };
  },
  created() {
    this.getOriginMsg();
  },
  methods: {
  	getOriginMsg(){
  		this.originMsg = this.$store.state.tmpObj;
  		this.originMsg.timedesc = this.originMsg.timedesc.replace( /-/g,'');
  	},
  	updatePost(){
  		let year = new Date().getFullYear();
  		let month = new Date().getMonth()+1;
  		month = month < 10 ? "0"+month : month;
  		let day = new Date().getDate();
  		day = day < 10 ? "0"+day : day;
  		let tmpDate =  year + "-" + month + "-" + day;
  		
  		if (this.originMsg.objdesc.trim().length === 0 || this.originMsg.realname.trim().length === 0 || this.originMsg.locationdesc.trim().length === 0 || this.originMsg.obj.trim().length === 0 || this.originMsg.telephone.trim().length === 0 || this.originMsg.timedesc.length === 0) {
	        return Toast("更新内容不能为空！");
	    }
  		if(this.originMsg.telephone.trim().length > 11 || this.originMsg.telephone.trim().length < 8 || isNaN(Number(this.originMsg.telephone.trim())) ){  			
  			return Toast("请输入8-11位联系电话");
  		}
  		if(new Date(this.timedesc).getTime() > new Date().getTime() && this.timedesc != tmpDate){
  				return Toast("请选择正确的时间");
  		}
  		let posttime = Number(this.originMsg.timedesc.replace(/-/g,''));
  		this.originMsg.latlngy = this.$store.state.tmpLatlng.lng;
  	 	this.originMsg.latlngx = this.$store.state.tmpLatlng.lat;
  		var updateObj = {
  			"obj": this.originMsg.obj,
  			"objdesc": this.originMsg.objdesc.trim(),
  			"realname": this.originMsg.realname,
  			"locationdesc": this.originMsg.locationdesc,
  			"telephone": this.originMsg.telephone,
  			"timedesc": posttime,
  			"latlngy": this.originMsg.latlngy,
  			"latlngx": this.originMsg.latlngx,
  			"timestamp": new Date().getTime(),
  			"uuid": this.$store.state.updateUUID
  		};
  		//console.log(updateObj);
  		if(this.$route.path.indexOf("found") != -1){
  			this.$http.post("found/update", JSON.stringify(updateObj),{
	          headers: {
	            contentType: "application/json"
	          }})
	  		.then(response => {
	  			console.log(response);
		        if (response.body.status === "success") {
		        		Toast("更新成功！");
		        		alert("本条信息的uuid为：" + response.body.uuid);
		        		this.$router.push("/found"); 
		        } else {
		          Toast("更新失败！");
		        }
	        }) 
  		}else{
  			this.$http.post("lost/update", JSON.stringify(updateObj),{
	          headers: {
	            contentType: "application/json"
	          }})
	  		.then(response => {
	  			console.log(response);
		        if (response.body.status === "success") {
		        		Toast("更新成功！");
		        		alert("本条信息的uuid为：" + response.body.uuid);
		        		this.$router.push("/lost");
		        } else {
		          Toast("更新失败！");
		        }
	        }) 
  		}
    },
  }
}
</script>

<style lang="scss">
#updateCompo{
	text-decoration: none;
	.cmt-container {
	  padding: 0 20px;	 
	  .hr{
	  	width: 100%;
	  	height: 1px;
	  	display: block;
	  	background: #ccc;
	  	margin: 8px 0;
	  }
	  h3{
	  	font-weight: bold;
	    font-size: 18px;
	    margin: 50px 0 0;
	  }
	  textarea {
	    font-size: 14px;
	    height: 85px;
	    margin: 0;
	  } 
	  .el-date-editor{
			.el-input__prefix, .el-input__suffix{
				top: -7px !important;
			}
		}
	  .post-btn{
	  	width: 8%;
	  	font-family: "microsoft yahei";
	  	font-weight: lighter;
	  }
	  .input-group{
			margin: 8px 0;
			width: 100%;
			
			.input-group-addon{
				box-sizing: border-box;
				width: 8%;
			}
		}
		.map-input{
			.btn{
				width: 11%;
				display: inline-block;
				height: 30px;
				line-height: 30px;
				padding-top: 0;
				font-size: 12px;
				margin-top: -5px;
			}
			input{
				width: 71%;
				display: inline-block;
			}
			.label{
				width: 18%;
				display: inline-block;
				height: 30px;
				line-height: 30px;
				font-size: 12px;
			}
		}
	}
}
</style>
