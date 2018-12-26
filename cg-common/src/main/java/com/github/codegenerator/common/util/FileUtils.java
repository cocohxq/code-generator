package com.github.codegenerator.common.util;

import com.github.codegenerator.common.in.model.GenerateInfo;
import com.github.codegenerator.common.in.model.StringTemplateLoader;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {


    /**
     * 生成类文件
     *
     * @param info
     */
    public static void generateFile(GenerateInfo info) {
        Configuration configration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configration.setDefaultEncoding("UTF-8");
        configration.setTemplateExceptionHandler((TemplateException te, Environment env, Writer out) -> {
            ContextContainer.getContext().error(env.getConfiguration().getSharedVariable("templateFileName").toString(),"中${",te.getBlamedExpressionString(),"}无法识别");
        });
        File codeDir = new File(info.getCodepath());
        if(codeDir.exists()){
            deleteDir(info.getCodepath(),true);
        }
        //遍历选中的模板
        info.getSelectedTmps().values().stream().forEach(template -> {
            try {
                StringTemplateLoader loader = new StringTemplateLoader(template.getTemplateContent());
                configration.setTemplateLoader(loader);
                configration.setSharedVariable("templateFileName",template.getTemplateFileName());
                Template tmp = configration.getTemplate("");
                //遍历选中的表
                for(Map<String,Object> tableContentMap : template.getTableContentList()){
                    tableContentMap.put("commonValueStack",info.getCommonValueStack());
                    String targetFilePath = (String)tableContentMap.get("targetFilePath");
                    String targetFileDir = targetFilePath.substring(0,targetFilePath.lastIndexOf(File.separator));
                    File dir = new File(targetFileDir);
                    dir.mkdirs();
                    Writer writer = null;
                    try {
                        File targetFile = new File(targetFilePath);
                        if(targetFile.exists()){
                            FileUtils.deleteFile(targetFile.getPath());
                        }
                        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
                        tmp.process(tableContentMap, writer);
                        writer.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        try {
                            if(null != writer) {
                                writer.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    /**
     * 生成模板文件
     * @param
     */
    public static Integer generateTmpTreeFiles(String sourceTmpTreeName,String targetTmpTreeName, List<String> tmps){
        String targetTreePath = FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR,targetTmpTreeName);
        String sourceTreePath = FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR,sourceTmpTreeName);
        //拼接上全路径
        List<String> actualPathList = new ArrayList<>(tmps.size());
        tmps.stream().forEach(l -> actualPathList.add(FileUtils.concatPath(sourceTreePath,l)));

        FileUtils.copyDirWithFilter(sourceTreePath,targetTreePath,actualPathList);
        return 1;

    }

    /**
     * 加载文件
     * @param fileModulePath
     * @return
     */
    public static String loadFile(String treeName,String fileModulePath,int type){
        String filePath = "";
        if(type == 0){
            filePath = FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR,treeName,fileModulePath);
        }else{
            filePath = FileUtils.concatPath(ContextContainer.USER_CODE_DIR,treeName,fileModulePath);
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(filePath)));
            return br.lines().collect(Collectors.joining("\n"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if(null != br){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新已有文件
     * @param tmpTreeName
     * @param fileModulePath
     * @param content
     * @return
     */
    public static Integer updateFile(String tmpTreeName,String fileModulePath,String content){
        File file = new File(FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR,tmpTreeName,fileModulePath));
        if(!file.exists()){
            throw new RuntimeException("需更新的文件不存在");
        }
        deleteFile(file.getPath());
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.flush();
        } catch (IOException e) {
            if(fileWriter != null){
                try {
                    fileWriter.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    /**
     * 重命名文件
     * @param tmpTreeName
     * @param
     * @param
     * @return
     */
    public static Integer modifyFileName(String tmpTreeName,String oldFileModulePath,String newFileModulePath){
        try {
            File file = new File(FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR,tmpTreeName,oldFileModulePath));
            if(!file.exists()){
                throw new RuntimeException("需更新的文件不存在");
            }

            file.renameTo(new File(FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR, tmpTreeName, newFileModulePath)));
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 生成zip文件
     * @param
     */
    public static void generateZip(List<String> sourcePathList, String targetPath, String zipName){
        File targetFile = new File(FileUtils.concatPath(targetPath,zipName));
        if(targetFile.exists()){
            deleteFile(targetFile.getPath());
        }
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(targetFile)));
            for(String filePath : sourcePathList){
                String fileModulePath = filePath.substring(filePath.indexOf("/module/"));//只保留有效的路径
                ZipEntry entry = new ZipEntry(fileModulePath);
                zos.putNextEntry(entry);
                File file = new File(filePath);
                byte[] buffer = new byte[1024*10];
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file),buffer.length);
                int count = 0;
                while((count = bis.read(buffer,0,buffer.length)) > 0){
                    zos.write(buffer,0,count);
                }
                zos.closeEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("生成zip文件异常");
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
            }
        }


    }


    public static byte[] readFile2ByteArray(String filePath){
        try {
            File file = new File(filePath);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            byte[] arr = new byte[(int)file.length()];

            int offset = 0;
            int count = 0;
            while(offset < arr.length && (count = bis.read(arr,offset,arr.length-offset)) != -1){
                offset+=count;
            }


            if(arr.length != offset){
                throw new IOException("文件转成字节异常");
            }
            return arr;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
        }

    }



    public static List<String> loadDirAllFilePathList(String dirPath){
        List<String> pathArr = new ArrayList<>();
        loadDirAllFilePathList(dirPath,pathArr);
        return pathArr;
    }

    private static void loadDirAllFilePathList(String dirPath,List<String> pathArr){
        File dir = new File(dirPath);
        for(File childFile : dir.listFiles()){
            if (childFile.isHidden()) {
                continue;
            }
            if(childFile.isDirectory()){
                loadDirAllFilePathList(childFile.getPath(),pathArr);
            }else{
                pathArr.add(childFile.getPath());
            }
        }
    }


    /**
     * 加载目录下的第一级文件列表
     * @param dirPath
     * @return
     */
    public static List<String> loadDirDirectFileList(String dirPath){
        File dir = new File(dirPath);
        if(!dir.isDirectory()){
            return new ArrayList<>(0);
        }
        //文件倒序排列
        return Arrays.stream(dir.listFiles()).sorted((s1,s2) -> s1.lastModified() > s2.lastModified()?-1:1).filter(l -> !l.isHidden()).map(l -> l.getName()).collect(Collectors.toList());
    }

    public static void copyDirWithSelf(String sourcePath, String targetPath) {
        File sourceDir = new File(sourcePath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new RuntimeException("需要copy的目录为空，name:"+sourceDir);
        }
        targetPath = FileUtils.concatPath(targetPath,sourceDir.getName());
        copyDirWithFilter(sourcePath,targetPath,null);
    }

    /**
     * copy源目录下的文件及文件夹到目标目录下
     * @param sourcePath
     * @param targetPath
     * @param fileModulePathList  需要copy的文件 为空默认全部copy
     */
    public static void copyDirWithFilter(String sourcePath, String targetPath, List<String> fileModulePathList) {
        File sourceDir = new File(sourcePath);
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new RuntimeException("需要copy的目录为空");
        }
        File targetDir = new File(targetPath);
        if (targetDir.exists()) {
            deleteDir(targetPath,true);
        }
        targetDir.mkdirs();

        for (File file : sourceDir.listFiles()) {
            if (file.isHidden()) {
                continue;
            }
            //路径完全匹配上才需要copy，原则上只需要copy文件，不需要文件夹
            if(!needCopy(file.getPath(),fileModulePathList)){
                continue;
            }
            if (file.isDirectory()) {
                copyDirWithFilter(FileUtils.concatPath(sourcePath,file.getName()), FileUtils.concatPath(targetPath,file.getName()),fileModulePathList);
            } else {
                BufferedWriter bw = null;
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(new File(FileUtils.concatPath(sourcePath,file.getName()))));
                    bw = new BufferedWriter(new FileWriter(new File(FileUtils.concatPath(targetPath,file.getName()))));
                    bw.write(br.lines().collect(Collectors.joining("\n")));
                    bw.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (null != br) {
                            br.close();
                        }
                        if (null != bw) {
                            bw.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static boolean needCopy(String filePath,List<String> fileModulePathList){
        if(null == fileModulePathList){
            return true;
        }
        for(String s : fileModulePathList){
            if(s.contains(filePath)){
                return true;
            }
        }
        return false;
    }

    /**
     * 新增模板文件
     * @param tmpTreeName
     * @param fileModulePath
     * @return
     */
    public static Integer addFile(String tmpTreeName,String fileModulePath,Integer fileType){
        try {
            File file = new File(FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR,tmpTreeName,fileModulePath));
            if(file.exists()){
                return 0;
            }
            if(fileType == 0) {
                file.createNewFile();
            }else{
                file.mkdir();
            }
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 根据文件路径获取文件名
     * @param fileModulePath
     * @return
     */
    public static String getFileNameByPath(String fileModulePath){
        int index = fileModulePath.lastIndexOf(File.separator);
        return fileModulePath.substring(index+1);
    }

    /**
     * 删除文件夹
     * @param path
     * @param includeSelf
     */
    public static void deleteDir(String path,boolean includeSelf){
        File file = new File(path);
        if(!file.exists() && !file.isDirectory()){
            return;
        }

        for(File childFile : file.listFiles()){
            if(childFile.isDirectory()){
                deleteDir(childFile.getPath(),true);
            }else{
                childFile.delete();
            }
        }
        //如果包含自身，也删除掉
        if(includeSelf){
            file.delete();
        }
    }

    /**
     * 删除文件
     * @param path
     */
    public static void deleteFile(String path){
        File file = new File(path);
        if(!file.exists()){
            return;
        }
        if(file.isDirectory()){
            deleteDir(path,true);
        }else{
            file.delete();
        }

    }


    public static String concatPath(String... paths){
        if(paths.length == 0){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        int index = 0;
        for(String path : paths){
            //去除每个path的首尾的路径分隔符  首path不去头/
            if(path.startsWith(File.separator) && index != 0){
                path = path.substring(1);
            }
            //尾path不去末尾/
            if(path.endsWith(File.separator) && index != paths.length-1){
                path = path.substring(0,path.length()-1);
            }
            //添加分隔符
            if(index != 0) {
                sb.append(File.separator);
            }
            sb.append(path);
            index++;
        }
        return sb.toString();
    }

    /**
     *
     * @param jarPath
     * @param targetDirPath
     * @param sourcePath
     */
    public static void copyDirFromJar(String jarPath,String sourcePath,String targetDirPath){
        try {
            File targetDir = new File(targetDirPath);
            if(targetDir.exists()){
                FileUtils.deleteFile(targetDirPath);
            }
            targetDir.mkdirs();

            JarFile jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> enums = jarFile.entries();
            JarEntry jarEntry = null;
            while(enums.hasMoreElements()){
                jarEntry = enums.nextElement();
                //jarEntry.getName() 返回的是带路径的名称，eg：BOOT-INF/classes/templates/default_tmps/module/src/main/java/${groupId}/entity/${tableCamelName}Entity.java.ftl
                //只处理BOOT-INF/classes/templates/default_tmps/ 以下的文件和目录
                if(!jarEntry.getName().startsWith(sourcePath) || jarEntry.getName().equals(sourcePath)){
                    continue;
                }
                String targetPath = FileUtils.concatPath(targetDirPath,jarEntry.getName().replace(sourcePath,""));
                if(jarEntry.isDirectory()){
                    //截取源目录，再拼接目标目录，创建文件夹
                    new File(targetPath).mkdir();
                    continue;
                }
                //从jar中copy文件
                copyFileFromInputStream(jarFile.getInputStream(jarEntry),targetPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static void copyFileFromInputStream(InputStream is,String targetPath){
        InputStreamReader isr = null;
        OutputStreamWriter osw = null;
        try {
            isr = new InputStreamReader(new BufferedInputStream(is));
            osw = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(new File(targetPath))));
            char[] buffer = new char[1024];
            int count = 0;
            while((count = isr.read(buffer,0,buffer.length))>0){
                osw.write(buffer,0,count);
            }
            osw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != osw){
                    osw.close();
                }
                if(null != isr){
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
