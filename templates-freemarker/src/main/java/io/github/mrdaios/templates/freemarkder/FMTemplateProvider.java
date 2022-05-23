package io.github.mrdaios.templates.freemarkder;

import io.github.mrdaios.templates.TemplateProvider;
import io.github.mrdaios.templates.api.freemarkder.FMBaseTemplateLoaderService;
import io.github.mrdaios.templates.api.freemarkder.FMBaseTemplateRenderService;
import io.github.mrdaios.templates.service.TemplateLoaderService;
import io.github.mrdaios.templates.service.TemplateRenderService;

public class FMTemplateProvider implements TemplateProvider {

    private TemplateLoaderService loaderServer;
    private TemplateRenderService renderServer;

    public FMTemplateProvider() {
        this.loaderServer = new FMBaseTemplateLoaderService();
        this.renderServer = new FMBaseTemplateRenderService();
    }

    @Override
    public TemplateLoaderService getTemplateLoader() {
        return loaderServer;
    }

    @Override
    public TemplateRenderService getTemplateRender() {
        return renderServer;
    }
}
