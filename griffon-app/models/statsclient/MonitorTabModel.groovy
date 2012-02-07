package statsclient

import groovy.beans.Bindable

class MonitorTabModel {

    static final FIRM = "firm"
    static final CLOUD = "cloud"

    def name;

    def dateFormat = "hh:mm:ss:SSS"

    @Bindable
    def rollupMode = CLOUD;

    @Bindable
    def statsData = new StatsTableModel()

    @Bindable
    def detailData = this.statsData // todo - this should be the real detail data

    @Bindable
    def lastUpdate = new Date().format(dateFormat)

    @Bindable lastUpdateComplete = new Date().format(dateFormat)

    def update = {
        lastUpdate = new Date().format(dateFormat)
        statsData.update()
        lastUpdateComplete = new Date().format(dateFormat)
    }
}