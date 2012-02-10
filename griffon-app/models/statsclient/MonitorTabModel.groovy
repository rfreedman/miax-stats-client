package statsclient

import groovy.beans.Bindable
import groovy.util.logging.Log
import java.util.logging.Level

@Log
class MonitorTabModel {

    static enum StatsDataView {
        SERVICE, FIRM, CLOUD, INSTANCE, SYSLOG

        def getData = { statsData ->
            def dataSubset = null
            switch (this) {
                case SERVICE:
                    dataSubset = statsData.serviceData
                    break

                case FIRM:
                    dataSubset = statsData.firmData
                    break

                case CLOUD:
                    dataSubset = statsData.cloudData
                    break

                case INSTANCE:
                    dataSubset = statsData.instanceData
                    break

                case SYSLOG:
                    break

            }

            return dataSubset
        }
    }

    def name;

    @Bindable
    def rollupMode = StatsDataView.CLOUD;

    def statsData // the data for all levels

    Map<StatsDataView, Set<Object>> subscriberLists = new HashMap<MonitorTabModel.StatsDataView, Set<Object>>();

    def subscribe(StatsDataView dataView, Object subscriber) {

        def subscribers = subscriberLists.get(dataView)
        if (subscribers == null) {
            subscribers = new HashSet<Object>()
            subscriberLists.put(dataView, subscribers)
        }
        subscribers << subscriber
    }

    def unSubscribe(StatsDataView dataView, Object subscriber) {
        def subscribers = subscriberLists.get(dataView)
        if (subscribers) {
            subscribers.remove(subscriber)
        }
    }

    def onUpdate = { data ->
        statsData = data

        StatsDataView.each {dataView ->
            Set<Object> subscriberList = subscriberLists.get(dataView)
            if (subscriberList != null) {

                def subset = dataView.getData(statsData)

                subscriberList.each {subscriber ->
                    try {
                        subscriber.onUpdate(subset)
                    } catch(Exception ex) {
                        //log.log(Level.WARNING, "error updating subscriber from onUpdate", ex)
                        ex.printStackTrace()
                    }
                }
            }
        }

    }
}