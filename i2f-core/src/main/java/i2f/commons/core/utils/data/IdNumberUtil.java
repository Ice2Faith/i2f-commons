package i2f.commons.core.utils.data;


import i2f.commons.core.utils.data.id.IdNumberData;
import i2f.commons.core.utils.data.id.RegionMap;

public class IdNumberUtil {
    public static boolean isLegalIdNumber(String idNumber){
        IdNumberData data=parse(idNumber);
        return data.isLegalId && data.isLegalCheckSum;
    }
    public static IdNumberData parse(String idNumber){
        IdNumberData ret=new IdNumberData();
        ret.idNumber=idNumber;
        if(ret.idNumber==null){
            ret.illegalReason="null id number.";
            ret.isLegalId=false;
            return ret;
        }
        ret.idNumber=ret.idNumber.trim().toLowerCase();
        if(!ret.idNumber.matches("[0-9]{17}[0-9|x]")){
            ret.illegalReason="length not equals 18 or contains other characters.";
            ret.isLegalId=false;
            return ret;
        }

        ret.region =ret.idNumber.substring(0,6);
        ret.date =ret.idNumber.substring(6,14);
        ret.policy=ret.idNumber.substring(14,16);
        ret.sex=ret.idNumber.substring(16,17);
        ret.checkSum=ret.idNumber.substring(17,18);

        ret.year=ret.date.substring(0,4);
        ret.month=ret.date.substring(4,6);
        ret.day=ret.date.substring(6,8);

        ret.sexDesc=Integer.parseInt(ret.sex)%2==0?"女":"男";
        ret.regionDesc= RegionMap.decode(ret.region);

        int year=Integer.parseInt(ret.year);
        int month=Integer.parseInt(ret.month);
        int day=Integer.parseInt(ret.day);

        ret.isLeap=DateUtil.isLeapYear(year);
        if(!DateUtil.isLegalDate(year,month,day)){
            ret.illegalReason="date time not legal";
            ret.isLegalId=false;
            return ret;
        }

        ret.isLegalId=true;

        try{
            ret.dateDesc=IdNumberData.dateFmt.parse(ret.date);
        }catch(Exception e){

        }

        char checkSum=getIdNumberCheckSum(ret.idNumber);
        ret.isLegalCheckSum=ret.checkSum.equalsIgnoreCase(""+checkSum);

        return ret;
    }

    public static final int[] WEIGHT_TABLE={7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
    public static final char[] SUM_TABLE={'1','0','X','9','8','7','6','5','4','3','2'};

    /**
     * 计算身份证最后一位
     * 因此你必须传入大于等于17位的身份证
     * 身份证号码一共18位
     * @param idNumber
     * @return
     */
    public static char getIdNumberCheckSum(String idNumber){
        int sum=0;
        for(int i=0;i< WEIGHT_TABLE.length;i+=1){
            sum+=WEIGHT_TABLE[i]*Integer.parseInt(idNumber.charAt(i)+"");
        }
        return SUM_TABLE[sum%11];
    }
}
