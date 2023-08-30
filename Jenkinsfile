pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                checkout scm
                sh 'mvn clean verify'
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
                sh 'mvn jar:jar deploy:deploy'
            }
        }
    }
    post {
        success {
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
            jacoco execPattern: '**/target/*.exec'
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            cleanWs()
        }
        failure {
            cleanWs()
        }
    }
}