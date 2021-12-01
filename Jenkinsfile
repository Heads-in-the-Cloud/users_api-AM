pipeline {
    agent any

    stages {
        stage('Initialize') {
            steps {
                echo 'Guarantee integrity'
                sh 'ls'
                sh 'pwd'
            }
        }
        stage('Build') {
            steps {
                echo 'Attempting to build Docker image'
                sh 'docker build -t users-api-am .'
            }
        }
        stage('Test') {
            steps {
                echo 'Run container and test'
            }
        }
        stage('Clean') {
            steps {
                echo 'Removing created resources'
                sh 'docker images'
                sh 'docker rmi users-api-am'
            }
        }
    }
}
