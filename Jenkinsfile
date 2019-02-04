node{
  stage('SCM Checkout'){
    git 'https://github.com/srgopalam/periodservice'
  }
  
  stage('Compile-Package'){
    def mavenHome = tool name: '', type: 'maven'
    sh "${mavenHome}/bin/mvn package"
  }
}
