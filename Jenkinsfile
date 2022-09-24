pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                checkout scm
                sh 'mvn clean install'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
                nexusArtifactUploader credentialsId: 'jenkins.jware.co',
                                      groupId: 'foundation.identity.keri',
                                      nexusUrl: 'nexus.jurassicware.com:8443',
                                      nexusVersion: 'nexus3', protocol: 'https',
                                      repository: 'jware-snapshots',
                                      version: '0.0.1-SNAPSHOT'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
    }
}