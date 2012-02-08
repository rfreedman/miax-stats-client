package statsclient

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Log

@Log
class StatsModel {

    final int COL_COUNT = 25; // number of fake columns of numeric data
    final random = new Random(new Date().getTime())

    /*
     * This is where we subscribe to stats published by the monitor-server app.
     *
     * When stats are received (data for all 4 tables) as JSON, we will decode
     * the JSON data and push it to three table models.
     *
     * The individual tab's models will bind to these 4 table models,
     * and build their own table models that are subsets of these.
     *
     */


    def jsonColumnConfig = "{}"
    def jsonData = "{}"

    def dumpColumnConfig = {
        def columnConfigs = new JsonSlurper().parseText(jsonColumnConfig).columnConfigs
        log.info "columnConfigs = ${columnConfigs}"
    }

    def dumpJson = {
        def data = new JsonSlurper().parseText(jsonData).data
        log.info "serviceData = ${data ? data.serviceData : 'null'}"
    }

    // generate column configs for our fake data
    // todo - get this from the monitor-server, keyed by the service name
    def generateFakeColumnConfig = {
        def json = new JsonBuilder()

        json columnConfigs: [
                serviceColumns: generateFakeServiceColumnConfig(),
                firmColumns: generateFakeFirmColumnConfig(),
                cloudColumns: generateFakeCloudColumnConfig(),
                instanceColumns: generateFakeInstanceColumnConfig()
        ]
        jsonColumnConfig = json.toPrettyString();
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

        jsonData = json.toPrettyString()
    }

    def generateFakeServiceData = {
        def rowCount = 1
        def d = []

        for (int i = 0; i < rowCount; i++) {
            def row = []
            for (int j = 0; j < COL_COUNT; j++) {
                row.push(random.nextInt(101))
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
                row.push(random.nextInt(101))
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
                row.push(random.nextInt(101))
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
                        row.push(random.nextInt(101))
                    }
                    d.push(row)
                }
            }
        }

        return d
    }
}