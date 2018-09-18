#!/usr/bin/env groovy

def call() {
    echo("Executing [Deploy to Pre-Production] stage steps...")

    def namespace = 'pre-production'
    kubernetes.apply(namespace, '8611') //need to use a different port b/c this demo is running on the same cluster and node
    kubernetes.testContainer(namespace)

    echo("Completed [Deploy to Pre-Production] stage steps.")
}
