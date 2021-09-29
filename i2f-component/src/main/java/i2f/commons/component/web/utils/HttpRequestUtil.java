package i2f.commons.component.web.utils;

import i2f.commons.core.utils.file.FileUtil;
import i2f.commons.core.utils.safe.CheckUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;

public class HttpRequestUtil {
    public static File getContextPath(HttpServletRequest request, String relativePath){
        HttpSession session=request.getSession();
        return getContextPath(session,relativePath);
    }
    public static File getContextPath(HttpSession session, String relativePath){
        String rootPath=session.getServletContext().getRealPath("");
        if(CheckUtil.isEmptyStr(relativePath,true)){
            return new File(rootPath);
        }
        return FileUtil.getFile(rootPath,relativePath);
    }
}
