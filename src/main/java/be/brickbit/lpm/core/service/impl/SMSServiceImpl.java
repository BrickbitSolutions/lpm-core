package be.brickbit.lpm.core.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.service.api.notify.SMSService;
import be.brickbit.lpm.core.service.api.user.UserService;
import be.brickbit.lpm.infrastructure.exception.ServiceException;
import be.brickbit.lpm.sms.SMSTemplate;

@Service
public class SMSServiceImpl implements SMSService {
    private SMSTemplate smsTemplate;
    private UserService userService;

    @Autowired
    public SMSServiceImpl(SMSTemplate smsTemplate, UserService userService) {
        this.smsTemplate = smsTemplate;
        this.userService = userService;
    }

    @Override
    public void sendSMS(Long userId, String text) {
        User user = userService.findOne(userId);

        if (StringUtils.isNotBlank(user.getMobileNr())) {
            smsTemplate.sendSMS(
                    user.getMobileNr(),
                    String.format("Hello %s, %s", user.getUsername(), text)
            );
        } else {
            throw new ServiceException("User has not set a mobile phone nr.");
        }
    }
}
