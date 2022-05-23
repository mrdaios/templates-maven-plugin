package io.github.mrdaios.templates.exception;

public class TemplateRenderException extends RuntimeException {

    public TemplateRenderException(String message) {
        super(String.format("[模板渲染异常]%s", message));
    }

    public TemplateRenderException(String message, Throwable cause) {
        super(String.format("[模板渲染异常]%s", message), cause);
    }

    public TemplateRenderException(Throwable cause) {
        super(cause);
    }
}
