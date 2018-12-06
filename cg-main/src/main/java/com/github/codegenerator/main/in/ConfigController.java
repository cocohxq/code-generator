package com.github.codegenerator.main.in;

import com.alibaba.fastjson.JSONObject;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.TreeNode;
import com.github.codegenerator.common.spi.initializer.Initializer;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.FileUtils;
import com.github.codegenerator.common.util.TreeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置入口
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

    //step1 进入选择页
    @GetMapping("/init")
    public String init(Model model) {
        model.addAttribute("view", ContextContainer.getViewerInfo());
        return "config";
    }

    //step  初始化数据的初始化
    @PostMapping("/initData")
    @ResponseBody
    public Map<String, Object> initData(@RequestBody Config config) {
        ContextContainer.getContext().setConfig(config);
        for (Initializer initializer : ContextContainer.getStepInitializer(config.getStepType())) {
            initializer.initialize(ContextContainer.getContext());
        }
        Map<String, Object> result = new HashMap<>();
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
        if(fileType.intValue() == 1){
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
        String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
        String tmpModulePath = param.get("tmpModulePath");
        Integer fileType = Integer.parseInt(param.get("fileType"));
        return FileUtils.addFile(tmpTreeName, tmpModulePath,fileType);
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
        FileUtils.modifyFileName(tmpTreeName, tmpModulePath, newTmpModulePath);
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
        String filePath = FileUtils.concatPath(ContextContainer.getContext().getGenerateInfo().getCodepath(),fileName);
        ResponseEntity entity = new ResponseEntity<byte[]>(FileUtils.readFile2ByteArray(filePath),
                headers, HttpStatus.CREATED);
        FileUtils.deleteFile(filePath);//清除zip文件
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
            if (new File(FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR,userTmpTreeName)).exists()) {
                jsonObject.put("msg","不能提交到已存在的其它模板树");
                return jsonObject;
            }
            FileUtils.generateTmpTreeFiles(tmpTreeName, userTmpTreeName, selectTmps);
        }

        //保存之后再更新
        if (null != tmpModulePath && null != templateContent && !templateContent.equals("")) {
            FileUtils.updateFile(userTmpTreeName, tmpModulePath, templateContent);
        }
        jsonObject.put("msg","提交成功");
        return jsonObject;
    }

    @PostMapping("/commit")
    @ResponseBody
    public Integer commit(@RequestBody Map<String, Object> param){
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
        FileUtils.deleteFile(FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR,tmpTreeName,modulePath));
        jsonObject.put("msg","删除成功");
        return jsonObject;
    }
}
