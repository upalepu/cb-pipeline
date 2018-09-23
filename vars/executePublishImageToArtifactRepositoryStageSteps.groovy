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
    sh("sudo docker build -t ${dockerImageTag} -t ${dockerImageTagLatest} -t ${dockerRegistryTag} -t ${dockerRegistryTagLatest} --build-arg JAR_FILE=${jarFile} .")

    withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'LOGIN_USERNAME', passwordVariable: 'LOGIN_PASSWORD')]) {
        sh("echo ${LOGIN_PASSWORD} | sudo docker login --username ${LOGIN_USERNAME} --password-stdin ${dockerHostAndDockerPort}")
    }

    sh("sudo docker push ${dockerRegistryTag}")
    sh("sudo docker push ${dockerRegistryTagLatest}")
*/
    echo("before with credentials")
    withCredentials([usernamePassword(credentialsId: 'nexus', usernameVariable: 'LOGIN_USERNAME', passwordVariable: 'LOGIN_PASSWORD')]) {
        echo("inside withCredentials; before buildimage")
        buildImage( name: "${artifactId}", password: "${LOGIN_PASSWORD}", username: "${LOGIN_USERNAME}", path: "${jarfile}" )
        echo("inside withCredentials; before tagimg ${dockerImageTag}")
        tagImage( name: "${artifactId}", tag: "${dockerImageTag}", password: "${LOGIN_PASSWORD}", username: "${LOGIN_USERNAME}" )
        echo("inside withCredentials; before tagimg ${dockerImageTagLatest}")
        tagImage( name: "${artifactId}", tag: "${dockerImageTagLatest}", password: "${LOGIN_PASSWORD}", username: "${LOGIN_USERNAME}" )
        echo("inside withCredentials; before tagimg ${dockerRegistryTag}")
        tagImage( name: "${artifactId}", tag: "${dockerRegistryTag}", password: "${LOGIN_PASSWORD}", username: "${LOGIN_USERNAME}" )
        echo("inside withCredentials; before tagimg ${dockerRegistryTagLatest}")
        tagImage( name: "${artifactId}", tag: "${dockerRegistryTagLatest}", password: ${LOGIN_PASSWORD}, username: ${LOGIN_USERNAME} )
        echo("inside withCredentials; before pushImg ${dockerRegistryTag}")
        pushImage( name: "${artifactId}", tag: "${dockerRegistryTag}", registry: "${dockerHostAndDockerPort}", password: "${LOGIN_PASSWORD}", username: "${LOGIN_USERNAME}" )
        echo("inside withCredentials; before pushimg ${dockerRegistryTagLatest}")
        pushImage( name: "${artifactId}", tag: "${dockerRegistryTagLatest}", registry: "${dockerHostAndDockerPort}", password: "${LOGIN_PASSWORD}", username: "${LOGIN_USERNAME}" )
    }
    echo("Completed [Publish Image] stage steps.")
}
