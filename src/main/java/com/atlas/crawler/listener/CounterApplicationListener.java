/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CounterApplicationListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {
    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        AtomicInteger counter = (AtomicInteger)getSession().getAttribute("counter");
        counter.incrementAndGet();
    }

    private HttpSession getSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest().getSession(false);
    }
}
