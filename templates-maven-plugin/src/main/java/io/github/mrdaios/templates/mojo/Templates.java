package io.github.mrdaios.templates.mojo;

import io.github.mrdaios.templates.TemplateProvider;
import io.github.mrdaios.templates.TemplateProviderManager;
import io.github.mrdaios.templates.exception.TemplateLoaderException;
import io.github.mrdaios.templates.exception.TemplateRenderException;
import io.github.mrdaios.templates.model.TemplateRenderModel;
import io.github.mrdaios.templates.service.TemplateLoaderService;
import io.github.mrdaios.templates.service.TemplateRenderService;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Mojo(name = "templates")
public class Templates extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    /**
     * 模板路径
     */
    @Parameter(property = "templatePath")
    private File templatePath;

    @Parameter(property = "templateType", required = true)
    private String templateType;

    /**
     * 模板数据路径
     */
    @Parameter(property = "templateDataPath", required = true)
    private URL templateDataPath;

    /**
     * 生成路径
     */
    @Parameter(property = "templateOutputPath", required = true)
    private File templateOutputPath;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        if (null == templateType ||
                null == templateDataPath ||
                null == templateOutputPath) {
            log.warn("not found configuration.");
            return;
        }
        TemplateProvider templateProvider = TemplateProviderManager.getDriver(templateType);
        try {
            log.info("模板开始加载...");
            TemplateLoaderService templateLoader = templateProvider.getTemplateLoader();
            Map<String, String> templateMap = templateLoader.loadTemplate(templatePath);
            log.info("模板开始渲染...");
            TemplateRenderService templateRender = templateProvider.getTemplateRender();
            Map<String, TemplateRenderModel> renderResult = templateRender.render(templateMap, templateDataPath);
            log.info("文件开始生成...");
            generateFile(templateOutputPath.getPath(), renderResult);
        } catch (TemplateLoaderException exception) {
            throw new MojoFailureException(exception.getMessage(), exception);
        } catch (TemplateRenderException exception) {
            throw new MojoFailureException(exception.getMessage(), exception);
        }
    }

    private void generateFile(String outRootPath, Map<String, TemplateRenderModel> renderResult) {
        Log log = getLog();
        AtomicInteger count = new AtomicInteger();
        renderResult.forEach((renderPath, renderModel) -> {
            Path path = Paths.get(outRootPath, renderPath);
            File file = path.getParent().toFile();

            // Overrided
            if (path.toFile().exists() && !renderModel.isOverrided()) {
                return;
            }

            if (!file.exists()) {
                file.mkdirs();
            }
            file = path.toFile();
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                IOUtil.copy(renderModel.getContent(), fileOutputStream);
                IOUtil.close(fileOutputStream);
                count.incrementAndGet();
            } catch (FileNotFoundException exception) {
                throw new RuntimeException(exception.getMessage(), exception);
            } catch (IOException exception) {
                throw new RuntimeException(exception.getMessage(), exception);
            }
        });
        log.info(String.format("文件生成成功,总共:%d,成功:%d", renderResult.size(), count.get()));
    }
}
