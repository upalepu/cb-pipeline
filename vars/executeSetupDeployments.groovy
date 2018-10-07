#!/usr/bin/env groovy

def call() {
    echo("Executing [Setup Deployments] stage steps...")
    cbk8s.setupkubecfg()
    echo("Completed [Setup Deployments] stage steps.")
}
