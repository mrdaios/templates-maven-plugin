package io.github.mrdaios.templates.exception;

public class TemplateLoaderException extends RuntimeException {

    public TemplateLoaderException(String message) {
        super(String.format("[模板加载异常]%s", message));
    }

    public TemplateLoaderException(String message, Throwable cause) {
        super(String.format("[模板加载异常]%s", message), cause);
    }

    public TemplateLoaderException(Throwable cause) {
        super(cause);
    }
}
