package com.group_3.healthlink.servlets.TestResults;

import com.group_3.healthlink.SystemLogAction;
import com.group_3.healthlink.User;
import com.group_3.healthlink.TestResult;
import com.group_3.healthlink.services.SystemLogService;
import com.group_3.healthlink.services.TestResultService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/test-results/download/*")
public class DownloadTestResultServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            // Extract result ID from URL
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.length() <= 1) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Result ID is required");
                return;
            }

            int resultId = Integer.parseInt(pathInfo.substring(1));

            // Get the test result
            TestResult result = TestResultService.getTestResultById(resultId);
            if (result == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("Test result not found");
                return;
            }

            // Check if user has access to this test result
            if (!TestResultService.hasAccess(user.getUserId(), user.getRole(), resultId)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("Access denied");
                return;
            }

            // Set response headers
            resp.setContentType(getContentType(result.getFileType()));
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + result.getFileName() + "\"");
            resp.setContentLength(result.getFileData().length);

            // Write file data to response
            try (OutputStream out = resp.getOutputStream()) {
                out.write(result.getFileData());
                out.flush();
            }

            SystemLogService.createNew(
                    user.getUserId(),
                    SystemLogAction.DOWNLOAD_TEST_RESULT,
                    "Test Result ID: " + resultId);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid result ID");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error downloading test result: " + e.getMessage());
        }
    }

    private String getContentType(String fileType) {
        switch (fileType.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            default:
                return "application/octet-stream";
        }
    }
}