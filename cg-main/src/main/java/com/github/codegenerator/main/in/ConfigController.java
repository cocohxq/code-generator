package com.github.codegenerator.main.in;

import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.spi.stephandler.StepHandler;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.DataUtil;
import com.github.codegenerator.common.util.FileUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        for (StepHandler stepHandler : ContextContainer.getStepInitializer(config.getStep())) {
            stepHandler.handle(ContextContainer.getContext());
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
}
