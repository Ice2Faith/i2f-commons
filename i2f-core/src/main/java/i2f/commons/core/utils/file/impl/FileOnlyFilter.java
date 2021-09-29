package i2f.commons.core.utils.file.impl;


import i2f.commons.core.utils.file.IFileFilter;

import java.io.File;

public class FileOnlyFilter implements IFileFilter {

    @Override
    public boolean save(File item) {
        return item.isFile();
    }
}
