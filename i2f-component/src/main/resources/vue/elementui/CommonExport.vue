<template>
  <div>

    <el-popover trigger="hover"
                style="margin-left:5px;"
                placement="bottom-start"
                title=""
                effect="dark"
                width="150px">


    <el-button  slot="reference" :size="size" :type="type" v-loading="exports.loading">
      <template v-if="exports.downloadUrl && exports.downloadUrl!=''">
        <i @click="exports.downloadUrl=''" class="el-icon-copy-document el-icon--right"></i>
        <el-link :href="exports.downloadUrl" style="color:white;">
          下载<i class="el-icon-download el-icon--right"></i>
        </el-link>
      </template>
      <template v-else>
        <span @click="requireRequestData">
          {{text}}<i class="el-icon-copy-document el-icon--right"></i>
        </span>
      </template>
    </el-button>

        <div>
          <div v-html="tips"></div>
          一、导出操作<br/>
          1.点击<span style="color:orangered">{{text}}</span>按钮，按钮进入<span style="color: orangered">加载等待</span>状态，按钮带<span style="color: orangered">复制</span><i class="el-icon-copy-document el-icon"></i>图标<br/>
          2.按钮等待导出<span style="color: orangered">结束</span>之后，显示为<span style="color: orangered">下载</span>，点击下载即可下载导出文件，按钮带有<span style="color: orangered">复制</span><i @click="exports.downloadUrl=''" class="el-icon-copy-document el-icon"></i>、<span style="color: orangered">下载</span><i @click="exports.downloadUrl=''" class="el-icon-download el-icon"></i>图标<br/>
          3.点击<span style="color: orangered">复制</span><i class="el-icon-copy-document el-icon"></i>图标<span style="color: orangered">重置</span>为初始状态，可重新进行导出工作<br/>
        </div>

    </el-popover>
  </div>
</template>
<script>

export default {
  name: 'CommonExport',
  props: {
    request:{
      type:Object,
      default:{
        reqData:{},
        requestUrl:'',
      }
    },
    text:{
      type:String,
      default:'导出'
    },
    commit:{
      type:Boolean
    },
    size:{
      type:String,
      default:'normal',
    },
    type:{
      type:String,
      default:'primary',
    },
    tips:{
      type:String,
      default:'',
    }
  },
  data() {
    return {
      exports:{
        respData:{

        },
        timer:'',
        downloadUrl:'',
        loading:false,
      }
    }
  },
  destroyed() {
    clearInterval(this.exports.timer);
  },
  methods: {
    requireRequestData(){
      clearInterval(this.exports.timer);
      this.$emit('require');
    },
    checkExportFileIsOk(){
      let _this=this;
        let checkUrl=this.exports.respData.checkUrl;
        this.exports.timer=setInterval(()=>{

          this.$axios.post($config.baseUrl+checkUrl,JSON.stringify({}), function(data) {
            if (data.code==200) {
              let rs = data.data
              console.log('ckRs:',rs);
              if(rs==true || rs=='true'){
                _this.exports.downloadUrl=$config.baseUrl+_this.exports.respData.downloadUrl;
                _this.exports.loading=false;
                clearInterval(_this.exports.timer);
                _this.$emit('ok',_this.exports.downloadUrl);
              }
            }
          });
        },3*1000);

    },
    exportTableData(){
      this.$emit('update:commit',false);
      this.$emit('committed');
      let reqUrl=this.request.requestUrl;
      let params=this.request.reqData;

      this.exports.downloadUrl='';
      this.exports.loading=true;

      var client = new RESTClient();
      let _this=this;
      client.post($config.baseUrl+reqUrl,JSON.stringify(params), function(data) {
        if (data.code==200) {
          _this.exports.respData = data.resultValue
          _this.checkExportFileIsOk();
          _this.$emit('requested',_this.exports.respData,data);
        }
      });
    },
  },
  watch:{
    commit:{
      immediate:true,
      deep:true,
      handler:function(val,old){
        if(val==true){
          this.exportTableData();
        }
      }
    }
  }
}
</script>

<style scoped>
.qry-tab{
    /* border:  1px solid #cccccc7d; */
    /* margin-bottom: 5px; */
    /* background: #fff; */
    /* border-bottom: 10px solid #e4e7ee; */
    /* border-bottom:3px solid rgb(4, 137, 152) */
    /* padding: 0 15px; */
    height: auto;
    /* transition: height 5s */
}
.qry-title{
    line-height: 30px;
    padding: 10px;
    border-bottom: 1px solid #cccccc7d;
    font-size: 15px;
}
.right-icon{
    /* float:right; */
    line-height: 30px;
    cursor: pointer;
}
.collapse-area{
    /* margin-top: 14px;
    margin-bottom: 14px; */
    padding:0;
    transition: display 3s linear;
}
.right-area{
    text-align: right;
}
.icon-div{
  /* margin-bottom:-6px; */
  /* margin-top:-7px; */
  margin-bottom: -1px;;
  text-align:center;
}
.arrow{
    /* position: absolute; */
    /* bottom: 0; */
    left: calc(50% - 25px);
    font-size: 4px;
    width: 50px;
    cursor: pointer;
    text-align: center;
    color: #909399;
    height: 10px;
    line-height: 11px;
    font-weight: bolder;
    border-top-left-radius: 5px;
    border-top-right-radius: 5px;
    background-color: #e4e7ee;
}

</style>
