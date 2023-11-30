//package ru.maliutin.tasklist.web.security.expression;
//
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.security.access.expression.SecurityExpressionRoot;
//import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import ru.maliutin.tasklist.domain.user.Role;
//import ru.maliutin.tasklist.service.UserService;
//import ru.maliutin.tasklist.web.security.JwtEntity;
//
///**
// * Второй способ проверки доступа, к данным сервера у аутентифицированного пользователя.
// * Производится добавление своего SecurityExpression, к базавому определенному Spring.
// */
//@Setter
//@Getter
//public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
//
//    private Object filterObject;
//    private Object returnObject;
//    private Object target;
//    private HttpServletRequest servletRequest;
//
//    private UserService userService;
//
//    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
//        super(authentication);
//    }
//
//    public boolean canAccessUser(Long id){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        JwtEntity user = (JwtEntity) authentication.getPrincipal();
//
//        Long userId = user.getId();
//
//        return userId.equals(id) || hasAnyRole(authentication, Role.ROLE_ADMIN);
//    }
//
//    /**
//     * Служебный метод проверки присутствия у объекта аутентификации переданных ролей.
//     * @param authentication объект аутентификации.
//     * @param roles коллекция ролей
//     * @return true - если какая-либо из ролей коллекции присутствует у объекта аутентификации, иначе false.
//     */
//    private boolean hasAnyRole(Authentication authentication, Role... roles){
//        for (Role role : roles){
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
//            if (authentication.getAuthorities().contains(authority)){
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean canAccessTask(long taskId){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        JwtEntity user = (JwtEntity) authentication.getPrincipal();
//        Long userId = user.getId();
//
//        return userService.isTaskOwner(userId, taskId);
//    }
//
//    @Override
//    public Object getThis() {
//        return target;
//    }
//}
