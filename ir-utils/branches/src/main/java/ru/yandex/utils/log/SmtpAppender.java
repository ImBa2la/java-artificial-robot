package ru.yandex.utils.log;

import org.apache.log4j.spi.TriggeringEventEvaluator;

import ru.yandex.utils.ApplicationUtils;

public class SmtpAppender extends org.apache.log4j.net.SMTPAppender {
	
    public SmtpAppender() {}

    public SmtpAppender(TriggeringEventEvaluator triggeringEventEvaluator) {
        super(triggeringEventEvaluator);
    }

    @Override
    public void setSubject(String subject) {
        super.setSubject(ApplicationUtils.getHostName() + ":" + subject);
    }
}