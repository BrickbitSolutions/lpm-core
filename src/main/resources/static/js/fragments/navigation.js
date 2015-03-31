$(document).ready(function () {
    $(".button-collapse").sideNav();
    $('.collapsible').collapsible({
        accordion : false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
    });
    $(".dropdown-button").dropdown({
        hover:false
    });
    $(".logout-button").click(function(){
        $("#logout-form").submit();
    });
});
