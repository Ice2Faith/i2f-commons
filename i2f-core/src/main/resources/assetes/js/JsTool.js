window.$jtl={
    // 重新加载JS文件
    // 示例：
    // reloadJs('jsDataId','/static/data.js');
    reloadJs(id,src){
        document.getElementById(id).remove();
        var scriptObj = document.createElement("script");
        scriptObj.src = src;
        scriptObj.type = "text/javascript";
        scriptObj.id = id;
        document.getElementsByTagName("head")[0].appendChild(scriptObj);
    },
    // 强制缩放屏幕至全屏
    // 示例：
    // let ratio=scalePage('body',1920,768);
    scalePage(elemSelector,originWidth,originHeight){
        let innerWidth = window.innerWidth;
        let ratioX = innerWidth / originWidth;
        let innerHeight=window.innerHeight;
        let ratioY=innerHeight/originHeight;
        let elem=document.querySelector(elemSelector);
        elem.style.transform = 'scale(' + ratioX+','+ratioY + ')';
        elem.style.transformOrigin = '0 0';
        elem.style.overflow='auto';
        return {ratioX:ratioX,ratioY:ratioY};
    },
    // 强制缩放元素到恒居中固定
    // 示例：scaleFixedCenterElem('.el-dialog',0.5,0.5);
    // 一般结合全屏缩放参数使用：
    // let ratio=scalePage('body',1920,768);
    // scaleFixedCenterElem('.el-dialog',ratio.ratioX,ratio.ratioY);
    scaleFixedCenterElem(elemSelector,ratioX,ratioY){
        let elems=document.querySelectorAll(elemSelector);
        elems.forEach(item=>{
            item.style.transform = 'scale(' + ratioX+','+ratioY + ') translateX(-50%)';
            item.style.transformOrigin = '0 0';
            item.style.left='50%';
            item.style.position='fixed';
        })
    },
    // 注册当屏幕大小改变时强制缩放，实现自适应分辨率缩放
    // 示例：
    // registerScaleFullPage('body',1920,768);
    registerScaleFullPage(elemSelector,originWidth,originHeight){
        this.scalePage(elemSelector,originWidth,originHeight);
        window.onresize=function (){
            this.scalePage(elemSelector,originWidth,originHeight);
        }
    },
    // 注册元素改变监听器，可以在此实现回调
    // 示例：
    // registerOnElementChange(func,'body',true,true,true);
    registerOnElementChange(callbackFunc,elemSelector,needListenAttr,needListenChild,needListenSubTree){

        if(elemSelector==undefined){
            elemSelector='body';
        }
        if(needListenAttr==undefined){
            needListenAttr=true;
        }
        if(needListenChild==undefined){
            needListenChild=true;
        }
        if(needListenSubTree==undefined){
            needListenSubTree=true;
        }

        // 选择需要观察变动的节点
        const targetNode = document.querySelector(elemSelector);

        // 观察器的配置（需要观察什么变动）
        const config = { attributes: needListenAttr, childList: needListenChild, subtree: needListenSubTree };

        // 当观察到变动时执行的回调函数
        const callback = function(mutationsList, observer) {
            // Use traditional 'for loops' for IE 11
            // for(let mutation of mutationsList) {
            //   if (mutation.type === 'childList') {
            //     console.log('A child node has been added or removed.');
            //   }
            //   else if (mutation.type === 'attributes') {
            //     console.log('The ' + mutation.attributeName + ' attribute was modified.');
            //   }
            // }
            callbackFunc(mutationsList,observer);
        };

        // 创建一个观察器实例并传入回调函数
        const observer = new MutationObserver(callback);

        // 以上述配置开始观察目标节点
        observer.observe(targetNode, config);
    },
    parseDate(date){
        let ret=new Date();
        let year=ret.getFullYear();
        let month=ret.getMonth();
        let day=ret.getDate();
        let hour=ret.getHours();
        let min=ret.getMinutes();
        let sec=ret.getSeconds();
        let msec=ret.getMilliseconds();
        if(/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2} \d{3}$/.test(date)){
            year=date.substring(0,4);
            month=date.substring(5,7)-1;
            day=date.substring(8,10);
            hour=date.substring(11,13);
            min=date.substring(14,16);
            sec=date.substring(17,19);
            msec=date.substring(20,23);
        }else if(/^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/.test(date)){
            year=date.substring(0,4);
            month=date.substring(5,7)-1;
            day=date.substring(8,10);
            hour=date.substring(11,13);
            min=date.substring(14,16);
            sec=date.substring(17,19);
            msec=0;
        }else if(/^\d{4}-\d{2}-\d{2}$/.test(date)){
            year=date.substring(0,4);
            month=date.substring(5,7)-1;
            day=date.substring(8,10);
            hour=0;
            min=0;
            sec=0;
            msec=0;
        }else if(/^\d{4}-\d{2}$/.test(date)){
            year=date.substring(0,4);
            month=date.substring(5,7)-1;
            day=1;
            hour=0;
            min=0;
            sec=0;
            msec=0;
        }else if(/^\d{4}$/.test(date)){
            year=date.substring(0,4);
            month=1-1;
            day=1;
            hour=0;
            min=0;
            sec=0;
            msec=0;
        }else if(/^\d{4}\d{2}\d{2}$/.test(date)){
            year=date.substring(0,4);
            month=date.substring(4,6)-1;
            day=date.substring(6,8);
            hour=0;
            min=0;
            sec=0;
            msec=0;
        }else if(/^\d{4}\d{2}$/.test(date)){
            year=date.substring(0,4);
            month=date.substring(4,6)-1;
            day=1;
            hour=0;
            min=0;
            sec=0;
            msec=0;
        }
        return new Date(year,month,day,hour,min,sec,msec);
    },
    formatDate(date,patten){
        let year=date.getFullYear();
        let month=date.getMonth()+1;
        let day=date.getDate();
        let hour=date.getHours();
        let min=date.getMinutes();
        let sec=date.getSeconds();
        let msec=date.getMilliseconds();

        let phour=hour%12;
        if(year<10){
            year='000'+year;
        }else if(year<100){
            year='00'+year;
        }else if(year<1000){
            year='0'+year;
        }
        if(month<10){
            month='0'+month;
        }
        if(day<10){
            day='0'+day;
        }
        if(hour<10){
            hour='0'+hour;
        }
        if(min<10){
            min='0'+min;
        }
        if(sec<10){
            sec='0'+sec;
        }
        if(msec<10){
            msec='00'+msec;
        }else if(msec<100){
            msec='0'+msec;
        }
        if(phour<10){
            phour='0'+phour;
        }
        patten=this.replaceAll(patten,'yyyy',year);
        patten=this.replaceAll(patten,'MM',month);
        patten=this.replaceAll(patten,'dd',day);
        patten=this.replaceAll(patten,'HH',hour);
        patten=this.replaceAll(patten,'mm',min);
        patten=this.replaceAll(patten,'ss',sec);
        patten=this.replaceAll(patten,'SSS',msec);
        patten=this.replaceAll(patten,'hh',phour);

        return patten;
    },
    replaceAll(str,reg,rep){
        let ret='';
        let arr=str.split(reg);
        let isFirst=true;
        for(let item in arr){
            if(!isFirst){
                ret+=rep;
            }
            ret+=item;
            isFirst=false;
        }
    },
    notNull(obj){
        return obj!=null && obj!=undefined;
    },
    notBlank(str){
        return this.notBlank(str) && str!='';
    },
    notEmpty(obj){
        let js=JSON.stringify(obj);
        return this.notNull(obj) && js!='' && js!='{}' && js!='[]';
    }

}
