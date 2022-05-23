package io.github.mrdaios.templates.api.freemarkder;

import io.github.mrdaios.templates.api.freemarkder.extend.FMRenderContext;
import io.github.mrdaios.templates.exception.TemplateLoaderException;
import io.github.mrdaios.templates.service.TemplateLoaderService;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FMBaseTemplateLoaderService implements TemplateLoaderService {
    @Override
    public Map<String, String> loadTemplate(File templateRootPath) throws TemplateLoaderException {
        if (null == templateRootPath) {
            throw new TemplateLoaderException("模版配置为空.");
        }
        try {
            return traverse(templateRootPath, templateRootPath);
        } catch (IOException exception) {
            throw new TemplateLoaderException(exception.getMessage(), exception);
        }
    }

    protected Map<String, String> traverse(File templateRootPath, File file) throws IOException {
        Map<String, String> templateMap = new HashMap<>();
        if (null == file || !file.exists() || !file.isDirectory()) {
            return templateMap;
        }
        for (File listFile : file.listFiles()) {
            if (listFile.isDirectory()) {
                templateMap.putAll(traverse(templateRootPath, listFile));
            } else if (!listFile.getName().startsWith(".")) {
                StringWriter writer = new StringWriter();

                FileInputStream fileInputStream = new FileInputStream(listFile);
                IOUtils.copy(fileInputStream, writer);
                IOUtils.closeQuietly(fileInputStream);
                String templatePath = templateRootPath.toURI().relativize(listFile.toURI()).toString();
                templateMap.put(templatePath, writer.toString());
                FMRenderContext.getCurrentContext().putTemplate(templatePath, writer.toString());
            }
        }
        return templateMap;
    }
}