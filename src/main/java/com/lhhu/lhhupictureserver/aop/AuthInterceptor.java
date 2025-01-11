package com.lhhu.lhhupictureserver.aop;

import com.lhhu.lhhupictureserver.annotation.AuthCheck;
import com.lhhu.lhhupictureserver.exception.BusinessException;
import com.lhhu.lhhupictureserver.exception.ErrorCode;
import com.lhhu.lhhupictureserver.exception.ThrowUtils;
import com.lhhu.lhhupictureserver.model.entity.User;
import com.lhhu.lhhupictureserver.model.enums.UserRoleEnum;
import com.lhhu.lhhupictureserver.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 定义权限校验切面
 * 采用环绕通知的方式
 */
@Aspect
@Component
public class AuthInterceptor {
    @Resource
    private UserService userService;

    /**
     * 执行拦截
     * @param joinPoint 切入点
     * @param authCheck 权限校验注解
     * @return
     * @throws Throwable
     */
    @Around("@annotation(authCheck)")
    public Object doIntercept(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        // 从上下文Request容器中获取request请求
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 必须具有的权限
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);

        // 不需要任何权限，直接放行
        if(mustRoleEnum == null){
            joinPoint.proceed();
        }

        // 需要有权限才能通过
        String loginUserRole = loginUser.getUserRole();
        UserRoleEnum loginUserRoleEnum = UserRoleEnum.getEnumByValue(loginUserRole);
        ThrowUtils.throwIf(loginUserRoleEnum == null, ErrorCode.NO_AUTH_ERROR);

        // 必须有管理员权限才能通过
        if(UserRoleEnum.ADMIN.equals(mustRoleEnum) && !loginUserRoleEnum.equals(mustRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return joinPoint.proceed();  //通过权限校验，放行
    }
}
