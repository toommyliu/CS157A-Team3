package com.group_3.healthlink.servlets.TestResults;

import com.group_3.healthlink.SystemLogAction;
import com.group_3.healthlink.User;
import com.group_3.healthlink.UserRole;
import com.group_3.healthlink.services.SystemLogService;
import com.group_3.healthlink.services.TestResultService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/test-results/delete")
public class DeleteTestResultServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            String resultIdStr = req.getParameter("resultId");
            if (resultIdStr == null || resultIdStr.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Result ID is required");
                return;
            }

            int resultId = Integer.parseInt(resultIdStr);

            // Check if user has access to this test result
            if (!TestResultService.hasAccess(user.getUserId(), user.getRole(), resultId)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("Access denied");
                return;
            }

            // Delete the test result
            boolean success = TestResultService.deleteTestResult(resultId);

            if (success) {
                SystemLogService.createNew(
                    user.getUserId(),
                    SystemLogAction.DELETE_TEST_RESULT, "Test Result ID: " + resultId
                );

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Test result deleted successfully");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("Failed to delete test result");
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid result ID");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error deleting test result: " + e.getMessage());
        }
    }
} 