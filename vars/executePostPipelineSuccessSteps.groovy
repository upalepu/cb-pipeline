#!/usr/bin/env groovy

def call() {
    echo("Completed pipeline with result [success].")
    notifications.notifyPipelineSuccess()
}
