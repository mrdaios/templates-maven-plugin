package io.github.mrdaios.templates.api.freemarkder.extend;

import freemarker.core.Environment;
import freemarker.template.*;
import io.github.mrdaios.templates.model.TemplateRenderModel;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * 添加动态创建文件的指令
 */
public class FileDirectiveModel implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
        FMRenderContext currentContext = FMRenderContext.getCurrentContext();
        if (null == currentContext) {
            return;
        }
        // 文件生成路径
        String filePath = params.get("name").toString();

        // 是否可以覆盖文件,默认true
        boolean overrided = true;
        if (null != params.get("overrided") && params.get("overrided") instanceof TemplateBooleanModel) {
            overrided = ((TemplateBooleanModel) params.get("overrided")).getAsBoolean();
        }

        // 根据条件判断是否生成文件
        boolean condition = true;
        if (null != params.get("condition") && params.get("condition") instanceof TemplateBooleanModel) {
            condition = ((TemplateBooleanModel) params.get("condition")).getAsBoolean();
        }

        if (condition) {
            // 渲染
            StringWriter writer = new StringWriter();
            body.render(writer);

            currentContext.put(filePath, writer);
            TemplateRenderModel model = new TemplateRenderModel(filePath, writer.toString(), overrided);
            currentContext.put(filePath, model);
        }
    }
}
