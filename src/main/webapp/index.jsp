<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>PatientPortal</title>
    <link href="css/bootstrap.min.css" rel="stylesheet" />
  </head>
  <body class="min-vh-100">
    <div class="d-flex justify-content-center align-items-center vh-100 px-5">
      <div class="row h-100 justify-content-center align-items-center g-2">
        <div class="col-sm-6 col-lg-5 container mx-auto">
          <div class="d-flex flex-column px-3 px-lg-4">
            <span class="h1 fw-semibold">PatientPortal</span>
          </div>

          <div
            class="d-flex align-items-center px-3 px-lg-4 mt-5 pt-5 pt-xl-0 mt-xl-n5"
          >
            <form
              class="d-flex flex-column gap-4"
              style="width: 23rem"
              id="login-form"
              action="<%= request.getContextPath() %>/login-servlet"
              method="POST"
            >
              <h3 class="h3">Sign In</h3>

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

              <div class="pt-1 mb-4">
                <button class="btn btn-primary btn-lg btn-block" type="submit">
                  Login
                </button>
              </div>

              <p>
                Don't have an account?
                <a href="/" class="link-info">Register here</a>
              </p>
            </form>
          </div>
        </div>
        <div class="col-sm-6 col-lg-5 px-0 d-none d-sm-block">
          <img
            src="https://media-s3-us-east-1.ceros.com/cleveland-clinic/images/2022/09/13/24e7571a6e0c34bfdd8ac4a4dba773e4/lottconcepts-recovered-70.png"
            alt="Doctor using laptop computer"
            class="w-75 h-75"
          />
        </div>
      </div>
    </div>
    <script src="js/bootstrap.min.js"></script>
    <!-- <script>
      document.addEventListener('DOMContentLoaded', () => {
        const loginForm = document.querySelector('#login-form');
        loginForm.addEventListener('submit', (ev) => {
         ev.preventDefault();

          const formData = new FormData(loginForm);
          const email = formData.get('email');
          const password = formData.get('password');

          console.log('Login form submitted:', {
            email,
            password,
          });
        });
      });
    </script> -->
  </body>
</html>
