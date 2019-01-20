package bing.aop;

import bing.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 方法执行时间打印拦截器
 *
 * @author: IceWee
 * @date: 2017/8/24
 */
@Slf4j
@Aspect
@Component
public class MethodTimeInterceptor {

    public static final String POINT = "execution (* bing..service*..*(..))";
    public static final String POINTCUT = "pointcut()";

    @Pointcut(POINT)
    public void pointcut() {
    }

    @Around(POINTCUT)
    public Object timeArround(ProceedingJoinPoint joinPoint) {
        Object object = null;
        Object[] args = joinPoint.getArgs();

        long startTime = System.currentTimeMillis();
        try {
            object = joinPoint.proceed(args);
        } catch (Throwable e) {
            log.error("统计某方法执行耗时出错...", e);
            if (e instanceof BusinessException) {
                BusinessException be = (BusinessException) e;
                throw be;
            }
        }
        long endTime = System.currentTimeMillis();

        // 获取执行的方法名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();

        // 打印耗时的信息
        this.printExecTime(methodName, startTime, endTime);

        return object;
    }

    /**
     * 打印方法执行耗时的信息
     *
     * @param methodName
     * @param startTime
     * @param endTime
     */
    private void printExecTime(String methodName, long startTime, long endTime) {
        long diffTime = endTime - startTime;
        log.debug("[MI] {} 方法执行耗时 {} 毫秒...", methodName, diffTime);
    }

}
