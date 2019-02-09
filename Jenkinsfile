pipeline {
  environment {
    registry = "srgopalam/periodservice"
    registryCredential = 'dockerhub'
  }
  agent any
  stages {
      stage('SCM Checkout'){
    git 'https://github.com/srgopalam/periodservice'
  }
  
  stage('Compile & Test'){
    def mavenHome = tool name: 'maven360', type: 'maven'
    sh "${mavenHome}/bin/mvn test"
  }
  
  stage('Packaging the app'){
    def mavenHome = tool name: 'maven360', type: 'maven'
    sh "${mavenHome}/bin/mvn package"
  }
  
  stage('Jacoco Coverage'){
    jacoco( 
      execPattern: 'target/*.exec',
      classPattern: 'target/classes',
      sourcePattern: 'src/main/java',
      exclusionPattern: 'src/test*')
  }
    stage('Cloning Git') {
      steps {
        git 'https://github.com/gustavoapolinario/microservices-node-example-todo-frontend.git'
      }
    }
    stage('Building image') {
      steps{
        script {
          dockerImage = docker.build registry + ":$BUILD_NUMBER"
        }
      }
    }
    stage('Deploy Image') {
      steps{
        script {
          docker.withRegistry( '', registryCredential ) {
            dockerImage.push()
          }
        }
      }
    }
    stage('Remove Unused docker image') {
      steps{
        sh "docker rmi $registry:$BUILD_NUMBER"
      }
    }
  }
}
