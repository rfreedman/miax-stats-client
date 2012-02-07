package statsclient

import groovy.beans.Bindable

class MonitorTabModel {

    def name;

    def dateFormat = "hh:mm:ss:SSS"

    @Bindable
    def statsData = new StatsTableModel()

    @Bindable
    def detailData = this.statsData // todo - this should be the real detail data

    @Bindable
    def lastUpdate = new Date().format(dateFormat)

    @Bindable lastUpdateComplete = new Date().format(dateFormat)

    def update = {
        System.out.println("updating ${name}")
        lastUpdate = new Date().format(dateFormat)
        statsData.update()
        lastUpdateComplete = new Date().format(dateFormat)
    }
}