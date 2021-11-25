window.$dlg={
  pickColorDialog(config,callback=null){
    let defaultConfig={
      title:'颜色选择器',
      width:'480',
      height:'480',
      attachId:null,
      ctrlHeight:20,
    };


    defaultConfig.width=parseInt(defaultConfig.width);
    defaultConfig.height=parseInt(defaultConfig.height);
    defaultConfig.ctrlHeight=parseInt(defaultConfig.ctrlHeight);

    let pickColor={
      hex:'#ff0000',
      red:255,
      green:0,
      blue:0,
      alpha:255,
    };

    let containDom=document.createElement('div');
    document.body.appendChild(containDom);

    containDom.style.position='absolute';
    containDom.style.left=(window.innerWidth/2)+'px';
    containDom.style.top=(window.innerHeight/2)+'px';
    containDom.style.width=defaultConfig.width+'px';
    containDom.style.height=(defaultConfig.height+defaultConfig.ctrlHeight)+'px';
    containDom.style.transform='translate(-50%,-50%)';
    containDom.style.zIndex=2000;
    containDom.style.display='block';
    containDom.style.border='solid 1px #eee';
    containDom.style.padding='5px';
    containDom.style.borderRadius='5px';
    if(defaultConfig.attachId && defaultConfig.attachId!=''){
      let attachDom=document.getElementById(attachId);
      containDom.style.left=attachDom.offsetLeft+(attachDom.clientWidth/2);
      containDom.style.top=attachDom.offsetTop+(attachDom.clientHeight/2);
    }

    let topCtrlDom=document.createElement('div');
    topCtrlDom.style.width='100%';
    topCtrlDom.style.height=defaultConfig.ctrlHeight+'px';
    topCtrlDom.style.borderBottom='solid 1px #eee';
    containDom.appendChild(topCtrlDom);

    let titleDom=document.createElement('span');
    topCtrlDom.appendChild(titleDom);
    titleDom.innerText=defaultConfig.title;
    titleDom.style.height='100%';
    titleDom.style.float='left';
    titleDom.style.marginRight='8px';
    titleDom.style.fontSize=(defaultConfig.ctrlHeight*0.8)+'px';

    let ctrlDom=document.createElement('span');
    topCtrlDom.appendChild(ctrlDom);
    ctrlDom.style.textAlign='center';
    ctrlDom.innerText='X';
    ctrlDom.style.height='100%';
    ctrlDom.style.float='right';
    ctrlDom.addEventListener('click',function (event){
      containDom.style.display='none';
      containDom.parentNode.removeChild(containDom);
      if(callback){
        callback(pickColor);
      }
    });

    let blueCanvasDom=document.createElement('canvas');
    containDom.appendChild(blueCanvasDom);
    blueCanvasDom.width=defaultConfig.width;
    blueCanvasDom.height=defaultConfig.ctrlHeight/2;

    blueCanvasDom.addEventListener('click',function(event){
      let posX=(event.offsetX/blueCanvasDom.width)*255;
      posX=Math.round(posX);
      pickColor.blue=posX;
      rerenderArea();
    });

    let dc2dBlue=blueCanvasDom.getContext('2d');
    let blueImgData=dc2dBlue.createImageData(blueCanvasDom.width,blueCanvasDom.height);
    for(let i=0;i<blueCanvasDom.height;i++){
      let ri=(i/blueCanvasDom.height)*255;
      ri=Math.round(ri);
      for(let j=0;j<blueCanvasDom.width;j++){
        let rj=(j/blueCanvasDom.width)*255;
        rj=Math.round(rj);
        let pidx=(i*blueCanvasDom.width+j)*4; //rgba

        let r=blueImgData.data[pidx+0];
        let g=blueImgData.data[pidx+1];
        let b=blueImgData.data[pidx+2];
        let a=blueImgData.data[pidx+3];

        blueImgData.data[pidx+0]=0;
        blueImgData.data[pidx+1]=0;
        blueImgData.data[pidx+2]=rj;
        blueImgData.data[pidx+3]=255;

      }
    }

    dc2dBlue.putImageData(blueImgData,0,0);

    let alphaCanvasDom=document.createElement('canvas');
    containDom.appendChild(alphaCanvasDom);
    alphaCanvasDom.width=defaultConfig.width;
    alphaCanvasDom.height=defaultConfig.ctrlHeight/2;

    alphaCanvasDom.addEventListener('click',function(event){
      let posX=(event.offsetX/alphaCanvasDom.width)*255;
      posX=Math.round(posX);
      pickColor.alpha=posX;
      rerenderArea();
    });

    let dc2dAlpha=alphaCanvasDom.getContext('2d');
    let alphaImgData=dc2dAlpha.createImageData(alphaCanvasDom.width,alphaCanvasDom.height);
    for(let i=0;i<alphaCanvasDom.height;i++){
      let ri=(i/alphaCanvasDom.height)*255;
      ri=Math.round(ri);
      for(let j=0;j<alphaCanvasDom.width;j++){
        let rj=(j/alphaCanvasDom.width)*255;
        rj=Math.round(rj);
        let pidx=(i*alphaCanvasDom.width+j)*4; //rgba

        let r=alphaImgData.data[pidx+0];
        let g=alphaImgData.data[pidx+1];
        let b=alphaImgData.data[pidx+2];
        let a=alphaImgData.data[pidx+3];

        alphaImgData.data[pidx+0]=rj;
        alphaImgData.data[pidx+1]=rj;
        alphaImgData.data[pidx+2]=rj;
        alphaImgData.data[pidx+3]=255;

      }
    }

    dc2dAlpha.putImageData(alphaImgData,0,0);

    let canvasDom=document.createElement('canvas');
    containDom.appendChild(canvasDom);
    canvasDom.width=defaultConfig.width;
    canvasDom.height=defaultConfig.height;
    canvasDom.style.border='solid 1px #eee';

    if(!canvasDom.getContext){
      console.error('not support!');
      return;
    }

    let dc2d=canvasDom.getContext('2d');
    /*
    //原始图像
    let imgData=dc2d.createImageData(256,256);
    for(let i=0;i<256;i++){
      for(let j=0;j<256;j++){
        let pidx=(i*256+j)*4; //rgba

        let r=imgData.data[pidx+0];
        let g=imgData.data[pidx+1];
        let b=imgData.data[pidx+2];
        let a=imgData.data[pidx+3];

        imgData.data[pidx+0]=i;
        imgData.data[pidx+1]=j;
        imgData.data[pidx+2]=0;
        imgData.data[pidx+3]=180;

      }
    }
    */

    let rerenderArea=function(){
      //支持缩放
      let imgData=dc2d.createImageData(defaultConfig.width,defaultConfig.height);
      for(let i=0;i<defaultConfig.height;i++){
        let ri=(i/defaultConfig.height)*255;
        ri=Math.round(ri);
        for(let j=0;j<defaultConfig.width;j++){
          let rj=(j/defaultConfig.width)*255;
          rj=Math.round(rj);
          let pidx=(i*defaultConfig.width+j)*4; //rgba

          let r=imgData.data[pidx+0];
          let g=imgData.data[pidx+1];
          let b=imgData.data[pidx+2];
          let a=imgData.data[pidx+3];

          imgData.data[pidx+0]=ri;
          imgData.data[pidx+1]=rj;
          imgData.data[pidx+2]=pickColor.blue;
          imgData.data[pidx+3]=pickColor.alpha;

        }
      }

      dc2d.putImageData(imgData,0,0);
    }

    rerenderArea();

    let eventHandler=function(type,event){
      let posX=(event.offsetX/defaultConfig.width)*255;
      let posY=(event.offsetY/defaultConfig.height)*255;
      posX=Math.round(posX);
      posY=Math.round(posY);
      pickColor.red=posY;
      pickColor.green=posX;
      if(type=='mousedown'){

      }
      if(type=='mouseup'){
          if(callback){
            callback(pickColor);
          }
      }
      if(type=='mousemove'){

      }
    }

    canvasDom.addEventListener('mousemove',function (event){
      eventHandler('mousemove',event);
    });
    canvasDom.addEventListener('mouseup',function(event){
      eventHandler('mouseup',event);
    });
    canvasDom.addEventListener('mousedown',function (event){
      eventHandler('mousedown',event);
    });
  }
}
