package be.brickbit.lpm.core.service.impl;

import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.integration.sms.SMSTemplate;
import be.brickbit.lpm.core.service.api.notify.SMSService;
import be.brickbit.lpm.core.service.impl.internal.api.InternalUserService;
import be.brickbit.lpm.infrastructure.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SMSServiceImpl implements SMSService {
    private SMSTemplate smsTemplate;
    private InternalUserService userService;

    @Autowired
    public SMSServiceImpl(SMSTemplate smsTemplate, InternalUserService userService) {
        this.smsTemplate = smsTemplate;
        this.userService = userService;
    }

    @Override
    public void sendSMS(Long userId, String text) {
        User user = userService.findOne(userId);

        if (StringUtils.isNotBlank(user.getMobileNr())){
            smsTemplate.sendSMS(
                    user.getMobileNr(),
                    String.format("Hello %s, %s", user.getUsername(), text)
            );
        }else{
            throw new ServiceException("User has not set a mobile phone nr.");
        }
    }
}
