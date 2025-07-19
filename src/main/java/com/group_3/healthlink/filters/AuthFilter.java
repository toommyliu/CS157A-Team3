package com.group_3.healthlink.filters;

import com.group_3.healthlink.User;
import com.group_3.healthlink.services.AuthService;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class AuthFilter implements Filter {
  private static final List<String> PUBLIC_ROUTES = Arrays.asList(
      "/login",
      "/register",
      "/");

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String requestURI = httpRequest.getRequestURI(); // /healthlink_war_exploded/dashboard
    String contextPath = httpRequest.getContextPath(); // healthlink_war_exploded
    String relativePath = requestURI.substring(contextPath.length()); // /dashboard

    if (isStaticResource(relativePath) || isPublicRoute(relativePath)) {
      chain.doFilter(request, response);
      return;
    }

    User user = AuthService.ensureAuthenticated(httpRequest);
    if (user == null) {
      httpResponse.sendRedirect(contextPath + "/login");
      return;
    }

    chain.doFilter(request, response);
  }

  private boolean isPublicRoute(String pathToRoute) {
    return PUBLIC_ROUTES.contains(pathToRoute) || pathToRoute.isEmpty();
  }

  private boolean isStaticResource(String pathToRoute) {
    return pathToRoute.startsWith("/css/") ||
        pathToRoute.startsWith("/js/") ||
        pathToRoute.endsWith(".css") ||
        pathToRoute.endsWith(".js");
  }
}
