import "../utils/jquery.js";

$(() => {
    PrimeFaces.widget.Clock = PrimeFaces.widget.Clock.extend({
        refresh: function (cfg) {
            this._super(cfg)
            if (cfg.displayMode === 'analog')
                this.stop();
        },
        init: function (cfg) {
            this._super(cfg)
            if (cfg.displayMode === 'analog')
                this.stop()
        }
    });
});

