package statsclient

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Log
import static statsclient.MonitorTabModel.StatsDataView.*


// TODO - make this handle multiple subscriptions, to support multiple windows open for the same or different services
// do this by having a collection of service-specific classes that have the functionality currently present in this class
@Log
class DataService {

    static final boolean fakeRandom = false

    def dataCallbackClosure

    // the names of the tabs - for now, this maps to the individual statistics types - one tab per stat
    // later, we may support custom tabs
    def getTabNames = {
        def tabNamesJson = getTabNamesJson()
        new JsonSlurper().parseText(tabNamesJson).tabNames
    }

    // the names of the all of the columns for each rollup type
    // this includes all of the stat columns for all of the stat types,
    // and will be subset for the model for each tab
    def getGlobalColumnConfig = {
        def jsonColumnConfig = generateFakeColumnConfig()
        def columnConfigs = new JsonSlurper().parseText(jsonColumnConfig).columnConfigs
        return columnConfigs
    }

    def getTableColumnConfig = { tabName, dataView ->
        def jsonTableColumnConfig = generateFakeTableColumnConfig(tabName, dataView)
        def tableColumnConfig = new JsonSlurper().parseText(jsonTableColumnConfig).tableColumnConfig
    }

    /*
    def dumpJson = {
        def jsonData = "{}"
        def data = new JsonSlurper().parseText(jsonData).data
        log.info "serviceData = ${data ? data.serviceData : 'null'}"
    }
    */

    def getTabNamesJson = {
        // todo - get this from the monitor-server, keyed by the service name, and later, possibly by the user name
        generateFakeTabNames()
    }

    def getColumnConfigJson = {
        // todo - get this from the monitor-server, keyed by the service name
        generateFakeColumnConfig()
    }

    def subscribe = { closure ->
        this.dataCallbackClosure = closure
        fakeSubscribeImpl()
        log.info("after subscribe")
    }

    def unsubscribe = {
        fakeUnsubscribeImpl()
    }

    // ================================== fake data services ===========================================================

    Timer fakeTimer = new Timer();
    TimerTask timerTask = new FakeModelUpdateTimerTask();
    final int COL_COUNT = 25; // number of fake columns of numeric data
    final random = new Random(new Date().getTime())

    class FakeModelUpdateTimerTask extends TimerTask {
        @Override
        void run() {
            //log.info("start of task")
            def jsonData = generateFakeData()
            //log.info("data generated")
            def data = new JsonSlurper().parseText(jsonData).data
            //log.info("data parsed")
            dataCallbackClosure(data)
            //log.info("notifications complete\n\n")
        }
    }

    def fakeSubscribeImpl = {
        // in the real implementation, we would use CometD to subscribe to a Bayeux Channel
        fakeTimer.scheduleAtFixedRate(timerTask, 0, 500)
    }

    def fakeUnsubscribeImpl = {
        fakeTimer.cancel()
        fakeTimer = new Timer()
        timerTask = new FakeModelUpdateTimerTask();
    }


    def generateFakeTabNames = {
        def json = new JsonBuilder()
        json tabNames: ["Capacity", "Latency", "Custom1", "Custom2"]
        json.toPrettyString()
    }

    // generate column configs for our fake data
    def generateFakeColumnConfig = {
        def json = new JsonBuilder()

        json columnConfigs: [
                serviceColumns: generateFakeServiceColumnConfig(),
                firmColumns: generateFakeFirmColumnConfig(),
                cloudColumns: generateFakeCloudColumnConfig(),
                instanceColumns: generateFakeInstanceColumnConfig()
        ]
        json.toPrettyString();
    }

    def generateFakeTableColumnConfig = { tabName, dataView ->

        def columns = []

        switch(dataView) {
            case SERVICE:
                getGlobalColumnConfig().serviceColumns.eachWithIndex{ column, index ->
                    if(index % 2 == 0 || index > 10) {
                        columns << column
                    }
                }
                break;

            case CLOUD:
                columns = getGlobalColumnConfig().cloudColumns
                break;

            case FIRM:
                columns = getGlobalColumnConfig().firmColumns
                break

            case INSTANCE:
                columns = getGlobalColumnConfig().instanceColumns
                break
        }

        def json = new JsonBuilder()
        json tableColumnConfig: columns
        json.toPrettyString()
    }


    def generateFakeServiceColumnConfig = {
        def columns = []
        for (int i = 0; i < COL_COUNT; i++) {
            columns.push("col-${i}")
        }
        return columns
    }

    def generateFakeFirmColumnConfig = {
        def columns = ["Firm"]
        for (int i = 0; i < COL_COUNT; i++) {
            columns.push("col-${i}")
        }
        return columns
    }


    def generateFakeCloudColumnConfig = {
        def columns = ["Cloud"]
        for (int i = 0; i < COL_COUNT; i++) {
            columns.push("col-${i}")
        }
        return columns
    }

    def generateFakeInstanceColumnConfig = {
        def columns = ["Cloud", "Firm", "Instance"]
        for (int i = 0; i < COL_COUNT; i++) {
            columns.push("col-${i}")
        }
        return columns
    }

    // generate four table's worth of data:
    // top-level rollup (1 row w/aggregates for all columns)
    // rollup grouped by firm
    // rollup grouped by cloud
    // rollup grouped by instance (this will contain rows for all instances,
    //   and will contain instance, cloud, and firm ids
    //   so that the tab's models can filter it according to the selected firm or cloud in the firm or cloud rollup table
    def generateFakeData = {

        def json = new JsonBuilder()

        json data: [
                serviceData: generateFakeServiceData(),
                firmData: generateFakeFirmData(),
                cloudData: generateFakeCloudData(),
                instanceData: generateFakeInstanceData()
        ]

        json.toPrettyString()
    }

    def generateFakeServiceData = {
        def rowCount = 1
        def d = []

        for (int i = 0; i < rowCount; i++) {
            def row = []
            for (int j = 0; j < COL_COUNT; j++) {
                row.push fakeRandom ? random.nextInt(101) : j
            }
            d.push(row)
        }

        return d
    }

    // first column is firm id
    def generateFakeFirmData = {
        def rowCount = 100
        def d = []

        for (int i = 0; i < rowCount; i++) {
            def row = [i]
            for (int j = 0; j < COL_COUNT; j++) {
                row.push fakeRandom ? random.nextInt(101) : j
            }
            d.push(row)
        }

        return d
    }

    // first column is cloud id
    def generateFakeCloudData = {
        def rowCount = 24
        def d = []

        for (int i = 0; i < rowCount; i++) {
            def row = [i]
            for (int j = 0; j < COL_COUNT; j++) {
                row.push fakeRandom ? random.nextInt(101) : j
            }
            d.push(row)
        }

        return d
    }

    // first three columns are cloud, firm, instance
    def generateFakeInstanceData = {
        def clouds = 24
        def instancesPerCloud = 32
        def firmsPerInstance = 10

        def d = []

        for (int cloud = 0; cloud < clouds; cloud++) {
            for (int instance = 0; instance < instancesPerCloud; instance++) {
                for (int firm = 0; firm < firmsPerInstance; firm++) {
                    def row = [cloud, firm, instance]
                    for (int j = 0; j < COL_COUNT; j++) {
                       row.push fakeRandom ? random.nextInt(101) : j
                    }
                    d.push(row)
                }
            }
        }

        return d
    }

}
