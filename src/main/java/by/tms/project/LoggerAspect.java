package by.tms.project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAspect {

    private static Logger log = LogManager.getLogger(LoggerAspect.class);

    @Around("@annotation(by.tms.project.Logger)")
    public Object logger(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object returnValue = joinPoint.proceed();
        log.info("{}: Method {} was called with args {}, return value {}",joinPoint.getSignature().getDeclaringType().getName(), joinPoint.getSignature().getName(),args,returnValue);
        return returnValue;
    }
}
