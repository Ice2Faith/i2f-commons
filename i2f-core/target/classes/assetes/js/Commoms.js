window.DataUtil={
    getPathData:function(obj,path){
        if(!obj){
            return obj;
        }
        if(!path || path==''){
            return obj;
        }
        let metaProps = ('' + path).split('.');
        let item = obj;
        for (let i = 0; i < metaProps.length; i++) {
            if(!item){
                return item;
            }
            item = item[metaProps[i]];
        }
        return item;
    }
};
window.Base64Util={
    encodeOnly:function(str){
        return window.btoa(unescape(encodeURIComponent(str)));
    },
    decodeOnly:function(str){
        return decodeURIComponent(escape(window.atob(str)));
    },
    encode4Json:function (obj){
        return this.encodeOnly(JSON.stringify(obj));
    },
    decode2Json:function (str){
        return JSON.parse(this.decodeOnly(str));
    }
};
window.LocalStorageUtil={
    get:function (key){
        return localStorage.getItem(key);
    },
    set:function (key,val){
        return localStorage.setItem(key,val);
    },
    clean:function (key){
        return localStorage.removeItem(key);
    }
};
window.ElementUtil={

};
window.AjaxUtil={

};
window.AxiosUtil={

};


