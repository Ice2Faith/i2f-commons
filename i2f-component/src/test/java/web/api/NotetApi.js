let reqForm={
	id:''
	,title:''
	,viceTitle:''
	,headImg:''
	,attachFile:''
	,content:''
	,createDate:''
	,modifyDate:''
	,updateCount:''
	,viewCount:''
};

this.$axios({
	url:'Notet/queryList',
	method:'post',
	data:reqForm
}).then(resp=>{
	let list=resp;
	console.log('list',list);
})

this.$axios({
	url:'Notet/insert',
	method:'post',
	data:[reqForm]
}).then(resp=>{
	let success=resp;
	console.log('success',list);
})

this.$axios({
	url:'Notet/update',
	method:'post',
	data:reqForm
}).then(resp=>{
	let success=resp;
	console.log('success',list);
})

this.$axios({
	url:'Notet/delete',
	method:'post',
	data:reqForm
}).then(resp=>{
	let success=resp;
	console.log('success',list);
})
