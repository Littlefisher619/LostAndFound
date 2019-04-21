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
			<div id="header-bottom" class="container">
				<div class="row" id="btns-wrap">
  				<div class="col-xs-6 col-xs-offset-3" id="header-btns">
  					<router-link class="btn btn-primary" to="/found" tag="button">发布招领
							 <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						</router-link>
						<router-link class="btn btn-primary" to="/lost" tag="button">发布寻物
							<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
						</router-link>	
  				</div>
				</div>				
			</div>
		</header>
		<!--头部-->
		
		 <!-- 轮播图区域 -->
    <swiper :options="swiperOption">
     <!-- slides -->
	    <swiper-slide><img src="../../img/lunbo3.jpg" alt=""></swiper-slide>
	    <swiper-slide><img src="../../img/lunbo2.jpg" alt=""></swiper-slide>
	    <swiper-slide><img src="../../img/lunbo4.jpg" alt=""></swiper-slide>
	    <!-- Optional controls -->
	    <div class="swiper-pagination"  slot="pagination"></div>
	    <div class="swiper-button-prev" slot="button-prev"></div>
	    <div class="swiper-button-next" slot="button-next"></div>
	    <div class="swiper-scrollbar"   slot="scrollbar"></div>
	  </swiper>
  
  </div>
</template>

<script>

export default {
  data() {
    return {
 			swiperOption: {
 				pagination: {
          el: '.swiper-pagination',
          clickable :true,
        },
        autoplay: {
			    delay: 2000,
		    },
		    navigation: {
					nextEl: '.swiper-button-next',
				   prevEl: '.swiper-button-prev',
				},
				effect : 'cube',
				speed: 1300,
				loop: true, 
      }
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
	height: 85px;
	position: relative;
	
	#btns-wrap{
		#header-btns{
			display: flex;
			justify-content: space-around;
			margin-top: 25px;
		}
	}
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
.swiper-container {
	width: 90%;
  height: 300px;
  margin: 30px auto 0;

  .swiper-slide{
  	background-size: cover;
  	
    img {
      width: 100%;
      height: 100%;
    }
  }
}

</style>
