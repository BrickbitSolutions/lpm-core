package be.brickbit.lpm.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

import be.brickbit.lpm.core.controller.command.notify.NotifyCommand;
import be.brickbit.lpm.core.service.api.notify.SMSService;
import be.brickbit.lpm.mail.MailService;

@RestController
@RequestMapping(value = "/notify")
public class NotifyController {
    private final SMSService smsService;
    private final MailService mailService;

    @Autowired
    public NotifyController(SMSService smsService, MailService mailService) {
        this.smsService = smsService;
        this.mailService = mailService;
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'CATERING_ADMIN', 'CATERING_CREW')")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void notifyUser(@RequestBody NotifyCommand notifyCommand) {
        smsService.sendSMS(notifyCommand.getUserId(), notifyCommand.getMessage());
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    @RequestMapping(value = "/mail/test", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void testMail(@RequestParam(name = "mail") String email) throws MessagingException {
        mailService.sendMail(email, "Test Mail", "This is a LPM Test Mail.");
    }
}
