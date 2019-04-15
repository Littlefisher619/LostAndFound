<template>
  <div id="homeContainer">
  <!--头部-->
		<header id="found_header">
			<div id="header-top">
				<div class="container">
					<div class="row">
						<img src="../../img/logo.png" />
					</div>
				</div>
			</div>	
			<div id="header-bottom">
				<el-row>
				  <el-col :span="6" :offset="9">
				  	<div id="header-btns">
				  		<router-link class="btn btn-primary" to="/found" tag="button">发布招领
							  <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							</router-link>
							<router-link class="btn btn-primary" to="/lost" tag="button">发布寻物
							  <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							</router-link>						
						</div>		
				  </el-col>
				</el-row>
						
			</div>
		</header>
		<!--头部-->
		
		<!--轮播图-->
		 <div class="block" id="carousel">
		    <el-carousel>
		      <el-carousel-item v-for="item in lunbotuList" :key="item.id">
		        <img :src="item.url" />
		      </el-carousel-item>
		    </el-carousel>
		  </div>
		<!--/轮播图-->
  </div>
</template>

<script>

export default {
  data() {
    return {
    	lunbotuList: [
    		{url: "http://www.diunal.com/templates/diunalv2/images/lostfound.png",id: 1},
    		{url: "http://wx1.sinaimg.cn/mw690/70ae3966ly1fwvxsalxzoj20hs0ahgn5.jpg",id: 2},
    		{url: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1555265829920&di=0b35e5ce30817c02eea85f334308aa0b&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201412%2F18%2F20141218192508_h4ctV.jpeg", id: 3}
    	]
    };
  },
  created() {
  },
  methods: {
   
  }

};
</script>

<style lang="scss" scoped>
#found_header{
	width: 100%;
	background: #E2E2E2;
	height: 210px;
}
#header-top{
	width: 100%;
	height: 125px;
}
#header-bottom{
	width: 100%;
	height: 85px;
	position: relative;
}
#header-top .container{
	height: 100%;
}
#header-top .row{
	height: 100%;
}
#header-top img{
	position: relative;
    top: 50%;
    height: 54px;
    display: block;
    margin: 0 auto;
}
#header-btns{
	display: flex;
	justify-content: space-between;
	
	margin-top: 25px;
}
.el-carousel{
	margin: 30px auto;
	width: 800px;
	
	.el-carousel__item{
		width: 100%;
		height: 300px;
		
		img{
			background-size:cover;
			width: 100%;
			height: 100%;
		}
	}
}

</style>
