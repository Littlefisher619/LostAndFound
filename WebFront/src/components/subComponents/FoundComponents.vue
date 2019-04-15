<template>
  <div id="FoundCompo">
  	<div id="searchBar">
	    <div>
	        <select id="searchSelect">
	        	<option>按拾取地点查询</option>
	        	<option>按物品名称查询</option>
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
              <span>发布时间：{{ item.timedesc }}</span>
              <span>拾取地点：{{ item.timedesc }}</span>
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
		  <router-link type="button" class="btn btn-default" to="/map" tag="button">查看地图（选择坐标）</router-link>
		</div>
		<div class="input-group input-group-sm">
		  <span class="input-group-addon glyphicon glyphicon-calendar" id="sizing-addon5"></span>
		  <input type="text" class="form-control" placeholder="拾到时间" aria-describedby="sizing-addon5" v-model="timedesc">
		</div>
	    <textarea placeholder="请输入拾到物品的详细情况" maxlength="200" v-model="objdesc"></textarea>
	    <mt-button type="primary" size="large" @click="foundPublish" class="post-btn">发表招领启示</mt-button>
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
      latlngx: 0
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
  		let posttime = new Date(this.timedesc).getTime();
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
  		this.$http.post("found/publish", JSON.stringify(foundedObj))
  		.then(response => {
  			console.log(response);
	        if (response.body.status === "success") {
	          	this.getNewsList();
	        } else {
	          Toast("发布招领启示表失败！");
	        }
        })
       this.$store.commit("updateTmpLatlng", {lng: 119.204124, lat: 26.064756});
       //console.log(this.$store.state.tmpLatlng);
    },
    searchGo(){
    	var match = $("#searchSelect").val();
    	switch (match){
    		case "按拾取地点查询":
    			var search_text = $("#search-text").val();
    			if(search_text){
    				var obj = {key: "locationdesc", value: search_text};
	    			console.log(obj);
	    			this.$http.post("found/search", JSON.stringify(obj) )
			  		.then(response => {
				        if (response) {
				        	console.log(response);
				          	this.newslist = response.body;
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
	    			this.$http.post("found/search/timerange", JSON.stringify(obj))
			  		.then(response => {
				        if (response) {
				        	console.log(response);
				          	this.newslist = response.body;
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
	    			this.$http.post("found/search", JSON.stringify(obj) )
			  		.then(response => {
				        if (response) {
				        	console.log(response);
				          	this.newslist = response.body;
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
    }
  }
}
</script>

<style lang="scss" scoped>
#FoundCompo{
	text-decoration: none;
	.mui-table-view{
	  li{
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
	  }
	}
	.input-group{
		margin: 8px 0;
	}
	.map-input{
		.btn{
			width: 13%;
			display: inline-block;
			height: 30px;
			line-height: 30px;
			padding-top: 0;
		}
		input{
			width: 87%;
			display: inline-block;
		}
	}
	#searchBar{
		padding: 0 10px;
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
