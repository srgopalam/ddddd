pipeline {
    environment {
        registry = "srgopalam/periodservice"
        registryCredential = 'dockerhub'
    }

    agent any
    stages {
        stage('SCM Checkout') {
            steps {
                git 'https://github.com/srgopalam/periodservice'
            }
        }

        stage('Compile & Test') {

            steps {
                script {
                    def mavenHome = tool name: 'maven360', type: 'maven'
                    sh "${mavenHome}/bin/mvn test"
                }
            }
        }
   stage('Sonarqube') {
    environment {
        scannerHome = tool 'SonarQubeScanner'
    }

    steps {
        withSonarQubeEnv('sonarqube') {
            sh "${scannerHome}/bin/sonar-scanner"
        }

        timeout(time: 10, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
        }
    }
}
        stage('Packaging the app') {

            steps {
                script {
                    def mavenHome = tool name: 'maven360', type: 'maven'
                    sh "${mavenHome}/bin/mvn package"
                }
            }
        
        }

        stage('Jacoco Coverage') {
            steps {
                jacoco(
                        execPattern: 'target/*.exec',
                        classPattern: 'target/classes',
                        sourcePattern: 'src/main/java',
                        exclusionPattern: 'src/test*')
            }
        }
        stage('Building image') {
            steps {
                script {
                    dockerImage = docker.build registry + ":$BUILD_NUMBER"
                }
            }
        }
        stage('Deploy image') {
            steps {
                script {
                    docker.withRegistry('', registryCredential) {
                        dockerImage.push()
                    }
                }
            }
        }
        stage('Remove Unused docker image') {
            steps {
                sh "docker rmi $registry:$BUILD_NUMBER"
            }
        }
    }
}
