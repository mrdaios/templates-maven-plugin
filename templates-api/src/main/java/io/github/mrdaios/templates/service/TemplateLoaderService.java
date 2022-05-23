package io.github.mrdaios.templates.service;

import io.github.mrdaios.templates.exception.TemplateLoaderException;

import java.io.File;
import java.util.Map;

public interface TemplateLoaderService {

    Map<String, String> loadTemplate(File templateRootPath) throws TemplateLoaderException;
}
