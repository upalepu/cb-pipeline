#!/usr/bin/env groovy

def call() {
    echo("Executing [Integration Tests] stage steps...")

    if ('false' == SKIP) {
        sh("mvn -DskipSourceCompile=true -DskipTestCompile=true -Dskip.surefire.tests=true -Dmaven.javadoc.skip=true -s dynamic-settings.xml -e integration-test")
    }

    echo("Completed [Integration Tests] stage steps.")
}
