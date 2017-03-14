package be.brickbit.lpm.mail;

import be.brickbit.lpm.infrastructure.exception.ServiceException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailTemplateServiceImpl implements MailTemplateService {
    private final Configuration freemarkerConfiguration;

    @Value("${lpm.core.domain:https://www.example.com}")
    private String domainName;

    @Value("${lpm.core.mail.template.location:}")
    private String templateLocation;

    @Autowired
    public MailTemplateServiceImpl(Configuration freemarkerConfiguration) {
        this.freemarkerConfiguration = freemarkerConfiguration;
    }

    @Override
    public String createGeneralMessage(String name, String message) {
        final Template template = loadTemplate("generalmessage.html");

        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("message", message);

        return parseTemplateToString(template, params);
    }

    @Override
    public String createPasswordResetMessage(String name, String newPassword) {
        final Template template = loadTemplate("changedpassword.html");

        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("newPassword", newPassword);

        return parseTemplateToString(template, params);
    }

    @Override
    public String createActivationMail(String name, String token) {
        final Template template = loadTemplate("activationmail.html");

        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("activationLink", String.format("%s/activate/%s", domainName, token));

        return parseTemplateToString(template, params);
    }

    private String parseTemplateToString(Template template, Map<String, Object> params) {
        try {
            return FreeMarkerTemplateUtils.processTemplateIntoString(template, params);
        } catch (IOException | TemplateException e) {
            throw new ServiceException("Could not parse mail template!", e);
        }
    }

    private Template loadTemplate(String templateName) {
        try {
            if (StringUtils.isEmpty(templateLocation)) {
                freemarkerConfiguration.setClassForTemplateLoading(this.getClass(), "/mail/templates");
            } else {
                freemarkerConfiguration.setDirectoryForTemplateLoading(new File(templateLocation));
            }

            return freemarkerConfiguration.getTemplate(templateName);
        } catch (IOException e) {
            throw new ServiceException(String.format("Could not load template '%s'", templateName), e);
        }
    }
}
