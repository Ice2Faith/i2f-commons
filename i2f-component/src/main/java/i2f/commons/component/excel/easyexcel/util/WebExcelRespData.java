package i2f.commons.component.excel.easyexcel.util;

/**
 * @author ltb
 * @date 2021/10/19
 */
public class WebExcelRespData {
    private String fileName;
    private String checkUrl;
    private String downloadUrl;

    public WebExcelRespData(String fileName, String checkUrl, String downloadUrl) {
        this.fileName = fileName;
        this.checkUrl = checkUrl;
        this.downloadUrl = downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCheckUrl() {
        return checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "WebExcelRespData{" +
                "fileName='" + fileName + '\'' +
                ", checkUrl='" + checkUrl + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                '}';
    }
}
