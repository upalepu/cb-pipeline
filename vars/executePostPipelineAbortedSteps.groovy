#!/usr/bin/env groovy

def call() {
    echo("Completed pipeline with result [aborted].")
    notifications.notifyPipelineAborted()
}
