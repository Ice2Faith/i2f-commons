/**
 * 标准接口定义
 * @type {{filter(*, *, *): boolean, comparator(*, *): number, executor(*, *, *), mapper(*, *, *): *, executorConsoleLog(*=, *, *): void}}
 */
window.$interface = {
    comparator(elem1, elem2) {
        if (elem1 == elem2) {
            return 0;
        }
        if (elem1 < elem2) {
            return -1;
        }
        return 1;
    },
    executor(item, index, arr) {

    },
    mapper(item, index, arr) {
        return item;
    },
    filter(item, index, arr) {
        return true;
    },
    executorConsoleLog(item, index, arr) {
        console.log('arr[' + index + ']=', item);
    }
}

/**
 * 检查工具
 * @type {{isIntStr(*=): (boolean|boolean), containsStr(*=, *=, *=): (boolean|number), containsElem(*=, *=, *=): boolean, isNull(*): boolean, isEmpty(*=): *, isFloatStr(*=): (boolean|boolean), isUndefined(*=): boolean, isBlank(*=): *, isArray(*=): boolean}}
 */
window.$chk = {
    isNull(obj) {
        return obj == null;
    },
    isUndefined(obj) {
        return obj == undefined;
    },
    isEmpty(obj) {
        return !obj;
    },
    isBlank(obj) {
        return !obj || obj == '';
    },
    isIntStr(str) {
        if (this.isBlank(str)) {
            return false;
        }
        return /^[+|-]\d+$/.test(str);
    },
    isFloatStr(str) {
        if (this.isBlank(str)) {
            return false;
        }
        return /^[+|-]\d+(\.\d+)$/.test(str);
    },
    isArray(arr) {
        if (this.isEmpty(arr)) {
            return false;
        }
        if (arr instanceof Array) {
            return true;
        }
        let js = JSON.stringify(arr);
        if (/^\s*\[.*\]\s*$/.test()) {
            return true;
        }
        return false;
    },
    isTel11(str) {
        if (!this.isBlank(str)) {
            return false;
        }
        return /^1\d{10}$/.test(str);
    },
    isPasswordGe6(str) {
        if (!this.isBlank(str)) {
            return false;
        }
        return /^[a-zA-z0-9_]{6,}$/.test(str);
    },
    isNumPassword6(str) {
        if (!this.isBlank(str)) {
            return false;
        }
        return /^\d{6}$/.test(str);
    },
    isEMail(str) {
        if (!this.isBlank(str)) {
            return false;
        }
        return /^[a-zA-Z0-9_]+@[a-zA-Z0-9_]+(\.[a-zA-Z0-9_]+)*$/.test(str);
    },
    isCnIdNum18(str) {
        if (this.isEmpty(str)) {
            return false;
        }
        str = (str + '').toUpperCase();
        if (!/^\d{17}[0-9|X]$/.test(str)) {
            return false;
        }
        let ds = str.substring(0, 6);
        let year = str.substring(6, 10);
        let mon = str.substring(10, 12);
        let day = str.substring(12, 14);
        let tail = str.substring(14);
        if (!$date.isLegalDate(year, mon, day)) {
            return false;
        }
        return true;
    },
    isSqlSafe(str) {
        this.isBlank(str)
        {
            return true;
        }
        if(str.indexOf("'")){
            return false;
        }
        return true;
    },
    containsStr(str, findstr, ignoreCase = false) {
        if (this.isBlank(str) || this.isBlank(findstr)) {
            return false;
        }
        str = str + '';
        findstr = findstr + '';
        if (ignoreCase) {
            str = str.toLowerCase();
            findstr = findstr.toLowerCase();
        }
        return str.indexOf(findstr);
    },
    containsElem(arr, elem, comparator = $interface.comparator) {
        if (this.isEmpty(arr)) {
            return false;
        }
        if (!this.isArray(arr)) {
            return false;
        }
        for (let i = 0; i < arr.length; i++) {
            if (comparator(elem, arr[i]) == 0) {
                return true;
            }
        }
        return false;
    },

}

/**
 * 转换工具
 * @type {{}}
 */
window.$cvt={
    null2(obj,repVal){
        if($chk.isNull(obj)){
            return repVal;
        }
        return obj;
    },
    undefined2(obj,repVal){
        if($chk.isUndefined(obj)){
            return repVal;
        }
        return obj;
    },
    empty2(obj,repVal){
        if($chk.isEmpty(obj)){
            return repVal;
        }
        return obj;
    },
    blank2(obj,repVal){
        if($chk.isBlank(obj)){
            return repVal;
        }
        return obj;
    }
}

window.$url={
    encode(str){
        if($chk.isBlank(str)){
            return "";
        }
        return encodeURI(str);
    },
    decode(str){
        if($chk.isBlank(str)){
            return "";
        }
        return decodeURI(str);
    },
    stringifyUrlParams(param){
        let _this=this;
        let ret='';
        $obj.each(param,function(val,key,obj){
           ret+='&';
           ret+=key;
           ret+='=';
           ret+=_this.encode(val+'');
        });
        if(ret.length>0){
            ret=ret.substring(1);
        }
        return ret;
    },
    buildUrl(url,param){
        url=url+'';
        let pstr=this.stringifyUrlParams(param);
        if(url.indexOf('?')){
            if(url.endsWith('&')){
                return url+pstr;
            }else{
                return url+'&'+pstr;
            }
        }else{
            return url+'?'+pstr;
        }
    },
    linkUrl(baseUrl,url){
        baseUrl=baseUrl+'';
        if(baseUrl.endsWith('/')){
            return baseUrl+url;
        }else{
            return baseUrl+'/'+url;
        }
    },
    getUrlParamsObj(url){
        url=url+'';
        let ret={};
        let idx=url.indexOf('?');
        if(idx<0){
            return ret;
        }

        let pstr=url.substring(idx+1);
        let parr=pstr.split('&');
        for(let i=0;i<parr.length;i++){
            let item=parr[i];
            let iarr=item.split('=',2);
            if(iarr.length==2){
                ret[iarr[0]]=this.decode(iarr[1]);
            }
        }
        return ret;
    },
    getUrlParam(url,name){
        let obj=this.getUrlParamsObj(url);
        return obj[name];
    },
    getCurrentUrlParamsObj(){
        let url=window.location.href;
        return this.getUrlParamsObj(url);
    },
    getCurrentUrlParam(name){
        let obj=this.getCurrentUrlParamsObj();
        return obj[name];
    },
    /**
     * 解析URL的各部分到对象中
     * @param url
     * @returns {{path: string, protocol: string, password: string, file: string, port: string, host: string, arguments: {}, tag: string, username: string}}
     */
    urlParse(url){
        let ret={
            protocol:'',
            username:'',
            password:'',
            host:'',
            port:'',
            path:'',
            file:'',
            arguments:{},
            tag:''
        };
        // URL标准定义： 协议：//用户名:密码@子域名.域名.顶级域名:端口号/目录/文件名.文件后缀?参数=值#标志
        url=url+'';
        let idx=url.indexOf("://");
        if(idx>=0){
            ret.protocol=url.substring(0,idx)+'://';
            url=url.substring(idx+3);
        }

        let userHost='';
        idx=url.indexOf("/");
        if(idx>=0){
            userHost=url.substring(0,idx);
            url=url.substring(idx+1);
        }

        if(userHost!=''){
            idx=userHost.indexOf('@');
            if(idx>=0){
                let userPassword=userHost.substring(0,idx);
                userHost=userHost.substring(idx+1);
                let userPassArr=userPassword.split(":",2);
                let plen=userPassArr.length;
                if(plen>=1){
                    ret.username=userPassArr[0];
                }
                if(userPassArr.length>=2){
                    ret.password=userPassArr[1];
                }
            }
            let hostPortArr=userHost.split(":",2);
            let plen=hostPortArr.length;
            if(plen>=1){
                ret.host=hostPortArr[0];
            }
            if(plen>=2){
                ret.port=hostPortArr[1];
            }
        }

        let pathFile='';
        let urlParamStr='';
        idx=url.indexOf("?");
        if(idx>=0){
            pathFile=url.substring(0,idx);
            urlParamStr=url.substring(idx+1);
        }

        if(pathFile!=''){
            idx=pathFile.lastIndexOf("/");
            if(idx>=0){
                ret.path='/'+pathFile.substring(0,idx);
                ret.file=pathFile.substring(idx+1);
            }else{
                ret.file=pathFile;
            }
        }

        let tagStr='';
        if(urlParamStr!=''){
            idx=urlParamStr.indexOf("#");
            if(idx>=0){
                tagStr=urlParamStr.substring(idx+1);
                urlParamStr=urlParamStr.substring(0,idx);
            }
        }

        ret.arguments=this.getUrlParamsObj('?'+urlParamStr);
        ret.tag=tagStr;


        return ret;
    },
}

/**
 * 存储工具
 * @type {{localGet(*=): string | null, sessionSetAsJson(*=, *=): *, sessionGetWithJson(*=): any, sessionSet(*=, *=): void, localSetAsJson(*=, *=): *, localClean(*=): void, sessionClean(*=): void, localSet(*=, *=): void, sessionGet(*=): string | null, localGetWithJson(*=): any}}
 */
window.$store = {
    localSet(key, val) {
        return localStorage.setItem(key, val);
    },
    localGet(key) {
        return localStorage.getItem(key);
    },
    localClean(key) {
        return localStorage.removeItem(key);
    },
    localSetAsJson(key, obj) {
        return this.localSet(key, $json.toJson(obj));
    },
    localGetWithJson(key) {
        return $json.parseJson(this.localGet(key));
    },
    sessionSet(key, val) {
        return sessionStorage.setItem(key, val);
    },
    sessionGet(key) {
        return sessionStorage.getItem(key);
    },
    sessionClean(key) {
        return sessionStorage.removeItem(key);
    },
    sessionSetAsJson(key, obj) {
        return this.sessionSet(key, $json.toJson(obj));
    },
    sessionGetWithJson(key) {
        return $json.parseJson(this.sessionGet(key));
    },
}

/**
 * 日期工具
 * @type {{nextMonday(*=): *, formatDate(*=, *=): string, firstDayOfYear(*=): *, convertDateFmt(*=, *=, *=): string, lastDayOfPreviousMonth(*=): *, addSecond(*, *): *, addMonth(*, *): *, toDateObj(*=): {month: number, hour: number, year: number, millisecond: number, day: number, minute: number, second: number}, addDay(*, *): *, isLegalDate(*=, *=, *): boolean, daysOnMonth(*=, *): number, mondayOfDate(*=): *, convertSupportDateFmt(*=, *=): string, sundayOfDate(*=): *, supportAutoParseDatePattens(): ({patten: RegExp, fmt: string})[], parseDateObj(*=): Date, addHour(*, *): *, addMinute(*, *): *, firstDayOfNextMonth(*=): *, firstDayOfMonth(*=): *, isLeapYear(*): boolean, isSupportAutoParse(*=): boolean, lastDayOfMonth(*=): *, previousSunday(*=): *, addYear(*, *): *, daysOnYear(*=): number, parseDate(*, *=): Date, fromStr(*=, *=): (Date), addMillSecond(*, *): *, lastDayOfYear(*=): *}}
 */
window.$date = {
    /**
     * 是否闰年
     * @param year
     * @returns {boolean}
     */
    isLeapYear(year) {
        year = parseInt(year + '');
        if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)) {
            return true;
        }
        return false;
    },
    /**
     * 这个月有多少天
     * @param year
     * @param mon
     * @returns {number}
     */
    daysOnMonth(year, mon) {
        mon = parseInt(mon + '');
        switch (mon) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
                break;
            case 2:
                if (this.isLeapYear(year)) {
                    return 29;
                } else {
                    return 28;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
                break;
            default:
                return 0;
                break;
        }
        return 0;
    },
    /**
     * 这年有多少天
     * @param year
     * @returns {number}
     */
    daysOnYear(year) {
        if (this.isLeapYear(year)) {
            return 366;
        }
        return 365;
    },
    isLegalDate(year, month, day) {
        let mdays = this.daysOnMonth(year, month);
        if (mdays == 0) {
            return false;
        }
        if (day < 1 || day > mdays) {
            return false;
        }
        return true;
    },
    addDay(date, num) {
        date.setDate(date.getDate() + num);
        return date;
    },
    addMonth(date, num) {
        date.setMonth(date.getMonth() + num);
        return date;
    },
    addYear(date, num) {
        date.setFullYear(date.getFullYear() + num);
        return date;
    },
    addHour(date, num) {
        date.setHours(date.getHours() + num);
        return date;
    },
    addMinute(date, num) {
        date.setMinutes(date.getMinutes() + num);
        return date;
    },
    addSecond(date, num) {
        date.setSeconds(date.getSeconds() + num);
        return date;
    },
    addMillSecond(date, num) {
        date.setMilliseconds(date.getMilliseconds() + num);
        return date;
    },
    firstDayOfYear(date) {
        if (!date) {
            date = new Date();
        } else {
            date = new Date(date);
        }
        date.setMonth(0);
        date.setDate(1);
        return date;
    },
    firstDayOfMonth(date) {
        if (!date) {
            date = new Date();
        } else {
            date = new Date(date);
        }
        date.setDate(1);
        return date;
    },
    lastDayOfYear(date) {
        if (!date) {
            date = new Date();
        } else {
            date = new Date(date);
        }
        date.setMonth(0);
        date.setDate(1);
        date.setFullYear(date.getFullYear() + 1);
        date.setMonth(0);
        date.setDate(1);
        date.setDate(date.getDate() - 1);
        return date;
    },
    lastDayOfMonth(date) {
        if (!date) {
            date = new Date();
        } else {
            date = new Date(date);
        }
        date.setDate(1);
        date.setMonth(date.getMonth() + 1);
        date.setDate(1);
        date.setDate(date.getDate() - 1);
        return date;
    },
    lastDayOfPreviousMonth(date) {
        date = this.firstDayOfMonth(date);
        date.setDate(1);
        date.setDate(date.getDate() - 1);
        return date;
    },
    firstDayOfNextMonth(date) {
        date = this.firstDayOfMonth(date);
        date.setMonth(date.getMonth() + 1);
        return date;
    },
    mondayOfDate(date) {
        if (!date) {
            date = new Date(0);
        } else {
            date = new Date(date);
        }
        let day = date.getDate();
        let week = date.getDay();
        date.setDate(day - week + 1);
        return date;
    },
    sundayOfDate(date) {
        if (!date) {
            date = new Date();
        } else {
            date = new Date(date);
        }
        let day = date.getDate();
        let week = date.getDay();
        date.setDate(day + (7 - week));
        return date;
    },
    nextMonday(date) {
        date = this.mondayOfDate(date);
        date.setDate(date.getDate() + 7);
        return date;
    },
    previousSunday(date) {
        date = this.sundayOfDate(date);
        date.setDate(date.getDate() - 7);
        return date;
    },
    /**
     * 将日期date按照patten进行格式化为字符串
     * 支持的格式段：yyyy,MM,dd,HH,mm,ss,SSS,hh,y,M,d,H,m,s,S,h
     * @param date
     * @param patten
     * @returns {string}
     */
    formatDate(date, patten) {
        if (!date) {
            date = new Date();
        } else {
            date = new Date(date);
        }
        if (!patten || patten == '') {
            patten = 'yyyy-MM-dd HH:mm:ss SSS';
        }
        let year = date.getFullYear();
        let month = date.getMonth() + 1;
        let day = date.getDate();
        let hour = date.getHours();
        let min = date.getMinutes();
        let sec = date.getSeconds();
        let msec = date.getMilliseconds();

        let hour12 = hour > 12 ? (hour - 12) : hour;
        let isAm = hour < 12;

        let ret = patten;
        ret = this.replaceAll(ret, 'yyyy', this.paddingString(year, 4, '0'));
        ret = this.replaceAll(ret, 'MM', this.paddingString(month, 2, '0'));
        ret = this.replaceAll(ret, 'dd', this.paddingString(day, 2, '0'));
        ret = this.replaceAll(ret, 'HH', this.paddingString(hour, 2, '0'));
        ret = this.replaceAll(ret, 'mm', this.paddingString(min, 2, '0'));
        ret = this.replaceAll(ret, 'ss', this.paddingString(sec, 2, '0'));
        ret = this.replaceAll(ret, 'SSS', this.paddingString(msec, 3, '0'));
        ret = this.replaceAll(ret, 'hh', this.paddingString(hour12, 2, '0'));

        ret = this.replaceAll(ret, 'y', this.paddingString(year, 0));
        ret = this.replaceAll(ret, 'M', this.paddingString(month, 0));
        ret = this.replaceAll(ret, 'd', this.paddingString(day, 0));
        ret = this.replaceAll(ret, 'H', this.paddingString(hour, 0));
        ret = this.replaceAll(ret, 'm', this.paddingString(min, 0));
        ret = this.replaceAll(ret, 's', this.paddingString(sec, 0));
        ret = this.replaceAll(ret, 'S', this.paddingString(msec, 0));
        ret = this.replaceAll(ret, 'h', this.paddingString(hour12, 0));

        return ret;

    },
    /**
     * 支持将字符串str按照patten格式解析为日期Date对象
     * 支持的格式段：yyyy,MM,dd,HH,mm,ss,SSS
     * 也就是标准Java格式
     * @param str
     * @param patten
     * @returns {Date}
     */
    parseDate(str, patten) {
        str = str + '';
        if (!patten) {
            patten = 'yyyy-MM-dd HH:mm:ss SSS';
        }
        let idxYear4 = patten.indexOf('yyyy');
        let idxMonth2 = patten.indexOf('MM');
        let idxDay2 = patten.indexOf('dd');
        let idxHour2 = patten.indexOf('HH');
        let idxMin2 = patten.indexOf('mm');
        let idxSec2 = patten.indexOf('ss');
        let idxMsec3 = patten.indexOf('SSS');
        let idxHour122 = patten.indexOf('hh');

        let year = 1980;
        let month = 1;
        let day = 1;
        let hour = 0;
        let min = 0;
        let sec = 0;
        let msec = 0;

        if (idxYear4 >= 0) {
            year = parseInt(str.substring(idxYear4, idxYear4 + 4));
        }
        if (idxMonth2 >= 0) {
            month = parseInt(str.substring(idxMonth2, idxMonth2 + 2));
        }
        if (idxDay2 >= 0) {
            day = parseInt(str.substring(idxDay2, idxDay2 + 2));
        }
        if (idxHour2 >= 0) {
            hour = parseInt(str.substring(idxHour2, idxHour2 + 2));
        }
        if (idxMin2 >= 0) {
            min = parseInt(str.substring(idxMin2, idxMin2 + 2));
        }
        if (idxSec2 >= 0) {
            sec = parseInt(str.substring(idxSec2, idxSec2 + 2));
        }
        if (idxMsec3 >= 0) {
            msec = parseInt(str.substring(idxMsec3, idxMsec3 + 3));
        }
        return new Date(year, month - 1, day, hour, min, sec, msec);
    },
    /**
     * 支持自动解析的格式列表
     * fmt为格式，patten为格式对应的regex
     * @returns {({patten: RegExp, fmt: string}|{patten: RegExp, fmt: string}|{patten: RegExp, fmt: string}|{patten: RegExp, fmt: string}|{patten: RegExp, fmt: string})[]}
     */
    supportAutoParseDatePattens() {
        return [
            {
                fmt: 'yyyy-MM-dd HH:mm:ss SSS',
                patten: /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2} \d{3}$/,
            },
            {
                fmt: 'yyyy-MM-dd HH:mm:ss',
                patten: /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/,
            },
            {
                fmt: 'yyyy-MM-dd',
                patten: /^\d{4}-\d{2}-\d{2}$/,
            },
            {
                fmt: 'yyyy年MM月dd日 HH时mm分ss秒',
                patten: /^\d{4}年\d{2}月\d{2}日 \d{2}时\d{2}分\d{2}秒$/,
            },
            {
                fmt: 'yyyyMMdd',
                patten: /^\d{4}\d{2}\d{2}$/,
            },
            {
                fmt: 'yyyyMM',
                patten: /^\d{4}\d{2}$/,
            },
            {
                fmt: 'yyyy',
                patten: /^\d{4}$/,
            }
        ];
    },
    isSupportAutoParse(str) {
        if ($chk.isBlank(str)) {
            return false;
        }
        let arr = this.supportAutoParseDatePattens();
        for (let i = 0; i < arr.length; i++) {
            if (arr[i].patten.test(str)) {
                return true;
            }
        }
        return false;
    },
    /**
     * 常用的日期格式解析
     * @param str
     */
    fromStr(str, defVal = new Date()) {
        if ($chk.isBlank(str)) {
            return defVal;
        }
        let arr = this.supportAutoParseDatePattens();
        for (let i = 0; i < arr.length; i++) {
            if (arr[i].patten.test(str)) {
                return this.parseDate(str, arr[i].fmt);
            }
        }
        return defVal;
    },
    /**
     * 转换日期格式，给定原始日期串，源格式串，新格式串
     * @param str
     * @param srcPatten
     * @param dstPatten
     * @returns {string}
     */
    convertDateFmt(str, srcPatten, dstPatten) {
        let date = this.parseDate(str, srcPatten);
        return this.formatDate(date, dstPatten);
    },
    /**
     * 自动转化支持的时间格式
     * @param str
     * @param dstPatten
     * @returns {string}
     */
    convertSupportDateFmt(str, dstPatten) {
        let date = this.fromStr(str, null);
        return this.formatDate(date, dstPatten);
    },
    /**
     * 转换日期为一个结构对象
     * @param date
     * @returns {{month: number, hour: number, year: number, millisecond: number, day: number, minute: number, second: number}}
     */
    toDateObj(date) {
        if (!date) {
            date = new Date();
        }
        let obj = {
            year: date.getFullYear(),
            month: date.getMonth() + 1,
            day: date.getDate(),
            hour: date.getHours(),
            minute: date.getMinutes(),
            second: date.getSeconds(),
            millisecond: date.getMilliseconds()
        };
        return obj;
    },
    /**
     * 将日期结构对象转换为日期
     * @param obj
     * @returns {Date}
     */
    parseDateObj(obj) {
        if (!obj) {
            return new Date();
        }
        return new Date(obj.year ? obj.year : 1980,
            obj.month ? obj.month - 1 : 0,
            obj.day ? obj.day : undefined,
            obj.hour ? obj.hour : undefined,
            obj.minute ? obj.minute : undefined,
            obj.second ? obj.second : undefined,
            obj.millisecond ? obj.millisecond : undefined
        );
    },
}

/**
 * 字符串工具
 * @type {{toCamel(*): string, toInt(*=, *=): number, parseJson(*=): any, trunc(*=, *=): (*|string), toBoolean(*): boolean, paddingString(*, *=, *, *=): string, toFloat(*=): number, format(*=, *, *=): *, firstUpper(*): (*), toPascal(*): string, firstLower(*): (*), replaceAll(*, *=, *): string}}
 */
window.$str = {
    toInt(str, radix = 10) {
        return parseInt(str, radix);
    },
    toFloat(str) {
        return parseFloat(str);
    },
    toBoolean(str) {
        if (str == 'true') {
            return true;
        } else if (str == 'false') {
            return false;
        }
        return false;
    },
    parseJson(str) {
        return JSON.parse(str);
    },
    trunc(str, maxLen = -1) {
        let mlen = parseInt(maxLen);
        if (mlen <= 3) {
            return str;
        }
        return str.substring(str, 0, maxLen - 3) + '...';
    },
    firstLower(str) {
        if (str.length >= 1) {
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        }
        return str;
    },
    firstUpper(str) {
        if (str.length >= 1) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return str;
    },
    toCamel(str) {
        let ret = '';
        let arr = str.split(/-|_/);
        for (let i = 0; i < arr.length; i++) {
            let item = arr[i].toLowerCase();
            if (i == 0) {
                ret = ret + this.$firstLower(item);
            } else {
                ret = ret + this.$firstUpper(item);
            }
        }
        return ret;
    },
    toPascal(str) {
        let ret = '';
        let arr = str.split(/-|_/);
        for (let i = 0; i < arr.length; i++) {
            let item = arr[i].toLowerCase();
            ret = ret + this.$firstUpper(item);
        }
        return ret;
    },
    /**
     * 使用split&join方式实现replaceAll,
     * 由于部分浏览器不支持replaceAll函数
     * @param str
     * @param reg
     * @param rep
     */
    replaceAll(str, reg, rep) {
        let ret = '';
        let arr = str.split(reg);
        for (let i = 0; i < arr.length; i++) {
            if (i != 0) {
                ret += rep;
            }
            ret += arr[i];
        }

        return ret;
    },
    /**
     * 对str字符串填充到指定长度使用pad字符串进行填充
     * pad长度不足以填充时，pad将会重复用来填充
     * @param str
     * @param len
     * @param pad
     * @returns {string}
     */
    paddingString(str, len, pad, isLeftPad = true) {
        str = str + '';
        pad = pad + '';
        len = parseInt(len);
        if (str.length >= len) {
            return str;
        }
        if (pad == '') {
            pad = ' ';
        }
        let diffLen = len - str.length;
        let padStr = '';
        let padLen = pad.length;
        let times = Math.floor(diffLen / padLen);
        for (let i = 0; i < times; i++) {
            padStr = padStr + pad;
        }
        let moreLen = diffLen - padStr.length;
        padStr = padStr + pad.substring(0, moreLen);
        if (isLeftPad) {
            return padStr + str;
        } else {
            return str + padStr;
        }
    },
    /**
     * 格式化字符串
     * 取值表达式
     * {取值方式表达式}
     * {取值方式表达式:字符串填充长度}
     * {取值方式表达式:字符串填充长度[填充字符串]}
     *
     * 使用示例：
     * 示例1：通过数组下标方式
     * $str.format('a{0}b{1:2}c{2:4[0]}d{3:-2[#]}e{4:+5[*]}f{0}g',[0,1,2,3,4])
     * 返回结果：
     * "a0b 1c0002d3#e****4f0g"
     *
     * 示例2：通过对象属性名方式
     * $str.format('{_} {_.index:2[0]} {_.names[1]:10}',{index:2,names:['a','b']})
     * 返回结果：
     * "[object Object] 02          b"
     *
     * 注意，混用模式需要注意自己的取值方式
     * 正例：
     * $str.format('{0:-4[0]} {_[1].names[1]:4[#]}',[1,{names:['a','b']}])
     * "1000 ###b"
     * @param obj
     * @param val
     * @param jsonfy 是否将值JSON化
     * @returns {*}
     */
    format(fmt, args, jsonfy = false) {
        //占位符：{3:+12[0]} 标识，取第三个参数，左边用0填充补足12位
        //尝试支持对象属性
        //{_.name:-14[ ]}
        //第一级参数认为是format的args参数本身
        //以后的级数就是args的内部路由
        let ret = '';
        let jsonRoute = '[a-zA-Z_$][a-zA-Z0-9_$]*(\\[\\d+\\])?(\\.[a-zA-Z_$][a-zA-Z0-9_$]*(\\[\\d+\\])?)*';
        let reg = new RegExp('\\{(\\d+|' + jsonRoute + ')(:[+|-]?\\d+(\\[.+?\\])?)?\\}', 'g');
        let result = null;
        let lidx = 0;
        while ((result = reg.exec(fmt)) != null) {
            //获取正则表达式匹配项
            let exp = result[0];
            let index = result.index;
            let expLen = exp.length;

            //去除匹配项的{}包裹
            let expv = exp.substring(1, exp.length - 1);

            //取值的JSONroute或者下标
            let argIdx = expv;

            let spIdx = expv.indexOf(":");
            if (spIdx >= 0) {
                argIdx = expv.substring(0, spIdx);

                expv = expv.substring(spIdx + 1);
            } else {
                expv = "";
            }

            //判断是左填充
            let argLeftPad = true;
            if (expv.charAt(0) == '+') {
                argLeftPad = true;
                expv = expv.substring(1);
            } else if (expv.charAt(0) == '-') {
                argLeftPad = false;
                expv = expv.substring(1);
            }

            //获取填充字符串
            let argLen = expv;
            let qtIdx = expv.indexOf('[');
            if (qtIdx >= 0) {
                argLen = expv.substring(0, qtIdx);
                expv = expv.substring(qtIdx);
                expv = expv.substring(1, expv.length - 1);
            } else {
                expv = "";
            }

            let argPadContent = expv;

            //拿到对应的填充对象
            let arg = undefined;

            if (/^\d+$/.test(argIdx)) {
                argIdx = parseInt(argIdx);
                arg = args[argIdx];
            } else if (new RegExp(jsonRoute).test(argIdx)) {
                let dotIdx = argIdx.indexOf('.');
                if (dotIdx >= 0) {
                    let narg = args;
                    let pbase = argIdx.substring(0, dotIdx);
                    let kidx = pbase.indexOf('[');
                    if (kidx >= 0) {
                        let vidx = pbase.substring(kidx + 1, pbase.length - 1);
                        narg = args[parseInt(vidx)];
                    }
                    let droute = argIdx.substring(dotIdx + 1);
                    arg = $obj.deepVal(narg, droute);
                } else {
                    arg = args;
                }
            }

            //拼接进行填充
            let argContent = '';
            if (jsonfy) {
                argContent += '' + JSON.stringify(arg);
            } else {
                argContent += '' + arg;
            }
            if (!$chk.isBlank(argLen)) {
                argLen = parseInt(argLen);
                if ($chk.isBlank(argPadContent)) {
                    argPadContent = ' ';
                }
                argContent = this.paddingString(argContent, argLen, argPadContent, argLeftPad);
            }
            //匹配之前的部分原文
            ret += fmt.substring(lidx, index);
            lidx = index + expLen;

            ret += argContent;

        }
        //最后未匹配的原文拷贝
        if (lidx < fmt.length) {
            ret += fmt.substring(lidx);
        }

        return ret;
    },
    /**
     * 数据脱敏，开头保持多少位，结束保持多少位，总长度多少位
     * @param str
     * @param keepStartLen
     * @param keepEndLen
     * @param maxLen
     */
    hideSensibleInfo(str,keepStartLen,keepEndLen=0,maxLen=-1){
        if($chk.isBlank(str)){
            return str;
        }
        str=str+'';
        let start=str.substring(0,keepStartLen);
        let end=str.substring(str.length-keepEndLen);
        let mid=str.substring(keepStartLen,str.length-keepEndLen);
        if(maxLen>=0){
            let llen=maxLen-keepStartLen-keepEndLen;
            if(llen>=0){
                mid=mid.substring(0,llen);
            }
        }
        let mlen=mid.length;
        mid='';
        for(let i=0;i<mlen;i++){
            mid+='*';
        }
        return start+mid+end;
    }
}

/**
 * JSON工具
 * @type {{toJson(*=): string, parseJson(*=): any}}
 */
window.$json = {
    toJson(obj) {
        return JSON.stringify(obj);
    },
    parseJson(json) {
        return JSON.parse(json);
    }
}

/**
 * 数组工具
 * @type {{appearCount(*=, *=): *[], max(*=, *=): (null|*), dequeue(*=): (null|*), sort(*=, *=, *=): (*), remove(*=, *=): (*), push(*=, *=): (*), each(*=, *=): (*|undefined), pop(*=): (null|*), filter(*=, *=): (*), enqueue(*=, *=): *, min(*=, *=): (null|*), top(*=): (null|*), unique(*=, *=): (*), shuffle(*=, *=): (*), indexOf(*=, *=, *=): (*|number), map(*=, *=): (*), first(*=): (null|*), combine(*, *): *}}
 */
window.$arr = {
    indexOf(arr, elem, comparator = $interface.comparator) {
        if (!$chk.isArray(arr)) {
            return arr;
        }
        for (let i = 0; i < arr.length; i++) {
            if (comparator(arr[i], elem) == 0) {
                return i;
            }
        }
        return -1;
    },
    remove(arr, idx) {
        if (!$chk.isArray(arr)) {
            return arr;
        }
        if (idx < 0 || idx >= arr.length) {
            return arr;
        }
        arr.splice(idx, 1);
        return arr;
    },
    push(arr, elem) {
        if (!$chk.isArray(arr)) {
            return arr;
        }
        arr.push(elem);
        return arr;
    },
    pop(arr) {
        if (!$chk.isArray(arr)) {
            return null;
        }
        if (arr.length == 0) {
            return null;
        }
        let elem = arr[arr.length - 1];
        arr.splice(arr.length - 1, 1);
        return elem;
    },
    top(arr) {
        if (!$chk.isArray(arr)) {
            return null;
        }
        if (arr.length == 0) {
            return null;
        }
        return arr[arr.length - 1];
    },
    enqueue(arr, elem) {
        return this.push(arr, elem);
    },
    dequeue(arr) {
        if (!$chk.isArray(arr)) {
            return null;
        }
        if (arr.length == 0) {
            return null;
        }
        let elem = arr[0];
        arr.splice(0, 1);
        return elem;
    },
    first(arr) {
        if (!$chk.isArray(arr)) {
            return null;
        }
        if (arr.length == 0) {
            return null;
        }
        let elem = arr[0];
        return elem;
    },
    combine(arr1, arr2) {
        return [...arr1, ...arr2];
    },
    unique(arr, comparator = $interface.comparator) {
        if (!$chk.isArray(arr)) {
            return arr;
        }
        let ret = [];
        for (let i = 0; i < arr.length; i++) {
            let isIn = false;
            for (let j = 0; j < ret.length; j++) {
                if (comparator(arr[i], ret[j]) == 0) {
                    isIn = true;
                    break;
                }
            }
            if (!isIn) {
                ret.push(arr[i]);
            }
        }
        return ret;
    },
    min(arr, comparator = $interface.comparator) {
        if (!$chk.isArray(arr)) {
            return null;
        }
        if (arr.length == 0) {
            return null;
        }
        let min = arr[0];
        for (let i = 1; i < arr.length; i++) {
            if (comparator(min, arr[i]) < 0) {
                min = arr[i];
            }
        }
        return min;
    },
    max(arr, comparator = $interface.comparator) {
        if (!$chk.isArray(arr)) {
            return null;
        }
        if (arr.length == 0) {
            return null;
        }
        let max = arr[0];
        for (let i = 1; i < arr.length; i++) {
            if (comparator(max, arr[i]) > 0) {
                max = arr[i];
            }
        }
        return max;
    },
    /**
     * 得到元素出现次数的列表
     * @param arr
     * @param comparator
     * @returns {[]}
     */
    appearCount(arr, comparator = $interface.comparator) {
        // 元素 {item:,count:}
        let ret = [];
        if (!$chk.isArray(arr)) {
            return ret;
        }
        for (let i = 0; i < arr.length; i++) {
            let isIn = false;
            for (let j = 0; j < ret.length; j++) {
                if (comparator(arr[i], ret[j].item) == 0) {
                    ret[j].count += 1;
                    isIn = true;
                    break;
                }
            }
            if (!isIn) {
                ret.push({
                    item: arr[i],
                    count: 1
                });
            }
        }
        this.sort(ret, false, function (item1, item2) {
            return item2.count - item1.count;
        });
        return ret;
    },
    sort(arr, desc = true, comparator = $interface.comparator) {
        if (!$chk.isArray(arr)) {
            return arr;
        }
        if (arr.length == 0) {
            return arr;
        }
        for (let i = 0; i < arr.length; i++) {
            for (let j = i + 1; j < arr.length; j++) {
                if ((comparator(arr[i], arr[j]) < 0) == desc) {
                    let tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
        return arr;
    },
    shuffle(arr, factor = 1.0) {
        if (!$chk.isArray(arr)) {
            return arr;
        }
        if (arr.length == 0) {
            return arr;
        }
        let times = Math.floor(arr.length * factor);
        for (let i = 0; i < times; i++) {
            let pi = Math.floor(Math.random() * arr.length);
            let ei = Math.floor(Math.random() * arr.length);
            if (pi != ei) {
                let tmp = arr[pi];
                arr[pi] = arr[ei];
                arr[ei] = tmp;
            }
        }
        return arr;
    },
    each(arr, executor) {
        if (!$chk.isArray(arr)) {
            return arr;
        }
        if ($chk.isEmpty(executor)) {
            return;
        }
        for (let i = 0; i < arr.length; i++) {
            executor(arr[i], i, arr);
        }
        return arr;
    },
    map(arr, mapper = $interface.mapper) {
        if (!$chk.isArray(arr)) {
            return arr;
        }
        let ret = [];
        for (let i = 0; i < arr.length; i++) {
            ret[i] = mapper(arr[i], i, arr);
        }
        return ret;
    },
    filter(arr, filter = $interface.filter) {
        if (!$chk.isArray(arr)) {
            return arr;
        }
        let ret = [];
        for (let i = 0; i < arr.length; i++) {
            if (filter(arr[i], i, arr)) {
                ret.push(arr[i]);
            }
        }
        return ret;
    }
}

/**
 * 对象工具
 * @type {{toJson(*=): string, copyObj2(*=, *=): *, methods(*): [], copyObj(*=, *): ({}|*), deepVal(*, *=): (*), copyArr(*, *, *): [], fields(*): [], emptyObj(*=, *=): (*), each(*=, *=): (undefined)}}
 */
window.$obj = {
    toJson(obj) {
        return JSON.stringify(obj);
    },
    fields(obj) {
        let arr = [];
        for (let key in obj) {
            if (obj[key] instanceof Function) {
                continue;
            }
            arr.push(key);
        }
        return arr;
    },
    methods(obj) {
        let arr = [];
        for (let key in obj) {
            if (obj[key] instanceof Function) {
                arr.push(key);
            }
        }
        return arr;
    },
    each(obj,executor){
        if($chk.isEmpty(obj)){
            return ;
        }
        if($chk.isEmpty(executor)){
            return ;
        }
        for (let key in obj) {
            if (obj[key] instanceof Function) {
                continue;
            }
            executor(obj[key],key,obj);
        }
    },
    /**
     * 深层次获取值，找不到不报错
     * 例如：
     * route=arg.vals[2].key
     * 那就等价于：
     * obj.arg.vals[2].key
     * @param route
     * @returns {Object}
     */
    deepVal(obj, route) {
        if (!route || route == '') {
            return obj;
        }
        let arr = route.split('.');
        let ret = obj;
        for (let i = 0; i < arr.length; i++) {
            if (!ret) {
                break;
            }
            let cur = arr[i];
            let idx = cur.indexOf('[');
            if (idx >= 0) {
                let av = cur.substring(0, idx);
                let nv = cur.substring(idx + 1);
                if (av != '') {
                    ret = ret[av];
                }
                if (nv != '') {
                    nv = nv.substring(0, nv.length - 1);
                    if (nv != '') {
                        ret = ret[parseInt(nv)];
                    }
                }
            } else {
                ret = ret[cur];
            }
        }
        return ret;
    },
    emptyObj(obj, val = '') {
        if (!obj) {
            return obj;
        }
        for (let attr in obj) {
            obj[attr] = val;
        }
        return obj;
    },
    /**
     * 拷贝obj的在attrArray数组中指明的属性列表到新的对象
     * @param obj
     * @param attrArray
     * @returns {{}|*}
     */
    copyObj(obj, attrArray) {
        if (!obj) {
            return obj;
        }
        let ret = {};
        let len = attrArray.length;
        for (let field in obj) {
            for (let i = 0; i < len; i++) {
                let attr = attrArray[i];
                if (field == attr) {
                    ret[field] = obj[field];
                    break;
                }
            }
        }
        return ret;
    },
    /**
     * 拷贝srcObj中的属性到dstObj中，仅拷贝二者共有的属性
     * @param srcObj
     * @param dstObj
     * @returns {*}
     */
    copyObj2(srcObj, dstObj) {
        if (!dstObj) {
            return dstObj;
        }
        if (!srcObj) {
            return srcObj;
        }
        for(let dkey in dstObj){
            if (dstObj[dkey] instanceof Function) {
                continue;
            }
            for(let skey in srcObj){
                if(srcObj[skey] instanceof Function){
                    continue;
                }
                if(dkey==skey){
                    dstObj[dkey]=srcObj[skey];
                }
            }
        }
        return dstObj;
    },
    copyArr(arr, offset, len) {
        let arrLen = arr.length;
        let ret = [];
        for (let i = offset; i < arrLen && i < (offset + len); i++) {
            ret.push(arr[i]);
        }
        return ret;
    },
    /**
     * 深拷贝
     * @param obj
     * @returns {any}
     */
    deepCopy(obj){
        let js=JSON.stringify(obj);
        return JSON.parse(js);
    }
}

/**
 * DOM工具
 * @type {{registerScaleFullPage(*=, *=, *=): void, scaleFixedCenterElem(*=, *, *): void, reloadJs(*=, *): void, dom(*=): any, scalePage(*=, *, *): {ratioX: number, ratioY: number}, locationTo(*, *=): void, style(*, *): *, reloadCss(*=, *): void, registerOnElementChange(*, *=, *=, *=, *=): void, resetTitle(*): void, remove(*): *}}
 */
window.$dom = {
    remove(dom) {
        let parent = dom.parentNode;
        let ret = dom;
        parent.removeChild(ret);
        return ret;
    },
    style(dom, sylObj) {
        for (let att in sylObj) {
            let name = att.$toCamel();
            dom.style[name] = sylObj[att];
        }
        return dom;
    },
    dom(name) {
        return document.createElement(name);
    },
    /**
     * 设置标题
     * @param title
     */
    resetTitle(title) {
        let tar = document.getElementsByTagName("title")[0];
        tar.innerHTML = title;
    },
    /**
     * 重新加载CSS
     * @param id
     * @param src
     */
    reloadCss(id, src) {
        let tar = document.getElementById(id);
        tar.parentNode.removeChild(tar);
        var domObj = this.dom("link");
        domObj.href = src;
        domObj.rel = "stylesheet";
        domObj.id = id;
        document.getElementsByTagName("head")[0].appendChild(domObj);
    },
    // 重新加载JS文件
    // 示例：
    // reloadJs('jsDataId','/static/data.js');
    reloadJs(id, src) {
        let tar = document.getElementById(id);
        tar.parentNode.removeChild(tar);
        var scriptObj = this.dom("script");
        scriptObj.src = src;
        scriptObj.type = "text/javascript";
        scriptObj.id = id;
        document.getElementsByTagName("head")[0].appendChild(scriptObj);
    },
    // 强制缩放屏幕至全屏
    // 示例：
    // let ratio=scalePage('body',1920,768);
    scalePage(elemSelector, originWidth, originHeight) {
        let innerWidth = window.innerWidth;
        let ratioX = innerWidth / originWidth;
        let innerHeight = window.innerHeight;
        let ratioY = innerHeight / originHeight;
        let elem = document.querySelector(elemSelector);
        elem.style.transform = 'scale(' + ratioX + ',' + ratioY + ')';
        elem.style.transformOrigin = '0 0';
        elem.style.overflow = 'auto';
        return {ratioX: ratioX, ratioY: ratioY};
    },
    // 强制缩放元素到恒居中固定
    // 示例：scaleFixedCenterElem('.el-dialog',0.5,0.5);
    // 一般结合全屏缩放参数使用：
    // let ratio=scalePage('body',1920,768);
    // scaleFixedCenterElem('.el-dialog',ratio.ratioX,ratio.ratioY);
    scaleFixedCenterElem(elemSelector, ratioX, ratioY) {
        let elems = document.querySelectorAll(elemSelector);
        elems.forEach(item => {
            item.style.transform = 'scale(' + ratioX + ',' + ratioY + ') translateX(-50%)';
            item.style.transformOrigin = '0 0';
            item.style.left = '50%';
            item.style.position = 'fixed';
        })
    },
    // 注册当屏幕大小改变时强制缩放，实现自适应分辨率缩放
    // 示例：
    // registerScaleFullPage('body',1920,768);
    registerScaleFullPage(elemSelector, originWidth, originHeight) {
        this.scalePage(elemSelector, originWidth, originHeight);
        window.onresize = function () {
            this.scalePage(elemSelector, originWidth, originHeight);
        }
    },
    // 注册元素改变监听器，可以在此实现回调
    // 示例：
    // registerOnElementChange(func,'body',true,true,true);
    registerOnElementChange(callbackFunc, elemSelector, needListenAttr, needListenChild, needListenSubTree) {

        if (elemSelector == undefined) {
            elemSelector = 'body';
        }
        if (needListenAttr == undefined) {
            needListenAttr = true;
        }
        if (needListenChild == undefined) {
            needListenChild = true;
        }
        if (needListenSubTree == undefined) {
            needListenSubTree = true;
        }

        // 选择需要观察变动的节点
        const targetNode = document.querySelector(elemSelector);

        // 观察器的配置（需要观察什么变动）
        const config = {attributes: needListenAttr, childList: needListenChild, subtree: needListenSubTree};

        // 当观察到变动时执行的回调函数
        const callback = function (mutationsList, observer) {
            // Use traditional 'for loops' for IE 11
            // for(let mutation of mutationsList) {
            //   if (mutation.type === 'childList') {
            //     console.log('A child node has been added or removed.');
            //   }
            //   else if (mutation.type === 'attributes') {
            //     console.log('The ' + mutation.attributeName + ' attribute was modified.');
            //   }
            // }
            callbackFunc(mutationsList, observer);
        };

        // 创建一个观察器实例并传入回调函数
        const observer = new MutationObserver(callback);

        // 以上述配置开始观察目标节点
        observer.observe(targetNode, config);
    },
    /**
     * 借助a标签实现跳转，可用于点击下载文件或者跳转等情况
     * @param href
     * @param target
     */
    locationTo(href, target = '_blank') {
        let id = 'tmpClickIdForTagA';

        var scriptObj = document.createElement("a");
        scriptObj.href = href;
        scriptObj.target = "_blank";
        scriptObj.id = id;
        document.getElementsByTagName("body")[0].appendChild(scriptObj);
        scriptObj.click();
        document.getElementsByTagName("body")[0].removeChild(scriptObj);
    },

    selectFile(callback,accept='*.*'){
        let id='tmpSelectFileForTagInputFile';

        var scriptObj = document.createElement("input");
        scriptObj.type = 'file';
        scriptObj.accept = accept;
        scriptObj.id = id;
        document.getElementsByTagName("body")[0].appendChild(scriptObj);
        scriptObj.click();

        scriptObj.onchange=function(event){
            callback(this.files[0],event,this.files,this);
            document.getElementsByTagName("body")[0].removeChild(scriptObj);
        }
    }

}

window.$dlg={
    pickColorDialog(config,callback=null){
        let defaultConfig={
            title:'颜色选择器',
            width:'480',
            height:'480',
            attachId:null,
            attachDom:null,
            ctrlHeight:20,
            background:'white',
        };

        $obj.copyObj2(config,defaultConfig);


        defaultConfig.width=parseInt(defaultConfig.width);
        defaultConfig.height=parseInt(defaultConfig.height);
        defaultConfig.ctrlHeight=parseInt(defaultConfig.ctrlHeight);

        let pickColor={
            hex:'#ff0000',
            red:255,
            green:0,
            blue:128,
            alpha:255,
        };

        let virtualId='dlg_color_picker';
        let ckDom=document.getElementById(virtualId);
        if(ckDom){
            ckDom.parentNode.removeChild(ckDom);
        }
        let containDom=document.createElement('div');
        containDom.id=virtualId;
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
        containDom.style.background=defaultConfig.background;

        if(defaultConfig.attachDom){
            let attachDom=defaultConfig.attachDom;
            containDom.style.left=attachDom.offsetLeft+(attachDom.clientWidth/2);
            containDom.style.top=attachDom.offsetTop+(attachDom.clientHeight/2);
        }
        else if(defaultConfig.attachId && defaultConfig.attachId!=''){
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

        let okEventHandler=function(event){
            pickColor.red=Math.max(pickColor.red,0);
            pickColor.green=Math.max(pickColor.green,0);
            pickColor.blue=Math.max(pickColor.blue,0);
            pickColor.alpha=Math.max(pickColor.alpha,0);
            pickColor.red=Math.min(pickColor.red,255);
            pickColor.green=Math.min(pickColor.green,255);
            pickColor.blue=Math.min(pickColor.blue,255);
            pickColor.alpha=Math.min(pickColor.alpha,255);

            let hr=pickColor.red.toString(16);
            let hg=pickColor.green.toString(16);
            let hb=pickColor.blue.toString(16);
            let ha=pickColor.alpha.toString(16);
            if(hr.length==1){
                hr='0'+hr;
            }
            if(hg.length==1){
                hg='0'+hg;
            }
            if(hb.length==1){
                hb='0'+hb;
            }
            if(ha.length==1){
                ha='0'+ha;
            }
            pickColor.hex='#'+hr+hg+hb+ha;
            if(callback){
                callback(pickColor);
            }
        }
        ctrlDom.addEventListener('click',function (event){
            containDom.style.display='none';
            containDom.parentNode.removeChild(containDom);
            okEventHandler(event);
        });


        let blueCanvasDom=document.createElement('canvas');
        containDom.appendChild(blueCanvasDom);
        blueCanvasDom.width=defaultConfig.width;
        blueCanvasDom.height=defaultConfig.ctrlHeight/2;
        let blueCanvasState={
            isDown:false,
        }

        let blueEventHandler=function (event){
            if(blueCanvasState.isDown){
                let posX=(event.offsetX/blueCanvasDom.width)*255;
                posX=Math.round(posX);
                pickColor.blue=posX;
                rerenderArea();
            }
        }
        blueCanvasDom.addEventListener('mousedown',function(event){
            blueCanvasState.isDown=true;
        });
        blueCanvasDom.addEventListener('mousemove',function(event){
            blueEventHandler(event);
        });
        blueCanvasDom.addEventListener('mouseup',function(event){
            blueEventHandler(event);
            blueCanvasState.isDown=false;
        });
        blueCanvasDom.addEventListener('mouseleave',function(event){
            blueEventHandler(event);
            blueCanvasState.isDown=false;
        });

        if(!blueCanvasDom.getContext){
            console.error('not support!');
            return ;
        }

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

        let alphaCanvasState={
            isDown:false,
        }

        let alphaEventHandler=function(event){
            if(alphaCanvasState.isDown){
                let posX=(event.offsetX/alphaCanvasDom.width)*255;
                posX=Math.round(posX);
                pickColor.alpha=posX;
                rerenderArea();
            }
        }

        alphaCanvasDom.addEventListener('mousedown',function(event){
            alphaCanvasState.isDown=true;
        });
        alphaCanvasDom.addEventListener('mousemove',function(event){
            alphaEventHandler(event);
        });
        alphaCanvasDom.addEventListener('mouseup',function(event){
            alphaEventHandler(event);
            alphaCanvasState.isDown=false;
        });
        alphaCanvasDom.addEventListener('mouseleave',function(event){
            alphaEventHandler(event);
            alphaCanvasState.isDown=false;
        });

        if(!alphaCanvasDom.getContext){
            console.log('not support!');
            return ;
        }

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

        let choiceDomState={
            lastRenderTime:0
        };

        let rerenderArea=function(){
            let now=new Date().getTime();
            if(now-choiceDomState.lastRenderTime<100){
                console.log('reduce');
                return ;
            }
            choiceDomState.lastRenderTime=now;
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
                okEventHandler(event);
                containDom.style.display='none';
                containDom.parentNode.removeChild(containDom);
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


window.$canvas={
    point2d(x,y){
        return {
            x:x,
            y:y
        };
    },
    distance(x1,y1,x2,y2){
        return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    },
    distancePoint(p1,p2){
      return this.distance(p1.x,p1.y,p2.x,p2.y);
    },
    regularPoint(p1,p2){
      let minX=Math.min(p1.x,p2.x);
      let minY=Math.min(p1.y,p2.y);
      let maxX=Math.max(p1.x,p2.x);
      let maxY=Math.max(p1.y,p2.y);
      p1.x=minX;
      p1.y=minY;
      p2.x=maxX;
      p2.y=maxY;
    },
    angle2Radian(angle){
      return angle/180*Math.PI;
    },
    middlePoint(p1,p2){
      return {
            x:(p1.x+p2.x)/2,
            y:(p1.y+p2.y)/2,
      }
    },
    size2d(x1,y1,x2,y2){
      return {
          width:Math.abs(x2-x1),
          height:Math.abs(y2-y1)
      };
    },
    sizePoint(p1,p2){
      return this.size2d(p1.x,p1.y,p2.x,p2.y);
    },
    dataUrl2File(dataUrl,fileName){
        let arr=dataUrl.split(',');
        let mime=arr[0].match(/:(.*?);/)[1];
        let bstr=atob(arr[1]);
        let len=bstr.length;
        let u8arr=new Uint8Array(len);
        while(len--){
            u8arr[len]=bstr.charCodeAt(len);
        }
        return new File([u8arr],fileName,{type:mime});
    },
    dc2d2File(dc2dDom,fileName,mime='image/png'){
      let dataUrl=dc2dDom.toDataURL(mime);
      return $canvas.dataUrl2File(dataUrl,fileName);
    },
    downloadDc2d(dc2dDom,fileName,mime='image/png'){
        let dataUrl=dc2dDom.toDataURL(mime);
        let dom=document.createElement('a');
        dom.download=fileName;
        dom.href=dataUrl;
        dom.target='_self';
        document.body.appendChild(dom);
        dom.click();
        document.body.removeChild(dom);
    },
    drawLine(dc2d,p1,p2){
        dc2d.beginPath();
        dc2d.moveTo(p1.x,p1.y);
        dc2d.lineTo(p2.x,p2.y);
        dc2d.stroke();
    },
    drawRect(dc2d,p1,p2){
        let pointBegin=$canvas.point2d(p1.x,p1.y);
        let pointEnd=$canvas.point2d(p2.x,p2.y);
        $canvas.regularPoint(pointBegin,pointEnd);
        let psize=$canvas.sizePoint(pointBegin,pointEnd);
        dc2d.strokeRect(pointBegin.x,pointBegin.y,psize.width,psize.height);
    },
    drawFillRect(dc2d,p1,p2){
        let pointBegin=$canvas.point2d(p1.x,p1.y);
        let pointEnd=$canvas.point2d(p2.x,p2.y);
        $canvas.regularPoint(pointBegin,pointEnd);
        let psize=$canvas.sizePoint(pointBegin,pointEnd);

        dc2d.fillRect(pointBegin.x,pointBegin.y,psize.width,psize.height);
    },
    drawCircle(dc2d,p,radius){
        dc2d.beginPath();
        dc2d.arc(p.x,p.y,radius,0,2*Math.PI,false);
        dc2d.stroke();
        dc2d.closePath();
    },
    drawFillCircle(dc2d,p,radius){
        dc2d.beginPath();
        dc2d.arc(p.x,p.y,dis,0,2*Math.PI,false);
        // dc2d.stroke();
        dc2d.closePath();
        dc2d.fill();
    },
    drawEllipse(dc2d,p1,p2){
        let beginPoint=$canvas.point2d(p1.x,p1.y);
        let endPoint=$canvas.point2d(p2.x,p2.y);
        $canvas.regularPoint(beginPoint,endPoint);
        let psize=$canvas.sizePoint(beginPoint,endPoint);
        let p=$canvas.middlePoint(beginPoint,endPoint);
        let radiusX=psize.width/2;
        let radiusY=psize.height/2;
        dc2d.save();
        let radius=Math.max(radiusX,radiusY);
        let ratioX=radiusX/radius;
        let ratioY=radiusY/radius;
        dc2d.scale(ratioX,ratioY);
        dc2d.beginPath();
        dc2d.moveTo((p.x+radiusX)/ratioX,p.y/ratioY);
        dc2d.arc(p.x/ratioX,p.y/ratioY,radius,0,2*Math.PI,true);
        dc2d.closePath();
        dc2d.stroke();
        dc2d.restore();
    },
    drawFillEllipse(dc2d,p1,p2){
        let beginPoint=$canvas.point2d(p1.x,p1.y);
        let endPoint=$canvas.point2d(p2.x,p2.y);
        $canvas.regularPoint(beginPoint,endPoint);
        let psize=$canvas.sizePoint(beginPoint,endPoint);
        let p=$canvas.middlePoint(beginPoint,endPoint);
        let radiusX=psize.width/2;
        let radiusY=psize.height/2;
        dc2d.save();
        let radius=Math.max(radiusX,radiusY);
        let ratioX=radiusX/radius;
        let ratioY=radiusY/radius;
        dc2d.scale(ratioX,ratioY);
        dc2d.beginPath();
        dc2d.moveTo((p.x+radiusX)/ratioX,p.y/ratioY);
        dc2d.arc(p.x/ratioX,p.y/ratioY,radius,0,2*Math.PI,true);
        // dc2d.stroke();
        dc2d.closePath();
        dc2d.fill();
        dc2d.restore();
    },
    drawRoundRect(dc2d,p1,p2,radius){
        let beginPoint=$canvas.point2d(p1.x,p1.y);
        let endPoint=$canvas.point2d(p2.x,p2.y);
        $canvas.regularPoint(beginPoint,endPoint);
        let psize=$canvas.sizePoint(beginPoint,endPoint);
        if(!radius){
            radius=Math.min(psize.width,psize.height)*1/5;
        }
        if(radius*2>Math.min(psize.width,psize.height)){
            radius=Math.min(psize.width,psize.height)/2;
        }
        dc2d.beginPath();
        dc2d.moveTo(beginPoint.x+radius,beginPoint.y);
        dc2d.arcTo(beginPoint.x+psize.width,beginPoint.y,beginPoint.x+psize.width,beginPoint.y+psize.height,radius);
        dc2d.arcTo(beginPoint.x+psize.width,beginPoint.y+psize.height,beginPoint.x,beginPoint.y+psize.height,radius);
        dc2d.arcTo(beginPoint.x,beginPoint.y+psize.height,beginPoint.x,beginPoint.y,radius);
        dc2d.arcTo(beginPoint.x,beginPoint.y,beginPoint.x+psize.width,beginPoint.y,radius);
        dc2d.stroke();
        dc2d.closePath();
    },
    drawFillRoundRect(dc2d,p1,p2,radius){
        let beginPoint=$canvas.point2d(p1.x,p1.y);
        let endPoint=$canvas.point2d(p2.x,p2.y);
        $canvas.regularPoint(beginPoint,endPoint);
        let psize=$canvas.sizePoint(beginPoint,endPoint);
        if(!radius){
            radius=Math.min(psize.width,psize.height)*1/5;
        }
        if(radius*2>Math.min(psize.width,psize.height)){
            radius=Math.min(psize.width,psize.height)/2;
        }
        dc2d.beginPath();
        dc2d.moveTo(beginPoint.x+radius,beginPoint.y);
        dc2d.arcTo(beginPoint.x+psize.width,beginPoint.y,beginPoint.x+psize.width,beginPoint.y+psize.height,radius);
        dc2d.arcTo(beginPoint.x+psize.width,beginPoint.y+psize.height,beginPoint.x,beginPoint.y+psize.height,radius);
        dc2d.arcTo(beginPoint.x,beginPoint.y+psize.height,beginPoint.x,beginPoint.y,radius);
        dc2d.arcTo(beginPoint.x,beginPoint.y,beginPoint.x+psize.width,beginPoint.y,radius);
        // dc2d.stroke();
        dc2d.closePath();
        dc2d.fill();
    },
    drawPolygon(dc2d,pointArr){
      if(!pointArr || pointArr.length==0){
          return ;
      }
      dc2d.beginPath();
      dc2d.moveTo(pointArr[0].x,pointArr[0].y);
      for(let i=1;i<pointArr.length;i++){
          dc2d.lineTo(pointArr[i].x,pointArr[i].y);
      }
      dc2d.lineTo(pointArr[0].x,pointArr[0].y);
      dc2d.stroke();
      dc2d.closePath();
    },
    drawFillPolygon(dc2d,pointArr){
        if(!pointArr || pointArr.length==0){
            return ;
        }
        dc2d.beginPath();
        dc2d.moveTo(pointArr[0].x,pointArr[0].y);
        for(let i=1;i<pointArr.length;i++){
            dc2d.lineTo(pointArr[i].x,pointArr[i].y);
        }
        dc2d.lineTo(pointArr[0].x,pointArr[0].y);
        // dc2d.stroke();
        dc2d.closePath();
        dc2d.fill();
    },
    initPainter(id,config){
        let defaultConfig={
            width:null,
            height:null,
            border:null,
        };
        $obj.copyObj2(config,defaultConfig);

        let dom=document.getElementById(id);

        if(defaultConfig.width){
            dom.width=defaultConfig.width;
            dom.style.width=defaultConfig.width;
        }
        if(defaultConfig.height){
            dom.height=defaultConfig.height;
            dom.style.height=defaultConfig.height;
        }
        if(defaultConfig.border){
            dom.style.border=defaultConfig.border;
        }

        if(!dom.getContext){
            console.error('not support!');
            return ;
        }
        let dc2d=dom.getContext('2d');

        const ConstValues={
            PIm2:2*Math.PI,
            PI:Math.PI,
            PId2:Math.PI/2,
            PI0:0,
        }
        const DrawTypes={
            NONE:'none',
            CLEANER:'cleaner',
            LINE:'line',
            FREE_LINE:'free_line',
            RECT:'rect',
            FILL_RECT:'fill_rect',
            CIRCLE:'circle',
            FILL_CIRCLE:'fill_circle',
            ELLIPSE:'ellipse',
            FILL_ELLIPSE:'fill_ellipse',
            ROUND_RECT:'round_rect',
            FILL_ROUND_RECT:'fill_round_rect',
            POLYGON:'polygon',
            FILL_POLYGON:'fill_polygon',
        }

        const EventTypes={
            MOUSE_DOWN:'mousedown',
            MOUSE_UP:'mouseup',
            MOUSE_MOVE:'mousemove',
            MOUSE_LEAVE:'mouseleave',
            CLICK:'click',
            DBL_CLICK:'dblclick',
            KEY_DOWN:'keydown',
            KEY_UP:'keyup',
            CONTEXT_MENU:'contextmenu',
        }
        const MouseButtons={
            LEFT:0,
            MIDDLE:1,
            RIGHT:2
        }
        const ControlKeyMap={
            None:0,
            Ctrl:1,
            Shift:2,
            Alt:4,

        }
        const ControlKeyCodes={
            Ctrl:17,
            Shift:16,
            Alt:18,

        }

        let contextMenuDom=document.createElement('ul');
        contextMenuDom.id=id+'_contextmenu';
        contextMenuDom.style.position='absolute';
        contextMenuDom.style.display='none';
        contextMenuDom.style.zIndex=2000+dom.style.zIndex;
        contextMenuDom.style.border='solid 1px #eee';
        contextMenuDom.style.background='#eaeaeaaa';
        contextMenuDom.style.padding='5px';
        contextMenuDom.style.borderRadius='5px';
        contextMenuDom.style.boxShadow='1px 2px 5px rgba(0,0,0,0.3)';
        contextMenuDom.style.maxHeight='360px';
        contextMenuDom.style.overflowY='auto';
        dom.parentNode.appendChild(contextMenuDom);

        let state={
            drawType:DrawTypes.FREE_LINE,
            strokeStyle:'black',
            fillStyle:'black',
            lineWidth:'1px',
            isDrawing:false,
            beginPoint:{
                x:0,
                y:0,
            },
            controlKey:0,
            contextMenu:contextMenuDom,
            points:[],
        };

        let graphicsMenuItems=[
            {
                text:'无',
                value:{
                    drawType:DrawTypes.NONE,
                }
            },
            {
                text:'橡皮',
                value:{
                    drawType:DrawTypes.CLEANER,
                }
            },
            {
                text:'直线',
                value:{
                    drawType:DrawTypes.LINE,
                }
            },
            {
                text:'曲线',
                value:{
                    drawType:DrawTypes.FREE_LINE,
                }
            },
            {
                text:'矩形',
                value:{
                    drawType:DrawTypes.RECT,
                }
            },
            {
                text:'填充矩形',
                value:{
                    drawType:DrawTypes.FILL_RECT,
                }
            },
            {
                text:'圆角矩形',
                value:{
                    drawType:DrawTypes.ROUND_RECT,
                }
            },
            {
                text:'填充圆角矩形',
                value:{
                    drawType:DrawTypes.FILL_ROUND_RECT,
                }
            },
            {
                text:'圆形',
                value:{
                    drawType:DrawTypes.CIRCLE,
                }
            },
            {
                text:'填充圆形',
                value:{
                    drawType:DrawTypes.FILL_CIRCLE,
                }
            },
            {
                text:'椭圆形',
                value:{
                    drawType:DrawTypes.ELLIPSE,
                }
            },
            {
                text:'填充椭圆形',
                value:{
                    drawType:DrawTypes.FILL_ELLIPSE,
                }
            },
            {
                text:'多边形',
                value:{
                    drawType:DrawTypes.POLYGON,
                }
            },
            {
                text:'填充多边形',
                value:{
                    drawType:DrawTypes.FILL_POLYGON,
                }
            },
        ];

        let childFstDom=document.createElement('ul');
        childFstDom.style.listStyle='none';
        childFstDom.innerText='> 图形';
        childFstDom.addEventListener('click',function(event){
            let cdom=document.getElementById(id+'_contextmenu_graph_container');
            if(cdom.style.display=='none'){
                cdom.style.display='block';
            }else{
                cdom.style.display='none';
            }
        });
        contextMenuDom.appendChild(childFstDom);

        let fstContainDom=document.createElement('div');
        fstContainDom.id=id+'_contextmenu_graph_container';
        childFstDom.appendChild(fstContainDom);

        for (let i = 0; i < graphicsMenuItems.length; i++) {
            let item=graphicsMenuItems[i];
            let childSecDom=document.createElement('li');
            childSecDom.innerText=item.text;
            childSecDom.addEventListener('click',function(event){
                state.drawType=item.value.drawType;
                state.contextMenu.style.display='none';
            });
            childSecDom.addEventListener('mouseover',function(event){
                event.target.style.borderLeft='solid 2px orangered';
                event.target.style.paddingLeft='4px';
            });
            childSecDom.addEventListener('mouseleave',function(event){
                event.target.style.borderLeft='none';
                event.target.style.paddingLeft='0';
            });
            fstContainDom.appendChild(childSecDom);
        }


        let lineColorMenuItems=[
            {
                text:'黑色',
                value:{
                    strokeStyle:'black',
                }
            },
            {
                text:'红色',
                value:{
                    strokeStyle:'red',
                }
            },
            {
                text:'橙红色',
                value:{
                    strokeStyle:'orangered',
                }
            },
            {
                text:'绿色',
                value:{
                    strokeStyle:'green',
                }
            },
            {
                text:'蓝色',
                value:{
                    strokeStyle:'blue',
                }
            },
            {
                text:'白色',
                value:{
                    strokeStyle:'white',
                }
            },
        ];

        childFstDom=document.createElement('ul');
        childFstDom.style.listStyle='none';
        childFstDom.innerText='> 线颜色';
        childFstDom.addEventListener('click',function(event){
            let cdom=document.getElementById(id+'_contextmenu_line_color_container');
            if(cdom.style.display=='none'){
                cdom.style.display='block';
            }else{
                cdom.style.display='none';
            }
        });
        contextMenuDom.appendChild(childFstDom);

        fstContainDom=document.createElement('div');
        fstContainDom.id=id+'_contextmenu_line_color_container';
        fstContainDom.style.display='none';
        childFstDom.appendChild(fstContainDom);

        for (let i = 0; i < lineColorMenuItems.length; i++) {
            let item=lineColorMenuItems[i];
            let childSecDom=document.createElement('li');
            childSecDom.innerText=item.text;
            childSecDom.style.borderBottom='solid 2px '+item.value.strokeStyle;
            childSecDom.addEventListener('click',function(event){
                state.strokeStyle=item.value.strokeStyle;
                state.contextMenu.style.display='none';
            });
            childSecDom.addEventListener('mouseover',function(event){
                event.target.style.borderLeft='solid 2px orangered';
                event.target.style.paddingLeft='4px';
            });
            childSecDom.addEventListener('mouseleave',function(event){
                event.target.style.borderLeft='none';
                event.target.style.paddingLeft='0';
            });
            fstContainDom.appendChild(childSecDom);

        }

        if($dlg && $dlg.pickColorDialog){
            let childSecDom=document.createElement('li');
            childSecDom.innerText='自定义';
            childSecDom.style.borderBottom='solid 2px #00ffff88';
            childSecDom.addEventListener('click',function(event){
                $dlg.pickColorDialog({
                    width:256,
                    height:256,
                    attachDom: contextMenuDom
                },function(color){
                    state.strokeStyle=color.hex;
                });
                state.contextMenu.style.display='none';
            });
            childSecDom.addEventListener('mouseover',function(event){
                event.target.style.borderLeft='solid 2px orangered';
                event.target.style.paddingLeft='4px';
            });
            childSecDom.addEventListener('mouseleave',function(event){
                event.target.style.borderLeft='none';
                event.target.style.paddingLeft='0';
            });
            fstContainDom.appendChild(childSecDom);
        }

        let lineWidthMenuItems=[
            {
                text:'1px',
                value:{
                    lineWidth:'1',
                }
            },
            {
                text:'2px',
                value:{
                    lineWidth:'2',
                }
            },
            {
                text:'3px',
                value:{
                    lineWidth:'3',
                }
            },
            {
                text:'5px',
                value:{
                    lineWidth:'5',
                }
            },
            {
                text:'8px',
                value:{
                    lineWidth:'8',
                }
            },
            {
                text:'12px',
                value:{
                    lineWidth:'12',
                }
            },
            {
                text:'16px',
                value:{
                    lineWidth:'16',
                }
            },
            {
                text:'20px',
                value:{
                    lineWidth:'20',
                }
            },
        ]

        childFstDom=document.createElement('ul');
        childFstDom.style.listStyle='none';
        childFstDom.innerText='> 线粗度';
        childFstDom.addEventListener('click',function(event){
            let cdom=document.getElementById(id+'_contextmenu_line_width_container');
            if(cdom.style.display=='none'){
                cdom.style.display='block';
            }else{
                cdom.style.display='none';
            }
        });
        contextMenuDom.appendChild(childFstDom);

        fstContainDom=document.createElement('div');
        fstContainDom.id=id+'_contextmenu_line_width_container';
        fstContainDom.style.display='none';
        childFstDom.appendChild(fstContainDom);

        for (let i = 0; i < lineWidthMenuItems.length; i++) {
            let item=lineWidthMenuItems[i];
            let childSecDom=document.createElement('li');
            childSecDom.innerText=item.text;
            childSecDom.style.borderBottom='solid '+item.value.lineWidth+'px black';
            childSecDom.addEventListener('click',function(event){
                state.lineWidth=item.value.lineWidth;
                state.contextMenu.style.display='none';
            });
            childSecDom.addEventListener('mouseover',function(event){
                event.target.style.borderLeft='solid 2px orangered';
                event.target.style.paddingLeft='4px';
            });
            childSecDom.addEventListener('mouseleave',function(event){
                event.target.style.borderLeft='none';
                event.target.style.paddingLeft='0';
            });
            fstContainDom.appendChild(childSecDom);
        }

        let fillColorMenuItems=[
            {
                text:'黑色',
                value:{
                    fillStyle:'black',
                    color:'white',
                }
            },
            {
                text:'红色',
                value:{
                    fillStyle:'red',
                    color:'white',
                }
            },
            {
                text:'橙红色',
                value:{
                    fillStyle:'orangered',
                    color:'white',
                }
            },
            {
                text:'绿色',
                value:{
                    fillStyle:'green',
                    color:'white',
                }
            },
            {
                text:'蓝色',
                value:{
                    fillStyle:'blue',
                    color:'white',
                }
            },
            {
                text:'白色',
                value:{
                    fillStyle:'white',
                    color:'black',
                }
            },
        ]

        childFstDom=document.createElement('ul');
        childFstDom.style.listStyle='none';
        childFstDom.innerText='> 填充颜色';
        childFstDom.addEventListener('click',function(event){
            let cdom=document.getElementById(id+'_contextmenu_fill_color_container');
            if(cdom.style.display=='none'){
                cdom.style.display='block';
            }else{
                cdom.style.display='none';
            }
        });
        contextMenuDom.appendChild(childFstDom);

        fstContainDom=document.createElement('div');
        fstContainDom.id=id+'_contextmenu_fill_color_container';
        fstContainDom.style.display='none';
        childFstDom.appendChild(fstContainDom);

        for (let i = 0; i < fillColorMenuItems.length; i++) {
            let item=fillColorMenuItems[i];
            let childSecDom=document.createElement('li');
            childSecDom.innerText=item.text;
            childSecDom.style.background=item.value.fillStyle;
            childSecDom.style.color=item.value.color;
            childSecDom.addEventListener('click',function(event){
                state.fillStyle=item.value.fillStyle;
                state.contextMenu.style.display='none';
            });
            childSecDom.addEventListener('mouseover',function(event){
                event.target.style.borderLeft='solid 2px orangered';
                event.target.style.paddingLeft='4px';
            });
            childSecDom.addEventListener('mouseleave',function(event){
                event.target.style.borderLeft='none';
                event.target.style.paddingLeft='0';
            });
            fstContainDom.appendChild(childSecDom);
        }

        if($dlg && $dlg.pickColorDialog){
            let childSecDom=document.createElement('li');
            childSecDom.innerText='自定义';
            childSecDom.style.background='#00ffff88';
            childSecDom.style.color='black';
            childSecDom.addEventListener('click',function(event){
                $dlg.pickColorDialog({
                    width:256,
                    height:256,
                    attachDom: contextMenuDom
                },function(color){
                    state.fillStyle=color.hex;
                });
                state.contextMenu.style.display='none';
            });
            childSecDom.addEventListener('mouseover',function(event){
                event.target.style.borderLeft='solid 2px orangered';
                event.target.style.paddingLeft='4px';
            });
            childSecDom.addEventListener('mouseleave',function(event){
                event.target.style.borderLeft='none';
                event.target.style.paddingLeft='0';
            });
            fstContainDom.appendChild(childSecDom);
        }

        childFstDom=document.createElement('ul');
        childFstDom.style.listStyle='none';
        childFstDom.innerText='> 工具';
        childFstDom.addEventListener('click',function(event){
            let cdom=document.getElementById(id+'_contextmenu_tools_container');
            if(cdom.style.display=='none'){
                cdom.style.display='block';
            }else{
                cdom.style.display='none';
            }
        });
        contextMenuDom.appendChild(childFstDom);

        fstContainDom=document.createElement('div');
        fstContainDom.id=id+'_contextmenu_tools_container';
        fstContainDom.style.display='none';
        childFstDom.appendChild(fstContainDom);

        let childSecDom=document.createElement('li');
        childSecDom.innerText='保存为PNG图片';
        childSecDom.addEventListener('click',function(event){
            $canvas.downloadDc2d(dom,'save.png','image/png');
            state.contextMenu.style.display='none';
        });
        childSecDom.addEventListener('mouseover',function(event){
            event.target.style.borderLeft='solid 2px orangered';
            event.target.style.paddingLeft='4px';
        });
        childSecDom.addEventListener('mouseleave',function(event){
            event.target.style.borderLeft='none';
            event.target.style.paddingLeft='0';
        });
        fstContainDom.appendChild(childSecDom);

        let eventHandler=function(type,event){
            if(type==EventTypes.KEY_DOWN){

            }
            if(type==EventTypes.KEY_UP){

            }
            if(type==EventTypes.MOUSE_LEAVE){
                state.isDrawing=false;
            }
            if(type==EventTypes.MOUSE_DOWN){
                state.isDrawing=true;
                dc2d.strokeStyle=state.strokeStyle;
                dc2d.fillStyle=state.fillStyle;
                dc2d.lineWidth=state.lineWidth;
                state.beginPoint.x=event.offsetX;
                state.beginPoint.y=event.offsetY;
                if(state.isDrawing && event.button==MouseButtons.LEFT){
                    if(state.drawType==DrawTypes.FREE_LINE){
                        dc2d.beginPath();
                        dc2d.moveTo(event.offsetX,event.offsetY);
                    }
                    if(state.drawType==DrawTypes.CLEANER){
                        let wid=parseInt(state.lineWidth);
                        dc2d.clearRect(event.offsetX-1,event.offsetY-1,wid,wid);
                    }
                }
            }
            if(type==EventTypes.MOUSE_MOVE){
                if(state.isDrawing && event.button==MouseButtons.LEFT){
                    if(state.drawType==DrawTypes.FREE_LINE){
                        dc2d.lineTo(event.offsetX,event.offsetY);
                        dc2d.stroke();
                        dc2d.moveTo(event.offsetX,event.offsetY);
                    }
                    if(state.drawType==DrawTypes.CLEANER){
                        let wid=parseInt(state.lineWidth);
                        dc2d.clearRect(event.offsetX-1,event.offsetY-1,wid,wid);
                    }
                }

            }
            if(type==EventTypes.MOUSE_UP){
                if(event.button==MouseButtons.LEFT){
                    if(state.drawType==DrawTypes.POLYGON){
                        let p=$canvas.point2d(event.offsetX,event.offsetY);
                        state.points.push(p);
                    }
                    if(state.drawType==DrawTypes.FILL_POLYGON){
                        let p=$canvas.point2d(event.offsetX,event.offsetY);
                        state.points.push(p);
                    }
                }
                if(event.button==MouseButtons.RIGHT){
                    state.contextMenu.style.display='none';
                    if(event.ctrlKey){
                        dc2d.clearRect(-1,-1,dom.clientWidth+2,dom.clientHeight+2);
                    }
                    else if(event.altKey)  {
                        if(state.drawType==DrawTypes.POLYGON){
                            $canvas.drawPolygon(dc2d,state.points);
                            state.points=[];
                        }
                        if(state.drawType==DrawTypes.FILL_POLYGON){
                            $canvas.drawFillPolygon(dc2d,state.points);
                            state.points=[];
                        }
                    }else{
                        state.contextMenu.style.left=event.clientX+'px';
                        state.contextMenu.style.top=event.clientY+'px';
                        state.contextMenu.style.display='block';
                    }
                }else{
                    state.contextMenu.style.display='none';
                }
                if(state.isDrawing  && event.button==MouseButtons.LEFT){
                    if(state.drawType==DrawTypes.LINE){
                        let cp=$canvas.point2d(event.offsetX,event.offsetY);
                        $canvas.drawLine(dc2d,state.beginPoint,cp);
                    }
                    if(state.drawType==DrawTypes.FREE_LINE){
                        dc2d.lineTo(event.offsetX,event.offsetY);
                        dc2d.stroke();
                    }
                    if(state.drawType==DrawTypes.RECT){
                        let pointBegin=$canvas.point2d(state.beginPoint.x,state.beginPoint.y);
                        let pointEnd=$canvas.point2d(event.offsetX,event.offsetY);
                        $canvas.drawRect(dc2d,pointBegin,pointEnd);
                    }
                    if(state.drawType==DrawTypes.FILL_RECT){
                        let pointBegin=$canvas.point2d(state.beginPoint.x,state.beginPoint.y);
                        let pointEnd=$canvas.point2d(event.offsetX,event.offsetY);
                        $canvas.drawFillRect(dc2d,pointBegin,pointEnd);
                    }
                    if(state.drawType==DrawTypes.CIRCLE){
                        let pointBegin=$canvas.point2d(state.beginPoint.x,state.beginPoint.y);
                        let pointEnd=$canvas.point2d(event.offsetX,event.offsetY);
                        let dis=$canvas.distancePoint(pointBegin,pointEnd);
                        $canvas.drawCircle(dc2d,pointBegin,dis);
                    }
                    if(state.drawType==DrawTypes.FILL_CIRCLE){
                        let pointBegin=$canvas.point2d(state.beginPoint.x,state.beginPoint.y);
                        let pointEnd=$canvas.point2d(event.offsetX,event.offsetY);
                        let dis=$canvas.distancePoint(pointBegin,pointEnd);
                        $canvas.drawFillCircle(dc2d,pointBegin,dis);
                    }
                    if(state.drawType==DrawTypes.ELLIPSE){
                        let pointBegin=$canvas.point2d(state.beginPoint.x,state.beginPoint.y);
                        let pointEnd=$canvas.point2d(event.offsetX,event.offsetY);
                        $canvas.drawEllipse(dc2d,pointBegin,pointEnd);
                    }
                    if(state.drawType==DrawTypes.FILL_ELLIPSE){
                        let pointBegin=$canvas.point2d(state.beginPoint.x,state.beginPoint.y);
                        let pointEnd=$canvas.point2d(event.offsetX,event.offsetY);
                        $canvas.drawFillEllipse(dc2d,pointBegin,pointEnd);
                    }
                    if(state.drawType==DrawTypes.ROUND_RECT){
                        let pointBegin=$canvas.point2d(state.beginPoint.x,state.beginPoint.y);
                        let pointEnd=$canvas.point2d(event.offsetX,event.offsetY);
                        $canvas.drawRoundRect(dc2d,pointBegin,pointEnd);
                    }
                    if(state.drawType==DrawTypes.FILL_ROUND_RECT){
                        let pointBegin=$canvas.point2d(state.beginPoint.x,state.beginPoint.y);
                        let pointEnd=$canvas.point2d(event.offsetX,event.offsetY);
                        $canvas.drawFillRoundRect(dc2d,pointBegin,pointEnd);
                    }
                }
                state.isDrawing=false;
            }
        }

        dom.addEventListener(EventTypes.MOUSE_DOWN, event => {
            eventHandler(EventTypes.MOUSE_DOWN,event);
        });

        dom.addEventListener(EventTypes.MOUSE_UP, event => {
            eventHandler(EventTypes.MOUSE_UP,event);
        });

        dom.addEventListener(EventTypes.MOUSE_MOVE, event => {
            eventHandler(EventTypes.MOUSE_MOVE,event);
        });

        dom.addEventListener(EventTypes.MOUSE_LEAVE, event => {
            eventHandler(EventTypes.MOUSE_LEAVE,event);
        });
        dom.addEventListener(EventTypes.CLICK, event => {
            eventHandler(EventTypes.CLICK,event);
        });
        dom.addEventListener(EventTypes.DBL_CLICK, event => {
            eventHandler(EventTypes.DBL_CLICK,event);
        });
        dom.addEventListener(EventTypes.KEY_DOWN, event => {
            eventHandler(EventTypes.KEY_DOWN,event);
        });
        dom.addEventListener(EventTypes.KEY_UP, event => {
            eventHandler(EventTypes.KEY_UP,event);
        });
        dom.addEventListener(EventTypes.CONTEXT_MENU, event => {
            event.preventDefault();
            eventHandler(EventTypes.CONTEXT_MENU,event);
        });
    },
    initWaterMark(id,config){
        let defaultConfig={
            velSpace:30,
            horSpace:40,
            text:'water mark',
            fontSize:'24',
            fontFamily:'黑体',
            fontColor:'silver',
            opacity:0.3,
            rotate:-30,
        };
        $obj.copyObj2(config,defaultConfig);

        defaultConfig.rotate=$canvas.angle2Radian(defaultConfig.rotate);

        let mainDom=document.getElementById(id);
        let dom=document.createElement('canvas');
        dom.id=id+'_watermark';
        dom.style.zIndex='2000'+mainDom.style.zIndex;
        dom.style.position='absolute';
        mainDom.parentNode.appendChild(dom);

        let listenHandler=function() {
            dom.style.width = mainDom.clientWidth + 'px';
            dom.style.height = mainDom.clientHeight + 'px';
            dom.style.left = mainDom.offsetLeft + 'px';
            dom.style.top = mainDom.offsetTop + 'px';
            dom.style.display = 'block';
            dom.style.pointerEvents = 'none';
            if (!dom.getContext) {
                console.error('not support!');
                return;
            }
            let dc2d = dom.getContext('2d');

            dc2d.globalAlpha = defaultConfig.opacity;
            dc2d.fillStyle = defaultConfig.fontColor;
            dc2d.font = defaultConfig.fontSize + 'px ' + defaultConfig.fontFamily;

            let width = mainDom.clientWidth;
            let height = mainDom.clientHeight;
            dc2d.clearRect(-1,-1,width+2,height+2);

            let curHei = 0 - parseInt(defaultConfig.velSpace);
            while (curHei < height) {
                let curWid = 0 - parseInt(defaultConfig.horSpace);
                while (curWid < width) {
                    dc2d.save();

                    dc2d.translate(curWid, curHei);
                    dc2d.rotate(defaultConfig.rotate);
                    let mesure = dc2d.measureText(defaultConfig.text);
                    dc2d.fillText(defaultConfig.text, 0 - (mesure.width) / 2, 0);

                    dc2d.restore();

                    curWid += parseInt(defaultConfig.fontSize);
                    curWid += parseInt(defaultConfig.velSpace);
                }
                curHei += parseInt(defaultConfig.fontSize);
                curHei += parseInt(defaultConfig.velSpace);
            }
        }
        listenHandler();

         mainDom.addEventListener('resize',function(event){
            listenHandler();
         });

         mainDom.addEventListener('scroll',function (event){
            listenHandler();
         });

         mainDom.addEventListener('load',function (event){
             listenHandler();
         });

         mainDom.addEventListener('copy',function (event){
             listenHandler();
         })

        mainDom.addEventListener('cut',function (event){
            listenHandler();
        })

        mainDom.addEventListener('wheel',function (event){
            listenHandler();
        })
    },
}


