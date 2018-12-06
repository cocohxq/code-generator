package com.github.codegenerator.spi.db.common.util;

public class BuildUtils {


    /**
     * 将数据库的_分隔的命名改成java的驼峰命名,首字母也大写
     *
     * @param sourceName
     * @return
     */
    public static String conver2CameltName(String sourceName) {
        if (null == sourceName || "".equals(sourceName.trim())) {
            throw new RuntimeException("tableName: " + sourceName + "is valid!");
        }

        sourceName = sourceName.toLowerCase();
        if (sourceName.indexOf("_") < 0) {
            return sourceName;
        }

        StringBuffer sb = new StringBuffer();
        String[] names = sourceName.toLowerCase().split("_");
        for (int i = 0, j = names.length; i < j; i++) {
            if(i == 0){
                sb.append(sourceName.substring(0, 1).toLowerCase());
            }else{
                sb.append(names[i].substring(0, 1).toUpperCase());
            }
            sb.append(names[i].substring(1));
        }
        return sb.toString();
    }
}
