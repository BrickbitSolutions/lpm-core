<html>
<head>
    <title>LPM Login</title>
    <meta charset="utf-8">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <!--<style type="text/css">
        body {
            background: #333;
        }
    </style>-->
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-6 col-sm-offset-3 jumbotron">
            <h2 class="text-center"><i class="fa fa-lock"></i>WELCOME TO LPM</h2>
        <#if RequestParameters['error']??>
            <div class="alert alert-danger">
                There was a problem logging in. Please try again.
            </div>
        </#if>
            <form role="form" action="login" method="post" name="loginform">
                <fieldset>
                    <legend>Login</legend>

                    <div class="form-group">
                        <label for="username">Username</label>
                        <input id="username" type="text" name="username" placeholder="Your Username" required
                               class="form-control"/>
                    </div>

                    <div class="form-group">
                        <label for="password">Password</label>
                        <input id="password" type="password" name="password" placeholder="Your Password"
                               required class="form-control"/>
                    </div>

                    <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                    <div class="form-group">
                        <input type="submit" name="login" value="Login" class="btn btn-primary"/>
                    </div>
                </fieldset>
            </form>

            <div class="row text-center">
                <a href="#">No Account? Register here!</a>
            </div>
            <div class="row text-center">
                <a href="#">Terms of Use</a>
            </div>
            <div class="row text-center">
                <a href="#">Privacy Statement</a>
            </div>
        </div>
    </div>
</div>
</body>
</html>