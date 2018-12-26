package com.github.codegenerator.common.util;

import com.github.codegenerator.common.in.model.TreeNode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TreeUtils {


    public static final String specialDirName = ",module,src,main,java,${groupId},resources,webapp,WEB-INF,";
    //树菜单级别  0：可以在其下添加目录和文件 1：可以在其下添加目录和文件、删除、重命名  3：可以重命名、删除
    private static final String MENU_C = "0";//对应specialDirName的文件夹
    private static final String MENU_CMD = "1";//普通文件夹
    private static final String MENU_MD = "2";//普通文件

    /**
     * 从文件中读取tree
     *
     * @param
     * @param type 0:模板树   1：代码树
     * @return
     */
    public static TreeNode loadTree(String treeName, int type) {
        String treePath = "";
        if (type == 0) {
            treePath = FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR,treeName,ContextContainer.MODULE_PATH_ROOT);
        } else {
            treePath = FileUtils.concatPath(ContextContainer.USER_CODE_DIR,treeName,ContextContainer.MODULE_PATH_ROOT);
        }
        String actualPathPreffix = treePath.replace(ContextContainer.MODULE_PATH_ROOT,"");
        File fileDir = new File(treePath);

        //以module为root，组件模板树
        Map<String, String> packageContent = new HashMap<>();
        packageContent.put("type", "package");
        packageContent.put("memuLevel", MENU_C);
        packageContent.put("modulePath",fileDir.getPath().replace(treePath, ""));
        TreeNode treeRoot = new TreeNode(0l, fileDir.getName(), "open", new ArrayList<>(), packageContent, true);
        loadChildren(treeRoot, fileDir, 0, actualPathPreffix);
        return treeRoot;
    }

    private static void loadChildren(TreeNode curNode, File file, long id, String actualPathPreffix) {
        for (File childFile : file.listFiles()) {
            if (childFile.isHidden()) {
                continue;
            }
            //生成当前子目录节点
            TreeNode childNode = null;
            Map<String, String> nodeAttributeMap = new HashMap<>();
            nodeAttributeMap.put("modulePath", childFile.getPath().replace(actualPathPreffix, ""));//path以module做起点
            if (childFile.isDirectory()) {
                if (specialDirName.indexOf("," + childFile.getName() + ",") > -1) {
                    nodeAttributeMap.put("memuLevel", MENU_C);
                }else{
                    nodeAttributeMap.put("memuLevel", MENU_CMD);
                }
                nodeAttributeMap.put("type", "package");
                childNode = new TreeNode(++id, childFile.getName(), "open", new ArrayList<>(), nodeAttributeMap, true);
                curNode.getChildren().add(childNode);
                loadChildren(childNode, childFile, id, actualPathPreffix);
            } else {
                nodeAttributeMap.put("type", "file");
                nodeAttributeMap.put("memuLevel", MENU_MD);
                childNode = new TreeNode(++id, childFile.getName(), null, null, nodeAttributeMap, true);
                curNode.getChildren().add(childNode);
            }
        }
        Collections.sort(curNode.getChildren(),(b,f)-> SortUtils.sort(b.getText(),f.getText(),SortUtils.SORT_ASC));//按名称从小到大排序
    }




}
