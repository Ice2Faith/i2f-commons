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
    }

}
