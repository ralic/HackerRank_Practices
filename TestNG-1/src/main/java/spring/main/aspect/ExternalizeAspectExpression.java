package spring.main.aspect;

import org.aspectj.lang.annotation.Pointcut;


public class ExternalizeAspectExpression {

    @Pointcut("execution(* spring.main.bean.MailService.*(*))")
    public void mailServiceMethods() {
    }

}
