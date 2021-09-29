package i2f.commons.core.utils.file.core;

import i2f.commons.core.utils.str.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class FileSpecies {
    public static final String PICTURES="pictures";
    public static final String VIDEOS="videos";
    public static final String AUDIOS="audios";
    public static final String DOCUMENTS="documents";
    public static final String EXECUABLES="execuables";
    public static final String COMPRESSES="compresses";
    public static final String LIBDLLS="libdlls";
    public static final String UNKOWNES="unkownes";
    public static final String OTHERS="others";

    public static final String[] picturesArr = { ".jpg", ".jpeg", ".png", ".gif", ".tif",
            ".bmp", ".ico", ".raw", ".exif", ".webp",
            ".wmf", ".svg", ".psd"};
    public static final String[] videosArr = { ".mp4", ".mkv", ".rmvb", ".flv", ".avi",
            ".mov", ".wmv", ".3gp", ".yuv"};
    public static final String[] audiosArr ={".mp3", ".ogg", ".wav", ".aac", ".pcm", ".flac",
            ".wma", ".vqf", ".amr", ".ape"};
    public static final String[] documentsArr = { ".txt", ".log", ".doc", ".docx", ".xls",
            ".xlsx", ".ppt", ".pptx", ".pdf", ".html",
            ".htm", ".xml", ".json", ".php", ".asp",
            ".config", ".c", ".h", ".cpp", ".hpp",
            ".asm",  ".java", ".cs", ".ini", ".css",
            ".js",".go",".jsp"};
    public static final String[] execuablesArr = { ".exe", ".msc", ".elf", ".apk", ".bat",
            ".jar", ".py", ".sh", ".class"};
    public static final String[] compressesArr = { ".zip", ".rar", ".gz", ".tar", ".7z",
            ".iso",  ".bin", ".zipx", ".tgz", ".xz",
            ".war", ".img", ".wim", ".udf"};
    public static final String[] libdllsArr = { ".lib", ".dll", ".sys", ".so", ".a"};

    private static Map<String,String[]> species=new HashMap<>();
    static {
        species.put(FileSpecies.PICTURES, FileSpecies.picturesArr);
        species.put(FileSpecies.VIDEOS, FileSpecies.videosArr);
        species.put(FileSpecies.AUDIOS, FileSpecies.audiosArr);
        species.put(FileSpecies.DOCUMENTS, FileSpecies.documentsArr);
        species.put(FileSpecies.EXECUABLES, FileSpecies.execuablesArr);
        species.put(FileSpecies.COMPRESSES, FileSpecies.compressesArr);
        species.put(FileSpecies.LIBDLLS, FileSpecies.libdllsArr);
    }

    public static String getSpecieName(String fileName){
        String cksuff= StringUtil.getExtension(fileName);
        if("".equals(cksuff)){
            return UNKOWNES;
        }
        for(String spe : species.keySet())
        {
            String[] suffixes=species.get(spe);
            for(String suff : suffixes)
            {
                if(suff.equalsIgnoreCase(cksuff)){
                    return spe;
                }
            }
        }
        return OTHERS;
    }
}
