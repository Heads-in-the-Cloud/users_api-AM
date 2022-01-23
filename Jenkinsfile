pipeline {
    agent { label 'aws-ready' }

    environment {
        commit = sh(returnStdout: true, script: "git rev-parse --short=8 HEAD").trim()
        aws_region = "${sh(script:'aws configure get region', returnStdout: true).trim()}"
        aws_ecr_repo = "${sh(script:'aws sts get-caller-identity --query "Account" --output text', returnStdout: true).trim()}"
        repo_name = 'am-users-api'
        jar_name = 'utopia-0.0.1-SNAPSHOT.jar'
        sonarRunner = tool name: 'SonarQubeScanner-4.6.2'
    }

    stages {
        stage('AWS') {
            steps {
                echo 'logging in via AWS client'
                sh 'aws ecr get-login-password --region ${aws_region} | docker login --username AWS --password-stdin ${aws_ecr_repo}.dkr.ecr.${aws_region}.amazonaws.com'
            }
        }
        stage('Package') {
            steps {
                echo 'Cleaning Maven package'
                sh 'docker context use default'
                sh 'mvn -f pom.xml clean package'
            }
        }
        stage('SonarQube') {
            steps {
                echo 'Running SonarQube Quality Analysis'
                withSonarQubeEnv('SonarQube') {
                    sh """
                       ${sonarRunner}/bin/sonar-scanner \
                       -Dsonar.projectKey=AM-users-api \
                       -Dsonar.sources=./src/main/java/com/ss/training/utopia \
                       -Dsonar.java.binaries=./target/classes/com/ss/training/utopia
                    """
                }
                timeout(time: 15, unit: 'MINUTES') {
                    sleep(10)
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Build') {
            steps {
                echo 'Building Docker image'
                sh 'docker build --build-arg jar_name=${jar_name} -t ${repo_name} .'
            }
        }
        stage('Push Images') {
            steps {
                echo 'Tagging images'
                sh 'docker tag ${repo_name}:latest ${aws_ecr_repo}.dkr.ecr.${aws_region}.amazonaws.com/${repo_name}:latest'
                sh 'docker tag ${repo_name}:latest ${aws_ecr_repo}.dkr.ecr.${aws_region}.amazonaws.com/${repo_name}:${commit}'
                echo 'Pushing images'
                sh 'docker push ${aws_ecr_repo}.dkr.ecr.${aws_region}.amazonaws.com/${repo_name}:latest'
                sh 'docker push ${aws_ecr_repo}.dkr.ecr.${aws_region}.amazonaws.com/${repo_name}:${commit}'
            }
        }
        stage('Cleanup') {
            steps {
                echo 'Removing images'
                sh 'docker rmi ${repo_name}:latest'
                sh 'docker rmi ${aws_ecr_repo}.dkr.ecr.us-west-2.amazonaws.com/${repo_name}:latest'
                sh 'docker rmi ${aws_ecr_repo}.dkr.ecr.us-west-2.amazonaws.com/${repo_name}:${commit}'
            }
        }
        stage('ECS Update') {
            steps {
                echo 'Attempting to update ECS Deployment data'
                dir("${AM_RESOURCES_DIRECTORY}") {
                    sh 'jq -M --arg commit "${commit}" \'.users=$commit\' images.json > tmp.$$.json && mv tmp.$$.json images.json'
                }
            }
        }
    }
}
