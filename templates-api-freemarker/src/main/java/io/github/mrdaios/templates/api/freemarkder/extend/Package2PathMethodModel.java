package io.github.mrdaios.templates.api.freemarkder.extend;

import freemarker.template.TemplateMethodModelEx;

import java.nio.file.Paths;
import java.util.List;

/**
 * 包路径转换为路径
 *
 */
public class Package2PathMethodModel implements TemplateMethodModelEx {

    @Override
    public Object exec(List arguments) {
        try {
            String packageName = arguments.stream().findFirst().orElseThrow(() -> new RuntimeException("package2path参数为一个")).toString();
            String[] pathComponent = packageName.split("\\.");
            return Paths.get("", pathComponent).toString().replace("\\", "/").replace("APIComponent","");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}