#!/usr/bin/env groovy

def call() {
    echo("Executing [Publish Image] stage steps...")

    def pomInfo = readMavenPom()
    def artifactId = pomInfo.artifactId
    def version = pomInfo.version
    def jarFile = "${artifactId}-${version}.jar"
    def dockerImageTag = "${artifactId}:${version}"
    def dockerImageTagLatest = "${artifactId}:latest"
    def dockerHostAndDockerPort = "${NEXUS_HOSTNAME}:${NEXUS_SERVICE_PORT_DOCKER}"
    def dockerRegistryTag = "${dockerHostAndDockerPort}/${dockerImageTag}"
    def dockerRegistryTagLatest = "${dockerHostAndDockerPort}/${dockerImageTagLatest}"

    sh("sudo docker build -t ${dockerImageTag} -t ${dockerImageTagLatest} -t ${dockerRegistryTag} -t ${dockerRegistryTagLatest} --build-arg JAR_FILE=${jarFile} .")
    sh("sleep 10m")

    withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'LOGIN_USERNAME', passwordVariable: 'LOGIN_PASSWORD')]) {
        sh("echo ${LOGIN_PASSWORD} | sudo docker login --username ${LOGIN_USERNAME} --password-stdin ${dockerHostAndDockerPort}")
    }

    sh("sudo docker push ${dockerRegistryTag}")
    sh("sudo docker push ${dockerRegistryTagLatest}")
    echo("Completed [Publish Image] stage steps.")
}
