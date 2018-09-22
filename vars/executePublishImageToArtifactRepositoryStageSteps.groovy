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

/*
    sh("docker version")
    sh("docker build -t ${dockerImageTag} -t ${dockerImageTagLatest} -t ${dockerRegistryTag} -t ${dockerRegistryTagLatest} --build-arg JAR_FILE=${jarFile} .")

    withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'LOGIN_USERNAME', passwordVariable: 'LOGIN_PASSWORD')]) {
        sh("echo ${LOGIN_PASSWORD} | docker login --username ${LOGIN_USERNAME} --password-stdin ${dockerHostAndDockerPort}")
    }

    sh("docker push ${dockerRegistryTag}")
    sh("docker push ${dockerRegistryTagLatest}")
*/
    withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'LOGIN_USERNAME', passwordVariable: 'LOGIN_PASSWORD')]) {
        buildImage( name: "${artifactId}", password: "${LOGIN_PASSWORD}", username: "${LOGIN_USERNAME}", path: "${jarfile}" )
        tagImage( name: "${artifactId}", tag: "${dockerImageTag}", password: "${LOGIN_PASSWORD}", username: "${LOGIN_USERNAME}" )
        tagImage( name: "${artifactId}", tag: "${dockerImageTagLatest}", password: "${LOGIN_PASSWORD}", username: "${LOGIN_USERNAME}" )
        tagImage( name: "${artifactId}", tag: "${dockerRegistryTag}", password: "${LOGIN_PASSWORD}", username: "${LOGIN_USERNAME}" )
        tagImage( name: "${artifactId}", tag: "${dockerRegistryTagLatest}", password: ${LOGIN_PASSWORD}, username: ${LOGIN_USERNAME} )
        pushImage( name: "${artifactId}", tag: "${dockerRegistryTag}", registry: "${dockerHostAndDockerPort}", password: "${LOGIN_PASSWORD}", username: "${LOGIN_USERNAME}" )
        pushImage( name: "${artifactId}", tag: "${dockerRegistryTagLatest}", registry: "${dockerHostAndDockerPort}", password: "${LOGIN_PASSWORD}", username: "${LOGIN_USERNAME}" )
    }

    echo("Completed [Publish Image] stage steps.")
}
