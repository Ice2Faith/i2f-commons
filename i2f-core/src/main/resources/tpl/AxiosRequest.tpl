/**
 * @author ${ctx.author}
 * @date ${now@DateUtil.format}
 * @desc ${ctx.meta.tableName@GenerateContext.castTableName}Bean
 *       for table : ${ctx.meta.tableName}
 */

let reqForm={
#{[for,ctx.meta.columns],separator="
    ,",template="${_item.colName@GenerateContext.castColumnName}:''"}
}

// queryList
this.$axios({
    url:process.env.BASE_URL+'/${ctx.meta.tableName@GenerateContext.castTableName}/queryList',
    method:'post',
    data:reqForm
}).then(resp=>{
    let list=resp;
    console.log('list:',list);

}).catch(err=>{});

// insert
this.$axios({
    url:process.env.BASE_URL+'/${ctx.meta.tableName@GenerateContext.castTableName}/insert',
    method:'post',
    data:[reqForm]
}).then(resp=>{
    if(resp==true || resp=='true'){
        console.log('insert success');
    }else{
        console.log('insert failure');
    }
}).catch(err=>{});

// update
this.$axios({
    url:process.env.BASE_URL+'/${ctx.meta.tableName@GenerateContext.castTableName}/update',
    method:'post',
    data:reqForm
}).then(resp=>{
    if(resp==true || resp=='true'){
        console.log('update success');
    }else{
        console.log('update failure');
    }
}).catch(err=>{});

// delete
this.$axios({
    url:process.env.BASE_URL+'/${ctx.meta.tableName@GenerateContext.castTableName}/delete',
    method:'post',
    data:reqForm
}).then(resp=>{
    if(resp==true || resp=='true'){
        console.log('delete success');
    }else{
        console.log('delete failure');
    }
}).catch(err=>{});
