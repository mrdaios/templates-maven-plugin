package io.github.mrdaios.templates.model;

import java.nio.file.Paths;

public class TemplateRenderModel {

    private final String filePath;
    private final String fileName;
    private final boolean overrided;
    private final String content;

    public TemplateRenderModel(String filePath, String content, boolean overrided) {
        this.filePath = filePath;
        this.fileName = Paths.get(filePath).getFileName().toString();
        this.overrided = overrided;
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isOverrided() {
        return overrided;
    }

    public String getContent() {
        return content;
    }
}
