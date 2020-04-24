package ru.mai;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Beaver
 */
public class NameFilter implements FileFilter {

    private String mask;

    public NameFilter(String mask) {
        this.mask = mask;
    }

    public boolean accept(File file) {
        return file.getName().indexOf(mask) != -1;
    }
}
