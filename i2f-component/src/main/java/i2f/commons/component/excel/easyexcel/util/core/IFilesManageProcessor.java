package i2f.commons.component.excel.easyexcel.util.core;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author ltb
 * @date 2021/9/6
 */
public interface IFilesManageProcessor {
    String saveFileTo(String path,MultipartFile file, HttpServletRequest request);
    File getFile(String fileName, HttpServletRequest request);
}
