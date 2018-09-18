#!/usr/bin/env groovy

def call() {
    echo("Executing [Static Analysis Quality Gate] stage steps...")

    if ('false' == SKIP) {
        timeout(time: 2, unit: 'MINUTES') {
            waitForQualityGate(abortPipeline: true)
        }
    }

    echo("Completed [Static Analysis Quality Gate] stage steps.")
}
