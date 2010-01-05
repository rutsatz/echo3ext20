/**
 * @class Default wait indicator implementation.
 */
EchoExt20.WaitIndicator = Core.extend(Echo.Client.WaitIndicator, {

    $static: {
    
        appWaitIndicator: null,
    
        boot: function(client) {
            client.setWaitIndicator(new EchoExt20.WaitIndicator());
            client._preWaitIndicatorDelay = 5;
            client._waitIndicatorRunnable = new Core.Web.Scheduler.MethodRunnable(Core.method(client, client._waitIndicatorActivate), 
                client._preWaitIndicatorDelay, false);
        }
    },
    
    $load: function() {
        Echo.Boot.addInitMethod(this.boot);
    },
    
    activate: function() {
        var waitEl = EchoExt20.WaitIndicator.appWaitIndicator;
        if (waitEl != null) {
            waitEl.firstChild.src = EchoExt20.waitIconUrl;
            Ext.get("body").applyStyles("cursor:wait");
            //waitEl.style.backgroundImage = "url(" + waitEl.waitIconUrl + ")";
        }

    },
    
    deactivate: function() {
        var waitEl = EchoExt20.WaitIndicator.appWaitIndicator;
        if (waitEl != null) {
            waitEl.firstChild.src = EchoExt20.noWaitIconUrl;
            Ext.get("body").applyStyles("cursor:default");
            //waitEl.style.backgroundImage = "url(" + waitEl.noWaitIconUrl + ")";
        }
    }
});