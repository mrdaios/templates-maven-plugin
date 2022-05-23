package io.github.mrdaios.templates.api.freemarkder.extend;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import io.github.mrdaios.templates.model.TemplateRenderModel;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * FMRenderContext，用于在当前共享相关信息
 */
public class FMRenderContext {
    private static final ThreadLocal<FMRenderContext> threadContext = new ThreadLocal<>();

    private final Configuration originTemplateConfiguration;
    private Configuration templateConfiguration;
    private StringTemplateLoader templateLoader;
    private Map<String, Writer> writerMap = new HashMap<>();

    private Map<String, TemplateRenderModel> renderModelMap = new HashMap<>();

    public FMRenderContext() {
        originTemplateConfiguration = new Configuration(Configuration.VERSION_2_3_22);
        originTemplateConfiguration.setSharedVariable("file", new FileDirectiveModel());
        originTemplateConfiguration.setSharedVariable("camel", new CamelMethodModel());
        originTemplateConfiguration.setSharedVariable("package2path", new Package2PathMethodModel());
        originTemplateConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        templateLoader = new StringTemplateLoader();
        templateConfiguration = (Configuration) originTemplateConfiguration.clone();
        templateConfiguration.setTemplateLoader(templateLoader);
    }

    public static FMRenderContext getCurrentContext() {
        FMRenderContext context = threadContext.get();
        if (null == context) {
            context = new FMRenderContext();
            context.refreshContext();
            threadContext.set(context);
        }
        return context;
    }

    /**
     * 刷新context
     */
    public void refreshContext() {
        writerMap = new HashMap<>();
        renderModelMap = new HashMap<>();
    }

    /**
     * 存储渲染结果到context
     *
     * @param key
     * @param value
     * @return
     */
    public Writer put(String key, Writer value) {
        return writerMap.put(key, value);
    }

    public TemplateRenderModel put(String key, TemplateRenderModel value) {
        return renderModelMap.put(key, value);
    }

    /**
     * 获取渲染结果
     *
     * @return
     */
    public Map<String, Writer> getWriterMap() {
        return writerMap;
    }

    public Map<String, TemplateRenderModel> getRenderModelMap() {
        return renderModelMap;
    }

    /**
     * 新增模板
     *
     * @param name
     * @param templateContent
     */
    public void putTemplate(String name, String templateContent) {
        templateLoader.putTemplate(name, templateContent);
    }

    /**
     * 获取模板
     *
     * @param name
     * @return
     * @throws IOException
     */
    public Template getTemplate(String name) throws IOException {
        return templateConfiguration.getTemplate(name);
    }
}
