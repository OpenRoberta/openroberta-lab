define(["exports", "jquery", "guiState.controller"], function(exports, $, GUISTATE_C) {
    var $disclaimer = $("#cookieDisclaimer"),
    cookieName = "OpenRoberta_Cookie",
    cookieValue = "accepted",
    cookieSettings = {
        expires: 60,
        path: "/",
        domain: "",
        secure: false // Set to false for debugging:
    };
    
    function init() {
        var $async = $.Deferred();
        
        $.when(GUISTATE_C.init()).then(function(){
            
            if (cookieExists() || GUISTATE_C.isPublicServerVersion()) {
                refreshCookie();
            } else {
                $disclaimer.find("button").click(refreshCookie);
                showDisclaimer();
            }
            
            $async.resolve();
        });
        return $async.promise();
    }
    
    exports.init = init;

    /**
     * @private
     */
    function refreshCookie() {
        $.cookie(cookieName, cookieValue, cookieSettings);
        hideDisclaimer();
    }

    /**
     * @private
     */
    function hideDisclaimer() {
        if (!$disclaimer.hasClass("accepted")) {
            $disclaimer.addClass("accepted");
        }
    }
    /**
     * @private
     */
    function showDisclaimer() {
        $disclaimer.removeClass("accepted");
        $disclaimer.removeClass("hidden");
    }
    
    function cookieExists() {
        return typeof $.cookie(cookieName) !== "undefined";
    }
    
    exports.allowCookies = cookieExists;
    
    function maxExpirationTime(maximum = 364) {
        if (typeof maximum !== "number") {
            maximum = 364;
        }
        return cookieExists() ? Math.min(maximum, 364) : 0;
    }
    
    exports.maxExpirationTime = maxExpirationTime;
});