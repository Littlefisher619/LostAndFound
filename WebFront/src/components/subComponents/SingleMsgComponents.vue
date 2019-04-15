<template>
  <div id="singleMsg">
  	<div class="well well-sm">
  		<h3>{{singleMsg.obj}}</h3>
    	<h4>发布时间：{{singleMsg.timestamp}}</h4>
  	</div>
  </div>
</template>

<script>
	export default {
	  data() {
	    return {
	    	singleMsg: {}
	    };
	  },
	  created() {
	  	this.render();
	  },
	  methods: {
	    render(){
	    	this.singleMsg = this.$store.state.tmpObj;
	    }
	  }
 };
</script>

<style lang="scss" scoped>
#singleMsg{
	padding: 0 20px;
	
	.well{
		height: 100px;
		margin-top: 20px;
		padding-top: 0;
		
		h3{
			font-size: 22px;
			font-weight: bold;
			margin-bottom: 20px;
		}
		h4{
			font-size: 14px;
		}
	}
	
}
</style>
