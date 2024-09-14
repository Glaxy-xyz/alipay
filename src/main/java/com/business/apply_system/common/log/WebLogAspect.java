package com.business.apply_system.common.log;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Web层日志切面
 *
 * @author 程序猿DD
 * @version 1.0.0
 * <a href="http://blog.didispace.com">...</a>
 */
@Slf4j
@Aspect
@Order(5)
@Component
public class WebLogAspect {

    private static final ThreadLocal<Long> COS_TIME = new ThreadLocal<>();
    private static final ThreadLocal<String> RESPONSE_URL = new ThreadLocal<>();

    @Pointcut("execution(public * com.business..*.controller.*Controller.*(..))")
    public void webLog() {
        // Do nothing because of PointCut
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.info("====================== webLogAspect attributes is null ======================");
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String requestId = UUID.randomUUID().toString();
        // 记录下请求内容
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("REQUEST_ID:", requestId);
        jsonObject.put("URL:", request.getRequestURL().toString());
        jsonObject.put("HTTP_METHOD:", request.getMethod());
        jsonObject.put("IP:", request.getRemoteAddr());
        jsonObject.put("CLASS_METHOD:", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        log.info("REQUEST:{}", jsonObject.toJSONString());
        COS_TIME.set(System.currentTimeMillis());
        RESPONSE_URL.set(requestId);
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        // 处理完请求，返回内容
        log.info("REQUEST_ID:{},RESPONSE :{},SPEND TIME:{} ", RESPONSE_URL.get(),
                ret, (System.currentTimeMillis() - COS_TIME.get())+" ms");
        COS_TIME.remove();
        RESPONSE_URL.remove();
    }
}

