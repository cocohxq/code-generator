package com.github.codegenerator.main.in;

import com.alibaba.fastjson.JSONObject;
import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.spi.stephandler.StepHandler;
import com.github.codegenerator.common.util.ContextContainer;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 配置入口
 */
@Controller
public class ConfigController {

    //step1 进入选择页
    @GetMapping(value = {"/init", "/"})
    public String init(Model model,HttpServletRequest request) {
        Config config = Optional.ofNullable(request.getParameter("config")).map(str-> JSONObject.parseObject(str,Config.class)).orElse(new Config());

        if(StringUtils.isEmpty(config.getStep())){
            config.setStep(StepEnum.STEP_DB.getCode());
            config.setOperation("into");
        }
        Map<String, Object> result = execute(config);
        model.addAttribute("data", result.get("stepResult"));
        model.addAttribute("error", StringUtils.isEmpty(result.get("error"))? null : ((List)result.get("error")).stream().collect(Collectors.joining("")));
        model.addAttribute("view", ContextContainer.getViewerInfo());
        return "config";
    }

    /**
     * 每一步的操作都是对config对象的完善
     *
     * @param config
     * @return
     */
    @PostMapping("/execute")
    @ResponseBody
    public Map<String, Object> execute(@RequestBody Config config) {
        ContextContainer.getContext().setConfig(config);
        if (!StringUtils.isEmpty(config.getStep())) {
            for (StepHandler stepHandler : ContextContainer.getStepInitializer(config.getStep())) {
                stepHandler.handle(ContextContainer.getContext());
            }
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
