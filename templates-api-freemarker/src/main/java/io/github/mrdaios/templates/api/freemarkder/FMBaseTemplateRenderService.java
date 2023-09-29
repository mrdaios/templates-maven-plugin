package io.github.mrdaios.templates.api.freemarkder;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.github.mrdaios.templates.api.freemarkder.extend.FMRenderContext;
import io.github.mrdaios.templates.exception.TemplateRenderException;
import io.github.mrdaios.templates.model.TemplateRenderModel;
import io.github.mrdaios.templates.service.TemplateRenderService;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FMBaseTemplateRenderService implements TemplateRenderService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, TemplateRenderModel> render(Map<String, String> template, URL templateDataPath) throws TemplateRenderException {
        return render(template, templateDataPath, null);
    }

    @Override
    public Map<String, TemplateRenderModel> render(Map<String, String> template, URL templateDataPath, Map globalTemplateData) throws TemplateRenderException {
        Map data = loadData(templateDataPath);
        if (globalTemplateData != null) {
            data.put("global", globalTemplateData);
        }
        HashMap<String, TemplateRenderModel> renderModelMap = new HashMap<>();
        template.forEach((filePath, templateContent) -> {
            // 初始化运行环境
            FMRenderContext.getCurrentContext().refreshContext();
            Map<String, TemplateRenderModel> result = process(filePath, templateContent, data);
            if (null != result && result.size() > 0) {
                renderModelMap.putAll(result);
            }
        });
        return renderModelMap;
    }

    /**
     * 加载数据
     *
     * @param dataUrl
     * @return
     */
    private Map loadData(URL dataUrl) {
        Map map;
        try {
            map = objectMapper.readValue(dataUrl, Map.class);
        } catch (MalformedURLException exception) {
            throw new TemplateRenderException(exception.getMessage(), exception);
        } catch (StreamReadException exception) {
            throw new TemplateRenderException(exception.getMessage(), exception);
        } catch (DatabindException exception) {
            throw new TemplateRenderException(exception.getMessage(), exception);
        } catch (IOException exception) {
            throw new TemplateRenderException(exception.getMessage(), exception);
        }
        return map;
    }

    private Map<String, TemplateRenderModel> process(String path, String template, Object model) {
        Template renderTemplate = null;
        try {
            renderTemplate = FMRenderContext.getCurrentContext().getTemplate(path);
        } catch (IOException exception) {
            throw new TemplateRenderException(exception.getMessage(), exception);
        }

        /// 模板渲染
        Map<String, TemplateRenderModel> renderModelMap;
        try {
            StringWriter writer = new StringWriter();
            renderTemplate.process(model, writer);
            // 获取渲染结果
            renderModelMap = FMRenderContext.getCurrentContext().getRenderModelMap();
        } catch (TemplateException exception) {
            throw new TemplateRenderException(exception.getMessage(), exception);
        } catch (IOException exception) {
            throw new TemplateRenderException(exception.getMessage(), exception);
        }
        return renderModelMap;
    }
}
