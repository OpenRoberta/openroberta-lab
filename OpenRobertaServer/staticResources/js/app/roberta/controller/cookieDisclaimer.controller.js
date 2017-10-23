define(["exports", "jquery", "guiState.controller"], function(exports, $, GUISTATE_C) {
    var $disclaimer = $("#cookieDisclaimer"),
        $okButton = $disclaimer.children(".ok-button"),
        cookieName = "OpenRoberta_Cookie",
        cookieValue = "accepted",
        cookieSettings = {
            expires: 30,
            path: "/",
            domain: "",
            secure: true
        },
        cookiesEnabled = cookiesAllowedByBrowser();
    
    function init() {
        
        toggleCookieRelatedContent();
        
        if (!cookiesEnabled) {
           return;
        }
        
        cookieSettings.secure = GUISTATE_C.isPublicServerVersion();
        
        $(window).on('unload', function(){
            if (!cookiesAllowed()) {
                var cookies = $.cookie();
                for (var cookie in cookies) {
                    if (cookies.hasOwnProperty(cookie)) {
                        $.removeCookie(cookie);
                    }
                }
            }
        });
        
        $disclaimer.children("button").click(hideDisclaimer);
        $disclaimer.children(".no-button").click(function(){
            cookiesEnabled = false; 
            toggleCookieRelatedContent();
        });
        
        if (cookiesAllowed() || !GUISTATE_C.isPublicServerVersion()) {
            refreshCookie();
        } else {
            addHandler(refreshCookie);
            showDisclaimer();
        }
    }
    
    exports.init = init;
    
    function saveCookie(cookieName, cookieValue, cookieSettings) {
        if (!cookiesEnabled) {
            return false;
        }
        
        // Only allow secure cookies on the live server
        cookieSettings.secure = cookieSettings.secure || GUISTATE_C.isPublicServerVersion();
        
        if (cookiesAllowed()) {
            if (cookieSettings.expires) {
                cookieSettings.expires = maxExpirationTime(cookieSettings.expires);
            }
            $.cookie(cookieName, cookieValue, cookieSettings);
            return true;
        } else {
            addHandler(function(){
                if (cookieSettings.expires) {
                    cookieSettings.expires = maxExpirationTime(cookieSettings.expires);
                }
                $.cookie(cookieName, cookieValue, cookieSettings);
            });
            return false;
        }
    }
    exports.saveCookie = saveCookie;

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
        $disclaimer.addClass("accepted");
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
    
    function cookiesAllowedByBrowser() {
        // If that setting exists and is true, the browser accepts cookies
        if (navigator.cookieEneabled) {
            return true;
        }

        var testCookie = "BrowserCookieTest",
            testCookieValue = "test",
            cookiesDeactivated = false;
        
        // For some browsers we simply have to test the functionality
        $.cookie(testCookie, testCookieValue, {
            expires: 1,
            secure: false
        });
        
        cookiesDeactivated = $.cookie(testCookie) === testCookieValue;

        $.removeCookie(testCookie);
        
        return cookiesDeactivated;
    }
    exports.cookiesAllowedByBrowser = cookiesAllowedByBrowser;
    
    function cookiesAllowed() {
        return cookieExists();
    }
    exports.cookiesAllowed = cookiesAllowed;

    /**
     * @private
     */
    function addHandler(func) {
        if (typeof func === "function") {
            $okButton.one('click', func);
        }
    }
    exports.addHandler = addHandler;
    
    function maxExpirationTime(maximum) {
        if (typeof maximum !== "number") {
            maximum = 364;
        }
        return cookieExists() ? Math.min(maximum, 364) : null;
    }
    exports.maxExpirationTime = maxExpirationTime;
    
    function toggleCookieRelatedContent() {
        $('.cookies-enabled').toggleClass("cookie-hide", !cookiesEnabled);
        $('.cookies-disabled').toggleClass("cookie-show", !cookiesEnabled);
    }
});