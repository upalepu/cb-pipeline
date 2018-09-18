#!/usr/bin/env groovy

folder('pipelines') {
    displayName("Pipelines")
    description("The base folder containing pipelines managed by JaC.")
}

multibranchPipelineJob('pipelines/cicd-app-pipeline') {
    displayName('CICD Application Pipeline')
    description('Declarative Pipeline for the cicd app demo.')

    branchSources {
        github {
            scanCredentialsId('github')
            repoOwner('upalepu')
            repository('cicd-demo-app')
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            numToKeep(25)
        }
    }
}
