package com.bakery.bakeryordersystem.config;
import jakarta.servlet.http.*;
import org.springframework.web.servlet.HandlerInterceptor;

/** Guards /admin/** (ADMIN only) and /customer/** (CUSTOMER only). */
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws java.io.IOException {
        HttpSession s = req.getSession(false);
        String role = s == null ? null : (String) s.getAttribute("role");
        String path = req.getRequestURI();
        if (role == null) {
            if ("GET".equals(req.getMethod()))
                req.getSession().setAttribute("next", path + (req.getQueryString() == null ? "" : "?" + req.getQueryString()));
            res.sendRedirect("/login");
            return false;
        }
        boolean adminArea = path.startsWith("/admin");
        if (adminArea && !"ADMIN".equals(role)) { res.sendRedirect("/customer"); return false; }
        if (!adminArea && !"CUSTOMER".equals(role)) { res.sendRedirect("/admin"); return false; }
        return true;
    }
}
