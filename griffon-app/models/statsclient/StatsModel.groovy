package statsclient

import groovy.util.logging.Log

@Log
class StatsModel {

    def statsData
    Set<Object> subscribers = new HashSet<Object>()
    
    def subscribe = { subscriber ->
        subscribers << subscriber
    }

    def unSubscribe = { subscriber ->
        subscribers.remove(subscriber)
    }
    
    def onUpdate = { data ->
        statsData = data
        subscribers.each {subscriber ->
            subscriber.onUpdate(data)
        }
    }
}
