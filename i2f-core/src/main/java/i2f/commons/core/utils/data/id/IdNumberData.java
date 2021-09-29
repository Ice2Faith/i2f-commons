package i2f.commons.core.utils.data.id;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class IdNumberData {
    public static SimpleDateFormat dateFmt=new SimpleDateFormat("yyyyMMdd");
    public String idNumber;
    public String region;
    public String regionDesc;
    public String date;
    public Date dateDesc;
    public String year;
    public String month;
    public String day;
    public boolean isLeap=false;
    public String policy;
    public String sex;
    public String sexDesc;
    public String checkSum;
    public boolean isLegalCheckSum=false;
    public boolean isLegalId=false;
    public String illegalReason;
}
