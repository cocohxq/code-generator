package com.github.codegenerator.main.in;

import com.alibaba.fastjson.JSONObject;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.TreeNode;
import com.github.codegenerator.common.spi.initializer.Initializer;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.DataUtil;
import com.github.codegenerator.common.util.FileUtils;
import com.github.codegenerator.common.util.TreeUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 配置入口
 */
@Controller
public class ConfigController {

    //step1 进入选择页
    @GetMapping(value = {"/init", "/"})
    public String init(Model model, HttpServletRequest request) {
        //存在操作
        String operation = request.getParameter("op");
        if (!StringUtils.isEmpty(operation)) {
            model.addAttribute("op", operation);
            //加载相应数据
            String selectedConfig = request.getParameter("sc");
            if (!StringUtils.isEmpty(selectedConfig)) {
                request.setAttribute("selectedConfig", selectedConfig);
                Config config = DataUtil.getData("cb", selectedConfig, Config.class);
                //如果是复制新增，这里把config名称剔除,让页面重新输入
                if (!StringUtils.isEmpty(request.getParameter("cp"))) {
                    config.setConfigName("");
                }
                model.addAttribute("dbConfig", config);
            } else {
                model.addAttribute("dbConfig", new Config());
            }
        } else {
            List<String> configList = DataUtil.getDataNameList();
            configList.add(0, "请选择");
            if (null != configList) {
                model.addAttribute("dbConfigList", configList);
            }
        }

        model.addAttribute("view", ContextContainer.getViewerInfo());
        return "config";
    }

    /**
     * 每一步的操作都是对config对象的完善
     *
     * @param config
     * @return
     */
    @PostMapping("/initData")
    @ResponseBody
    public Map<String, Object> initData(@RequestBody Config config) {
//        ContextContainer.getContext().getConfig().setOperation(null);
//        BeanUtils.copyProperties(config,ContextContainer.getContext().getConfig(),getNullPropertyNames(config));
        ContextContainer.getContext().setConfig(config);
        for (Initializer initializer : ContextContainer.getStepInitializer(config.getStepType())) {
            initializer.initialize(ContextContainer.getContext());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("error", ContextContainer.getContext().getErrorMsgs());
        result.put("stepResult", ContextContainer.getContext().getStepInitResult());
        return result;
    }

    //进入tip页
    @GetMapping("/tip")
    public String init() {
        return "tip";
    }


    @PostMapping("/loadFile")
    @ResponseBody
    public Object loadFile(@RequestBody Map<String, String> param) {
        String fileModulePath = param.get("modulePath");
        String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
        Integer fileType = Integer.parseInt(param.get("fileType"));
        //代码树的根节点是session
        if (fileType.intValue() == 1) {
            tmpTreeName = ContextContainer.getContext().getSessionId();
        }
        JSONObject json = new JSONObject();
        json.put("fileContent", FileUtils.loadFile(tmpTreeName, fileModulePath, fileType));
        return json;
    }

    /**
     * 增加模板
     *
     * @param param
     * @return
     */
    @PostMapping("/addPath")
    @ResponseBody
    public Integer addPath(@RequestBody Map<String, String> param) {
        try {
            String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
            String tmpModulePath = param.get("tmpModulePath");
            Integer fileType = Integer.parseInt(param.get("fileType"));
            FileUtils.addFile(tmpTreeName, tmpModulePath, fileType);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 增加模板
     *
     * @param param
     * @return
     */
    @PostMapping("/movePath")
    @ResponseBody
    public Integer movePath(@RequestBody Map<String, String> param) {
        try {
            String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
            String sourceTmpModulePath = param.get("sourceTmpModulePath");
            String targetTmpModulePath = param.get("targetTmpModulePath");
            FileUtils.move(getActualPath(tmpTreeName, sourceTmpModulePath), getActualPath(tmpTreeName, targetTmpModulePath));
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 增加模板
     *
     * @param param
     * @return
     */
    @PostMapping("/copyPath")
    @ResponseBody
    public Integer copyPath(@RequestBody Map<String, String> param) {
        try {
            String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
            String sourceTmpModulePath = param.get("sourceTmpModulePath");
            String targetTmpModulePath = param.get("targetTmpModulePath");
            FileUtils.copy(getActualPath(tmpTreeName, sourceTmpModulePath), getActualPath(tmpTreeName, targetTmpModulePath));
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 修改模板名称
     *
     * @param
     * @return
     */
    @PostMapping("/editTemplateName")
    @ResponseBody
    public Integer editTemplateName(@RequestBody Map<String, Object> param) {
        String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
        String tmpModulePath = (String) param.get("tmpModulePath");
        String newTmpModulePath = (String) param.get("newTmpModulePath");
        if (null == tmpModulePath || null == tmpTreeName || null == newTmpModulePath) {
            return 0;
        }
        FileUtils.modifyFileName(getActualPath(tmpTreeName, tmpModulePath), getActualPath(tmpTreeName, newTmpModulePath));
        return 1;
    }

    /**
     * 下载zip文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    @GetMapping("/downloadZip")
    public ResponseEntity<byte[]> downloadZip(String fileName) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        String filePath = FileUtils.concatPath(ContextContainer.getContext().getGenerateInfo().getCodepath(), fileName);
        ResponseEntity entity = new ResponseEntity<byte[]>(FileUtils.readFile2ByteArray(filePath),
                headers, HttpStatus.CREATED);
        FileUtils.delete(filePath);//清除zip文件
        return entity;
    }


    @PostMapping("/refreshUserTmpTreeList")
    @ResponseBody
    public Object refreshUserTmpTreeList() {
        List<String> list = FileUtils.loadDirDirectFileList(ContextContainer.USER_TMPTREE_DIR);
        if (null == list) {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 加载模板树
     *
     * @param
     * @return
     */
    @PostMapping("/loadTmpTree")
    @ResponseBody
    public Object loadTmpTree(@RequestBody Map<String, String> param) {
        String tmpTreeName = param.get("tmpTreeName");
        TreeNode treeNode = TreeUtils.loadTree(tmpTreeName, 0);
        ContextContainer.getContext().getGenerateInfo().setSelectedTmpTreeName(tmpTreeName);
        return new TreeNode[]{treeNode};
    }

    /**
     * 保存用户模板树
     *
     * @param
     * @return
     */
    @PostMapping("/saveUserTmpTree")
    @ResponseBody
    public Object saveUserTmpTree(@RequestBody Map<String, Object> param) {
        String userTmpTreeName = (String) param.get("userTmpTreeName");
        String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
        String templateContent = (String) param.get("templateContent");
        String tmpModulePath = (String) param.get("tmpModulePath");
        List<String> selectTmps = (List<String>) param.get("tmps");
        JSONObject jsonObject = new JSONObject();
        //如果提交到新模板树，先创建一个
        if (!userTmpTreeName.equals(tmpTreeName)) {
            if (new File(FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR, userTmpTreeName)).exists()) {
                jsonObject.put("msg", "不能提交到已存在的其它模板树");
                return jsonObject;
            }
            generateTmpTreeFiles(tmpTreeName, userTmpTreeName, selectTmps);
        }

        //保存之后再更新
        if (null != tmpModulePath && null != templateContent && !templateContent.equals("")) {
            FileUtils.updateFile(userTmpTreeName, tmpModulePath, templateContent);
        }
        jsonObject.put("msg", "提交成功");
        return jsonObject;
    }

    @PostMapping("/commit")
    @ResponseBody
    public Integer commit(@RequestBody Map<String, Object> param) {
        String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
        String templateContent = (String) param.get("templateContent");
        String tmpModulePath = (String) param.get("tmpModulePath");
        if (null != tmpModulePath && null != templateContent && !templateContent.equals("")) {
            return FileUtils.updateFile(tmpTreeName, tmpModulePath, templateContent);
        }
        return 0;
    }

    @PostMapping("/deleteTmp")
    @ResponseBody
    public Object deleteTmp(@RequestBody Map<String, Object> param) {
        String modulePath = (String) param.get("modulePath");
        String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
        JSONObject jsonObject = new JSONObject();
        FileUtils.delete(FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR, tmpTreeName, modulePath));
        jsonObject.put("msg", "删除成功");
        return jsonObject;
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }


    private String getActualPath(String tmpTreeName, String modulePath) {
        return FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR, tmpTreeName, modulePath);
    }

    private String getModulePath(String tmpTreeName, String actualPath) {
        return actualPath.replace("", FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR, tmpTreeName));
    }

     /**
     * 生成模板文件
     *
     * @param
     */
    private Integer generateTmpTreeFiles(String sourceTmpTreeName, String targetTmpTreeName, List<String> tmps) {
        String targetTreePath = FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR, targetTmpTreeName);
        String sourceTreePath = FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR, sourceTmpTreeName);
        //拼接上全路径
        List<String> actualPathList = new ArrayList<>(tmps.size());
        tmps.stream().forEach(l -> actualPathList.add(FileUtils.concatPath(sourceTreePath, l)));

        FileUtils.copyDirWithFilter(sourceTreePath, targetTreePath, actualPathList);
        return 1;

    }
}
