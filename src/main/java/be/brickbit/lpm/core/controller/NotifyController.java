package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.controller.command.notify.NotifyCommand;
import be.brickbit.lpm.core.service.api.notify.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/notify")
public class NotifyController {
    private SMSService smsService;

    @Autowired
    public NotifyController(SMSService smsService) {
        this.smsService = smsService;
    }

    @PreAuthorize(value = "hasAnyRole('ADMIN', 'CATERING_ADMIN', 'CATERING_CREW')")
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void notifyUser(@RequestBody NotifyCommand notifyCommand) {
        smsService.sendSMS(notifyCommand.getUserId(), notifyCommand.getMessage());
    }
}
