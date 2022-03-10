// this is jfrog jenkins intigration
node {
    def server
    def rtMaven = Artifactory.newMavenBuild()
    def buildInfo
    
    stage('Clone') {
        git url: 'https://github.com/madhu101298/project-examples.git'
    }
    
        stage ('Artifactory configaration') {
        // Obtain an Artifactory server instance, defined in Jenkins --> Manage Jenkins --> Configure System:
        Server = Artifactory.server 'JFROG_ARTIFACTORY'
        
        //Tool name from jenkins Configuration
        rtMaven.tool = 'M3'
        rtMaven.deployer releaseRepo: 'jenkins-int', snapshotRepo: 'jenkins-int', server:server
        rtMaven.resolver releaseRepo: 'madhu-maven', snapshotRepo: 'madhu-maven', server:server
        buildInfo = Artifactory.newBuildInfo()
    }
    
    stage ('Exec Maven'){
        rtMaven.run pom: 'maven-examples/maven-example/pom.xml', goals:'clean install', buildInfo:buildInfo
    }
    
    stage ('Publish build Info') {
        server publishBuildInfo buildInfo
    }
}    
