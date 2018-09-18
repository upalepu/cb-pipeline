#!/usr/bin/env groovy

def call() {
    echo("Executing [Static Analysis] stage steps...")

    if ('false' == SKIP) {
        withSonarQubeEnv('sonarqube') {
            sh("mvn -DskipSourceCompile=true -DskipTestCompile=true -Dskip.surefire.tests=true -Dskip.failsafe.tests=true -Dmaven.javadoc.skip=true -s dynamic-settings.xml -e sonar:sonar")
        }
    }

    echo("Completed [Static Analysis] stage steps.")
}
