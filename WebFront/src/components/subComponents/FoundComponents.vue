<template>
  <div id="FoundCompo">
  	<div id="searchBar">
	    <div>
	        <select id="searchSelect">
	        	<option>按物品名称查询</option>
	        	<option>按拾取地点查询</option>      	
	        	<option>按时间范围查询</option>
	        </select>
	    </div>
	    <input type="text" class="form-control" id="search-text">
	    <button type="button" class="btn btn-default" id="search-go" @click="searchGo">搜索</button>
	    <button type="button" class="btn btn-default" id="show-allFound" @click="showAllMsg">显示所有</button>
	</div>
    <ul class="mui-table-view">
      <li class="mui-table-view-cell mui-media" v-for="(item,index) in newslist" :key="item.timestamp" @click="goView(index)">
        <a>
          <div class="mui-media-body">
            <h1>{{ item.obj }}</h1>
            <span>{{ item.objdesc }}</span>
            <p class='mui-ellipsis'>
              <span>发布时间：{{ item.timestamp*1000 | dateFormat}}</span>
              <span>拾取地点：{{ item.locationdesc }}</span>
              <span>拾取人：{{ item.realname }}</span>
            </p>
          </div>
        </a>
      </li>
    </ul>
    <div class="cmt-container">
	    <h3>发表招领启示</h3>
	    <span class="hr"></span>
	    <div class="input-group input-group-sm">
		  <span class="input-group-addon glyphicon glyphicon-user" id="sizing-addon1"></span>
		  <input type="text" class="form-control" placeholder="发布人" aria-describedby="sizing-addon1" v-model="realname">
		</div>
		<div class="input-group input-group-sm">
		  <span class="input-group-addon glyphicon glyphicon-earphone" id="sizing-addon6"></span>
		  <input type="text" class="form-control" placeholder="联系电话" aria-describedby="sizing-addon6" v-model="telephone">
		</div>
		<div class="input-group input-group-sm">
		  <span class="input-group-addon glyphicon glyphicon-book" id="sizing-addon2"></span>
		  <input type="text" class="form-control" placeholder="物品名称" aria-describedby="sizing-addon2" v-model="obj">
		</div>
		<div class="input-group input-group-sm map-input">
		  <span class="input-group-addon glyphicon glyphicon-paperclip" id="sizing-addon4"></span>
		  <input type="text" class="form-control" placeholder="拾取地点" aria-describedby="sizing-addon4" v-model="locationdesc">
		  <span class="label label-default" v-text=" '当前坐标：( ' + this.$store.state.tmpLatlng.lng + ' , ' + this.$store.state.tmpLatlng.lat + ' )' "></span>
		  <router-link type="button" class="btn btn-primary" to="/map" tag="button">查看地图（选择坐标）</router-link>
		</div>
		 <el-date-picker
	     v-model="timedesc"
	     type="date"
	     placeholder="选择日期"
	     value-format="yyyy-MM-dd">
	  </el-date-picker>
	    <textarea placeholder="请输入拾到物品的详细情况（最多120字）" maxlength="120" v-model="objdesc"></textarea>
	    <mt-button type="primary" size="large" @click="foundPublish" class="post-btn">发表招领启示</mt-button>
	    <mt-button size="large" @click="showcard" class="card-btn">快速发布学生卡招领启示</mt-button>
	    <div class="input-group imageForm">
				<el-upload
				  action="http://47.107.171.219:5000/ocr"
				  list-type="picture-card"
				  :on-preview="handlePictureCardPreview"
				  :on-remove="handleRemove"
				  :auto-upload="false"
				  ref="upload"
				  :on-success="onSuccess"
				  :limit="1">
				  <i class="el-icon-plus"></i>
				</el-upload>
				<el-dialog :visible.sync="dialogVisible">
				  <img width="100%" :src="dialogImageUrl" alt="">
				</el-dialog>
			  <button class="btn btn-primary imageBtn" @click="pubcard">提交</button>
			  <button class="btn btn-primary backBtn" @click="hideImageForm">返回</button>
			</div>
	</div>
  </div>
</template>

<script>
import { Toast } from "mint-ui";
import $ from 'jquery'

export default {
  data() {
    return {
      newslist: [],
      objdesc: "",
      realname: "",
      locationdesc: "",
      obj: "",
      telephone: "",
      timedesc: "",
      latlngy: 0,
      latlngx: 0,
      existFlag: 0,
      dialogImageUrl: '',
      dialogVisible: false
    };
  },
  created() {
    this.getNewsList();
  },
  methods: {  
    getNewsList() {
      // 获取新闻列表  
      this.$http.get("found/all").then(response => {
        if (response.status === 200) {
        	console.log(response);
          	if(response.body.length != 0){
        		this.newslist = response.body;
        		this.newslist.reverse();
        		//console.log(this.newslist);
        		this.newslist.forEach((item)=>{
				    	let arr = [];
				    	arr[0] = item.timedesc.toString().substring(0,4);
				    	arr[1] = item.timedesc.toString().substring(4,6);
				    	arr[2] = item.timedesc.toString().substring(6,8);
				    	item.timedesc = arr.join('-');
				    	//console.log(item.timedesc);
				    });
        	} 
        } else {
          Toast("获取招领启示表失败！");
        }
      });     	
  	},
  	foundPublish(){
  		if (this.objdesc.trim().length === 0 || this.realname.trim().length === 0 || this.locationdesc.trim().length === 0 || this.obj.trim().length === 0 || this.telephone.trim().length === 0 || this.timedesc.trim().length === 0) {
	        return Toast("发布内容不能为空！");
	   }
  		if(this.telephone.trim().length > 11 || this.telephone.trim().length < 8 || isNaN(Number(this.telephone.trim())) ){  			
  			return Toast("请输入8-11位联系电话");
  		}
  		let posttime = Number(this.timedesc.replace(/-/g,''));
  		this.latlngy = this.$store.state.tmpLatlng.lng;
  	  this.latlngx = this.$store.state.tmpLatlng.lat;
  		var foundedObj = {
  			"obj": this.obj,
  			"objdesc": this.objdesc.trim(),
  			"realname": this.realname,
  			"locationdesc": this.locationdesc,
  			"telephone": this.telephone,
  			"timedesc": posttime,
  			"latlngy": this.latlngy,
  			"latlngx": this.latlngx
  		};
  		//console.log(foundedObj);
  		let _this = this;
  		this.newslist.some(function(item){
  			var newtime = Number(item.timedesc.replace( /-/g,''));
  			if(item.obj == foundedObj.obj && item.objdesc == foundedObj.objdesc && item.realname == foundedObj.realname && foundedObj.locationdesc == item.locationdesc && foundedObj.telephone == item.telephone && 
  				posttime == newtime && foundedObj.latlngx == item.latlngx && foundedObj.latlngy == item.latlngy){
  					_this.existFlag = 1;
  					Toast("该条信息已存在");
  					return true;
  				}else{
  					_this.existFlag = 0;
  				}
  		});	
  		if(this.existFlag == 0){
  			this.$http.post("found/publish", JSON.stringify(foundedObj),{
	          headers: {
	            contentType: "application/json"
	          }})
	  		.then(response => {
	  			console.log(response);
		        if (response.body.status === "success") {
		        		Toast("发布成功！");
		        		alert("本条信息的uuid为：" + response.body.uuid);
		          	this.getNewsList();
		          	this.obj = "";
		          	this.objdesc = "";
		          	this.realname = "";
		          	this.locationdesc = "";
		          	this.telephone = "";
		          	this.timedesc = "";
		          	this.latlngy = 0;
		          	this.latlngx = 0;
		        } else {
		          Toast("发布招领启示表失败！");
		          $(".form-control").val('');
		        }
	        }) 
	       this.$store.commit("updateTmpLatlng", {lng: 119.204124, lat: 26.064756});
	       //console.log(this.$store.state.tmpLatlng);
  		}	
    },
    searchGo(){
    	var match = $("#searchSelect").val();
    	switch (match){
    		case "按拾取地点查询":
    			var search_text = $("#search-text").val();
    			if(search_text){
    				var obj = {key: "locationdesc", value: search_text};
	    			console.log(obj);
	    			this.$http.post("found/search", JSON.stringify(obj),{
		          headers: {
		            contentType: "application/json"
		          }})
			  		.then(response => {
				        if (response) {
				        	console.log(response);
				          	this.newslist = response.body;
				          	this.newslist.reverse();
				        } else {
				          Toast("查找失败");
				        }
			        });
    			}else{
    				Toast("查找内容不得为空");
    			}
    			break;
    		case "按时间范围查询":
    			var search_text = $("#search-text").val();
    			if(search_text){
    				var timearr = search_text.split(' ');
	    			var obj = {timefrom: new Date(timearr[0]).getTime(), timeto: new Date(timearr[1]).getTime()};
	    			console.log(obj);
	    			this.$http.post("found/search/timerange", JSON.stringify(obj),{
		          headers: {
		            contentType: "application/json"
		          }})
			  		.then(response => {
				        if (response) {
				        	console.log(response);
				          	this.newslist = response.body;
				          	this.newslist.reverse();
				        } else {
				          Toast("查找失败");
				        }
			        });
    			}else{
    				 Toast("查找内容不得为空");
    			}
    			
    			break;
    		case "按物品名称查询":
    			var search_text = $("#search-text").val();
    			if(search_text){
    				var obj = {key: "obj", value: search_text};
	    			console.log(obj);
	    			this.$http.post("found/search", JSON.stringify(obj),{
		          headers: {
		            contentType: "application/json"
		          }})
			  		.then(response => {
				        if (response) {
				        	console.log(response);
				          	this.newslist = response.body;
				          	this.newslist.reverse();
				        } else {
				          Toast("查找失败");
				        }
			        });
    			}else{
    				Toast("查找内容不得为空");
    			}
    			break;
    	}
    	$("#search-text").val('');
    },
    showAllMsg(){
    	this.getNewsList();
    },
    goView(index){
    	let tmpObj = this.newslist[index];
    	this.$store.commit("updateTmpObj", tmpObj);
    	this.$router.push("/found/" + index); 
    	//console.log(this.$store.state.tmpObj.timestamp)
    },
    showcard(){
    	$(".imageForm").show();
    },
    hideImageForm(){
    	$(".imageForm").hide();
    },
    pubcard(){
    	this.$refs.upload.submit();
    },
    onSuccess(response){
			 console.log(response);
			 if (response.status === "success") {
					this.objdesc = "姓名: " + response.data.stuname + " 学号： " + response.data.stuid + " 学院： " + response.data.stucollege;  
				} else {
					Toast("上传失败");
				}
		},
   	handleRemove(file, fileList) {
       console.log(file, fileList);
     },
     handlePictureCardPreview(file) {
       this.dialogImageUrl = file.url;
       this.dialogVisible = true;
     }
  }
}
</script>

<style lang="scss">
#FoundCompo{
	text-decoration: none;
	.mui-table-view{
	  li{
	  	padding-left: 20px;
	  	padding-right: 20px;
	    h1{
	      font-size: 16px;
	      font-weight: bold;
	      display: inline-block;
	      margin-right: 20px;
	    }
	    span{
	    	font-size: 14px;
	    }
	    .mui-ellipsis{
	      font-size: 12px;
	      color: #226aff;
	      display: flex;
	      justify-content: space-between;
	    }
	  }
	}
	.mui-table-view:hover{
		text-decoration: none;
	}
	.cmt-container {
		position: relative;
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
	  .post-btn{
	  	width: 15%;
	  	display: inline-block;
	  }
	  .card-btn{
	  	margin-left: 20px;
	  	width: 15%;
	  	display: inline-block;
	  	font-size: 13px;
	  	background: #CCCCCC;
	  }
	  .imageForm{
	  	display: none;
	  	position: absolute;
	  	width: 40%;
	  	left: 30%;
	  	top: 10%;
	  	padding: 10px 20px;
	  	border: 1px solid rgba(51,122,183,0.5);
	  	background: white;
	  	z-index: 999;
	  	
	  	input[type="file"]{
				display: none !important;
			}
	  	div{
	  		.el-upload--picture-card{
					.el-upload__input{
						display: none !important;
					}
				}
	  	}
	  	.btn{
	  		display: inline-block;
	  		width: 15%;
	  		height: 35px;
	  		margin: 20px 15px 10px 0;
	  		text-align: center;
	  	}
	  }
	  .el-date-editor{
			.el-input__prefix, .el-input__suffix{
				top: -7px !important;
			}
		}
	}
	.input-group{
		margin: 8px 0;
	}
	.map-input{
		.btn{
			width: 11%;
			display: inline-block;
			height: 30px;
			line-height: 30px;
			padding-top: 0;
			font-size: 12px;
			margin-top: -8px;
			font-family: "microsoft yahei";
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
	#searchBar{
		padding: 0 10px 0 20px;
		div{
			display: inline-block;
			width: 15%;
			select{
				width: 100%;
				box-sizing: border-box;
				padding-left: 45px;
				background: #CCCCCC;
				
				option{
					background: white;
				}
			}
			select:hover{
				cursor: pointer;
			}
		}
		input{
			margin-top: 10px;
			display: inline-block;
			width: 70%;
		}
		.btn{
			display: inline-block;
			margin-left: 15px;
		}
	}
}
</style>
