package com.example.program;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class JwtFilter implements Filter {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();
        
        if ((path.startsWith("/api/") && !path.startsWith("/api/auth/login")) || path.equals("/data")) {
            String token = null;
            
            
            String auth = req.getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                token = auth.substring(7);
            }
            
            
            if (token == null) {
                token = req.getParameter("token");
            }
            
            if (token == null || !jwtUtil.validateToken(token)) {
                if (path.startsWith("/api/")) {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"token not found\"}");
                    return;
                } else {
                    res.sendRedirect("/redirect-error");
                    return;
                }
            }
            
            req.setAttribute("currentUser", jwtUtil.getUsernameFromToken(token));
        }
        
        chain.doFilter(request, response);
    }
}
