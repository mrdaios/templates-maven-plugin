package io.github.mrdaios.templates;

import io.github.mrdaios.templates.service.TemplateLoaderService;
import io.github.mrdaios.templates.service.TemplateRenderService;

public interface TemplateProvider {

    default String getTemplateType() {
        return this.getClass().getName();
    }

    TemplateLoaderService getTemplateLoader();

    TemplateRenderService getTemplateRender();
}
