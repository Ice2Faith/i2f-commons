package i2f.commons.core.utils.file.impl;


import i2f.commons.core.utils.file.IFileFilter;

import java.io.File;

public class DirOnlyFilter implements IFileFilter {

    @Override
    public boolean save(File item) {
        return item.isDirectory();
    }
}
