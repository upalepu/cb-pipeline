#!/usr/bin/env groovy

def call() {
    echo("Executing [Deploy to Production] stage steps...")

    def namespace = 'production'
//    cbk8s.setupkubecfg()
    cbk8s.apply(namespace, '8612') //need to use a different port b/c this demo is running on the same cluster and node
    cbk8s.testContainer(namespace)

    echo("Completed [Deploy to Production] stage steps.")
}
