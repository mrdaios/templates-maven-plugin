package io.github.mrdaios.templates.service;

import io.github.mrdaios.templates.exception.TemplateRenderException;
import io.github.mrdaios.templates.model.TemplateRenderModel;

import java.net.URL;
import java.util.Map;

public interface TemplateRenderService {

    Map<String, TemplateRenderModel> render(Map<String, String> template, URL templateDataPath) throws TemplateRenderException;

    Map<String, TemplateRenderModel> render(Map<String, String> template, URL templateDataPath, Map globalTemplateData) throws TemplateRenderException;

}
