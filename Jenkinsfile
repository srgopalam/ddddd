node{
  stage('SCM Checkout'){
    git 'https://github.com/srgopalam/periodservice'
  }
  
  stage('Compile-Package'){
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
}
