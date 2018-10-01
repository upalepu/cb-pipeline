#!/usr/bin/env groovy

def setupkubecfg() {
    // The file setupkubecfg.sh contains the logic to create a Config context for the cluster
    // we are interested in. This function should be called before any calls to 
    // kubectl.  
    sh("/opt/bin/setupkubecfg.sh")
}

def apply(namespace, externalPort, templateFileName='kubernetes-app-config-template.yml') {
    def pomInfo = readMavenPom()
    def artifactId = pomInfo.artifactId
    def version = pomInfo.version
    def dockerImageTag = "${artifactId}:${version}"
//    def dockerHostAndDockerPort = "${NEXUS_HOSTNAME}:${NEXUS_SERVICE_PORT_DOCKER}"
    def dockerNexusHostAndPort = "${DOCKER_NEXUS_HOSTNAME}"
//    def imageTag = "${dockerHostAndDockerPort}/${dockerImageTag}"
    def imageTag = "${dockerNexusHostAndPort}/${dockerImageTag}"
    def appConfigFileName = "${namespace}-config.yml"

    def appConfigFileTemplate = readFile(file: templateFileName)
    def appConfigFileContent = appConfigFileTemplate.replaceAll('@@ARTIFACT_ID@@', artifactId)
                                                    .replaceAll('@@IMAGE_TAG@@', imageTag)
                                                    .replaceAll('@@NAMESPACE@@', namespace)
                                                    .replaceAll('@@EXTERNAL_PORT@@', externalPort)

    writeFile(file: appConfigFileName, text: appConfigFileContent)

    sh("/opt/bin/setupkubecfg.sh")
    sh("kubectl apply -f ${appConfigFileName}")
}

def testContainer(namespace) {
  def pomInfo = readMavenPom()
  def artifactId = pomInfo.artifactId

  sleep(30)
  //def clusterIPCommand = "kubectl get services --namespace=${namespace} -o jsonpath='{.spec.clusterIP}' ${artifactId}"
  //def clusterIP = sh(script: clusterIPCommand, returnStdout: true).trim()
  def portCommand = "kubectl get services --namespace=${namespace} -o jsonpath=\'{.spec.ports[?(@.name==\"http\")].port}\' ${artifactId}"
  def port = sh(script: portCommand, returnStdout: true).trim()

  //def healthCheckEndPoint = "http://${clusterIP}:${port}/actuator/health"
  def healthCheckEndPoint = "http://${artifactId}.${namespace}.svc.cluster.local:${port}/actuator/health"
  def healthCheckCommand = "curl -s -o /dev/null -w \"%{http_code}\" ${healthCheckEndPoint}"
  def statusCode = sh(script: healthCheckCommand, returnStdout: true).trim()
  if (statusCode != '200') {
      error("Health check failed for endpoint [${healthCheckEndPoint}].")
  }
}
