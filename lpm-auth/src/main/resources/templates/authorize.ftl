<html>
<head>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- Optional theme -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-sm-6 col-sm-offset-3 jumbotron">
            <h2 class="text-center">Please Confirm</h2>
            <p class="text-center">
                Allow access to listed data scopes?
            </p>
            <p>
                <ul class="list-unstyled">
                <#list authorizationRequest.scope as scope>
                    <li class="text-center">${scope}</li>
                </#list>
                </ul>
            </p>
            <div class="small text-center">
                <strong>Client: </strong>${authorizationRequest.clientId}
                <br>
                <strong>Origin: </strong>${authorizationRequest.redirectUri}
            </div>
            <div class="col-sm-4 text-center">
                <form id="confirmationForm" name="confirmationForm"
                      action="../oauth/authorize" method="post">
                    <input name="user_oauth_approval" value="true" type="hidden"/>
                    <button class="btn btn-primary" type="submit">Approve</button>
                </form>
            </div>
            <div class="col-sm-4"></div>
            <div class="col-sm-4 text-center">
                <form id="denyForm" name="confirmationForm"
                      action="../oauth/authorize" method="post">
                    <input name="user_oauth_approval" value="false" type="hidden"/>
                    <button class="btn btn-danger" type="submit">Deny</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>