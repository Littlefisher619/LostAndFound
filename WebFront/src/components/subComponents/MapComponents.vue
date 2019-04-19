<template>
  <div id="mapShow">
  	<div id="nav-bar">
		  <div class="input-group">
			  <span class="input-group-addon" id="basic-addon1">当前经纬度</span>
			  <input type="text" class="form-control" id="LatlngMsg">
			</div>
			<button type="button" class="btn btn-default" @click="goBack">确定坐标</button>
		</div>
   	<div class="mybaidumap" id="allmap"></div>
  </div>
</template>

<script>
	import $ from 'jquery'
	import BMap from 'BMap'
    import BMapSymbolSHAPEPOINT from 'BMap_Symbol_SHAPE_POINT'
 
	export default {
	  data() {
	    return {
	    	initPoint: {},
	    	viewMap: 0,
	    };
	  },
	   mounted () {
		this.baiduMap();
	   },
	  methods: {
	  	goBack(){
	  		var text = $("#LatlngMsg").val();
	  		var latlng = text.split(' , ');
	  		this.$store.commit("updateTmpLatlng", {lng: Number(latlng[0]), lat: Number(latlng[1])});
	  		//console.log(this.$store.state.tmpLatlng);
	  		this.$router.go(-1);
	  	},
	    baiduMap () {
		    var map = new BMap.Map('allmap')
		    if(this.$store.state.singleMsgLatlng.lat != -1 && this.$store.state.singleMsgLatlng.lng != -1){
		    	this.viewMap = 1;
		    	var point = new BMap.Point(this.$store.state.singleMsgLatlng.lng,this.$store.state.singleMsgLatlng.lat);
		    	//console.log(point);
		    	$("button").attr("disabled", true);
		    	$("#LatlngMsg").val(this.$store.state.singleMsgLatlng.lng +" , "+ this.$store.state.singleMsgLatlng.lat);
		    	this.$store.commit("updateSingleMsgLatlng", {lng: -1, lat: -1});
		    }else{
		    	this.viewMap = 0;
		    	var point = new BMap.Point(119.204124,26.064756);
		    	 $("#LatlngMsg").val("119.204124 , 26.064756");
		    	console.log(point);
		    }		    
		    map.centerAndZoom(point, 16);
		    map.addControl(new BMap.MapTypeControl({
		    	mapTypes: [
		    		BMAP_NORMAL_MAP,
		    		BMAP_HYBRID_MAP
		    	]
		    }));
		    map.setCurrentCity("福州");
		    map.enableScrollWheelZoom(true);
		    var marker =new BMap.Marker(point)// 创建标注   
		    var marker2;
		    map.addOverlay(marker)// 将标注添加到地图中
		    marker.setAnimation(BMAP_ANIMATION_BOUNCE);  //设置marker跳动的动画 
		    
		    // 覆盖百度地图API的地图点击事件
		    var _this = this;
		    map.addEventListener("click", function(e){
		    	if(_this.viewMap === 1){
		    		if(marker2){
			    		map.removeOverlay(marker2);
			    	}			    
				    marker2 =new BMap.Marker(e.point)// 创建标注  
				    map.addOverlay(marker2)// 将标注添加到地图中
				    marker2.setAnimation(BMAP_ANIMATION_BOUNCE);  //设置marker跳动的动画 
				    $("#LatlngMsg").val(e.point.lng +" , "+ e.point.lat);
		    	}else{
			    	map.clearOverlays(marker);
				    marker =new BMap.Marker(e.point)// 创建标注   
				   // console.log( e.point); 
				    map.addOverlay(marker)// 将标注添加到地图中
				    marker.setAnimation(BMAP_ANIMATION_BOUNCE);  //设置marker跳动的动画 
				    $("#LatlngMsg").val(e.point.lng +" , "+ e.point.lat);
		    	}
			 });  
		 }
	  }
	};
	
</script>

<style lang="scss" scoped>
#mapShow{
	#allmap{
		height: 800px;
	}
	#nav-bar{
		display: flex;
		justify-content: space-between;
		margin: 20px 10px;
		width: 50%;
		.input-group{
			width: 85%;
		}
	}
}
</style>
