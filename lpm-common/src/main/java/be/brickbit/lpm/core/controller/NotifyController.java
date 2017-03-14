package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.controller.command.notify.NotifyCommand;
import be.brickbit.lpm.infrastructure.AbstractController;
import be.brickbit.lpm.mail.MailService;
import be.brickbit.lpm.core.service.api.notify.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping(value = "/notify")
public class NotifyController extends AbstractController{
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
    @RequestMapping(value = "/mail/test", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void testMail(@RequestParam(name = "mail") String email) throws MessagingException {
        mailService.sendMail(email, "Test Mail", "This is a LPM Test Mail.");
    }
}
