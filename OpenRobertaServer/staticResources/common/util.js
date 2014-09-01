var UTIL = (function($) {
    /**
     * @memberOf UTIL load a JS script, this CANNOT be done with jquery (http://stackoverflow.com/questions/610995/jquery-cant-append-script-element)
     */
    function loadJS(jsFile) {
        // console.log('loading ' + jsFile);
        var script = document.createElement("script");
        script.type = "text/javascript";
        script.src = jsFile;
        script.charset = "utf-8";
        document.head.appendChild(script);
    }

    /**
     * @memberOf UTIL get a query param like '<uri>?qay=wsx&edc=rfv'. Name would be 'qay' (result would be 'wsx') or 'edc' (result would be 'rfv')
     */
    function getParameterByName(name) {
        var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
        return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
    }

    /**
     * @memberOf UTIL Toggle css-classes
     */
    function toggle($id, reset, set) {
        $id.removeClass(reset);
        $id.addClass(set);
    }

    /**
     * @memberOf UTIL Create a dialog with an OK button only.
     */
    function mkDialog(text) {
        var $dialog = $('<div title="Confirm">' + text + '</div>');
        $dialog.dialog({
            modal : true,
            close : function(event, ui) {
                $dialog.dialog('destroy');
            },
            buttons : [ {
                text : "Ok",
                click : function() {
                    $dialog.dialog('destroy');
                }
            } ]
        });
        $dialog.dialog('open');
    }

    /**
     * @memberOf UTIL Dump a JSON object into a HTML pre jquery object
     */
    function dump(data) {
        var $total = $('<pre class="font-small"></pre>');
        $total.append(JSON.stringify(data, undefined, 3));
        return $total;
    }

    /**
     * @memberOf UTIL array to ul
     */
    function array2ul(a) {
        var $total = $('<ol></ol>');
        for (var i = 0; i < a.length; i++) {
            var e = a[i];
            var $li = $('<li></li>');
            $li.append(showProperties(e));
            $total.append($li);
        }
        return $total;
    }

    /**
     * @memberOf UTIL date to string
     */
    function date2string(dateAsLong) {
        var date = new Date(dateAsLong);
        var cDay = date.getDate();
        var cMonth = date.getMonth();
        var cYear = date.getFullYear();
        var cHour = date.getHours();
        var cMin = date.getMinutes() + '';
        if (cMin.length == 1) {
            cMin = "0" + cMin;
        }
        var cSec = date.getSeconds();
        var cMsec = date.getMilliseconds() + '';
        if (cMsec.length == 1) {
            cMsec = "00" + cMsec;
        } else if (cMin.length == 2) {
            cMsec = "0" + cMsec;
        }
        return cDay + '.' + cMonth + '.' + cYear + ' ' + cHour + ':' + cMin + ':' + cSec + '.' + cMsec;
    }

    /**
     * @memberOf UTIL write key:value pairs (the properties) of an JSON object into an collection of HTML divs. Assume that some keys will have dates as values
     *           :-)
     */
    function showProperties(data) {
        var $total = $('<div></div>');
        for ( var p in data) {
            var $data = $('<div></div>');
            var value = data[p];
            if (p === 'created' && typeof value === 'number') {
                $data.text(p + ' : ' + date2string(value));
            } else if (p === 'generated' && typeof value === 'number') {
                $data.text(p + ' : ' + date2string(value));
            } else if (p === 'now' && typeof value === 'number') {
                $data.text(p + ' : ' + date2string(value));
            } else if (p === 'started' && typeof value === 'number') {
                $data.text(p + ' : ' + date2string(value));
            } else if (p === 'stat' && value instanceof Array) {
                $data.append(array2ul(value));
            } else {
                $data.text(p + ' : ' + JSON.stringify(value));
            }
            $total.append($data);
        }
        return $total.children();
    }

    return {
        'loadJS' : loadJS,
        'getParameterByName' : getParameterByName,
        'toggle' : toggle,
        'mkDialog' : mkDialog,
        'dump' : dump,
        'showProperties' : showProperties
    };
})($);