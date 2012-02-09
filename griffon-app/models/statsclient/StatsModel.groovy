package statsclient

import groovy.util.logging.Log
import java.util.logging.Level

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

        serviceSubscribers.each { subscriber ->
            try{
                subscriber.onUpdate(data.serviceData)
            } catch(Exception ex) {
                log.log(Level.WARNING, "error while notifying subscriber", ex)
            }
        }

        subscribers.each { subscriber ->
            try{
                subscriber.onUpdate(data)
            } catch(Exception ex) {
                log.log(Level.WARNING, "error while notifying subscriber", ex)
            }
        }
    }


    Set<Object> serviceSubscribers = new HashSet<Object>()
    def subscribeToServiceData = { subscriber ->
        serviceSubscribers << subscriber
    }

    def unSubscribeFromServiceData = { subscriber ->
        serviceSubscribers.remove(subscriber)
    }
}
