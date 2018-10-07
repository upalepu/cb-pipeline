#!/usr/bin/env groovy

def call() {
    echo("Executing [Publish Artifact] stage steps...")

    if ('false' == SKIP) {
        sh("mvn -Djacoco.skip=true -DskipSourceCompile=true -DskipTestCompile=true -Dskip.surefire.tests=true -Dskip.failsafe.tests=true -Dartifact.repo.host=nexus-svc.management.svc.cluster.local -Dartifact.repo.port=${NEXUS_SERVICE_PORT} -s dynamic-settings.xml -e deploy")
    }

    echo("Completed [Publish Artifact] stage steps.")
}
