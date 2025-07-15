<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Healthlink - Register</title>
    <link href="css/bootstrap.min.css" rel="stylesheet" />
  </head>
  <body class="min-vh-100">
    <div class="d-flex justify-content-center align-items-center vh-100 px-5">
      <div
        class="row w-100 h-100 justify-content-center align-items-center g-2"
      >
        <div class="col-sm-6 col-lg-5 container mx-auto w-75">
          <div class="d-flex flex-column px-3 px-lg-4 mb-5">
            <h1 class="h1 fw-bold">Healthlink</h1>
          </div>

          <div class="d-flex align-items-center px-3 px-lg-4 mt-2">
            <form
              class="d-flex flex-column gap-4 w-100"
              id="register-form"
              action="<%= request.getContextPath() %>/register"
              method="POST"
            >
              <h3 class="h3">Create Account</h3>

              <div class="d-flex flex-row gap-2 w-100">
                <div class="d-flex flex-column gap-2 w-50">
                  <label for="first-name" class="form-label">First Name</label>
                  <input
                    id="first-name"
                    type="text"
                    name="firstName"
                    class="form-control"
                    placeholder="Enter your first name"
                    required
                  />
                </div>

                <div class="d-flex flex-column gap-2 w-50">
                  <label for="first-name" class="form-label">Last Name</label>
                  <input
                    id="last-name"
                    type="text"
                    name="lastName"
                    class="form-control"
                    placeholder="Enter your last name"
                    required
                  />
                </div>
              </div>

              <div class="d-flex flex-column gap-2">
                <label for="email" class="form-label">Email</label>
                <input
                  id="email"
                  type="email"
                  name="email"
                  class="form-control"
                  placeholder="Enter your email"
                  required
                />
              </div>

              <div class="d-flex flex-column gap-2">
                <label for="password" class="form-label">Password</label>
                <input
                  id="password"
                  type="password"
                  name="password"
                  class="form-control"
                  placeholder="Enter your password"
                  required
                />
              </div>

              <div class="d-flex flex-column gap-2 w-50">
                <label for="role" class="form-label">Role</label>
                <select class="form-select" id="role" name="role" required>
                    <option value="" disabled selected>Select your role</option>
                    <option value="patient">Patient</option>
                    <option value="doctor">Doctor</option>
                </select>
              </div>

              <div class="pt-1 mb-4">
                <button class="btn btn-primary" type="submit">Register</button>
              </div>

              <p>
                Already have an account?
                <a
                  href="<%= request.getContextPath() %>/login"
                  class="link-info"
                  >Login</a
                >
              </p>
            </form>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
