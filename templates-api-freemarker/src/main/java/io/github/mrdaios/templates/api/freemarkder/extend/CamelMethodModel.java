package io.github.mrdaios.templates.api.freemarkder.extend;

import freemarker.template.SimpleSequence;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * camel命名
 */
public class CamelMethodModel implements TemplateMethodModelEx {
    @Override
    public Object exec(List arguments) throws TemplateModelException {
        Object collect = arguments.stream().map(argument -> {
            String string = argument.toString();
            if (argument instanceof SimpleSequence) {
                try {
                    string = ((String) exec(((SimpleSequence) argument).toList()));
                } catch (TemplateModelException e) {
                    e.printStackTrace();
                }
            }
            return camelString(string);
        }).collect(Collectors.joining(""));
        return collect;
    }

    private String camelString(String string) {
        if (string.length() > 0) {
            return string.substring(0, 1).toUpperCase() + string.substring(1);
        } else {
            return string.toUpperCase();
        }
    }
}
