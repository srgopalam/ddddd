pipeline {
    environment {
        IMAGE = "srgopalam/periodservice"
        REGISTRY = "https://registry.hub.docker.com"
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

                //timeout(time: 10, unit: 'MINUTES') {
                //     waitForQualityGate abortPipeline: true
                //}
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
        stage('test image') {
            steps {
                script {
                    // https://hub.docker.com/r/tutum/hello-world/
                    def container = image.run('-p 8081')
                    def contport = container.port(8081)
                    println image.id + " container is running at host port, " + contport
                    def resp = sh(returnStdout: true,
                            script: """
                                                set +x
                                                curl -w "%{http_code}" -o /dev/null -s \
                                                http://\"${contport}\"
                                                """
                    ).trim()
                    if (resp == "200") {
                        println "periodservice is alive and kicking!"
                        docker.withRegistry("${env.REGISTRY}", 'docker-hub-entree') {
                            image.push("${GIT_HASH}")
                            if ("${env.BRANCH_NAME}" == "master") {
                                image.push("LATEST")
                            }
                        }
                        currentBuild.result = "SUCCESS"
                    } else {
                        println "periodservice deployment failed!"
                        currentBuild.result = "FAILURE"
                    }
                }
            }
        }
    }
}

// No need to occupy a node
stage("Quality Gate") {
    timeout(time: 1, unit: 'HOURS') { // Just in case something goes wrong, pipeline will be killed after a timeout
        def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
        if (qg.status != 'OK') {
            error "Pipeline aborted due to quality gate failure: ${qg.status}"
        }
    }
}
