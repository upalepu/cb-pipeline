#!/usr/bin/env groovy

def call() {
    echo("Executing [Deploy to Production?] stage steps...")

    notifications.notifyOfInput()
    input(message: 'Deploy to production?', ok: 'Yes')

    echo("Completed [Deploy to Production?] stage steps.")
}
