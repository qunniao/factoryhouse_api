$.extend($,{
    loadScript : function(filenames) {

        if (filenames.length == 0) {
            return;
        }
        var filename = config.address+filenames.shift();
        $.getScript(filename, function (e,t) {
            $.loadScript(filenames);
        })
    }
});