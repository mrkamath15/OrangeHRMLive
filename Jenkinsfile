pipeline {
    agent any
    stages {
        stage("Build") {
            steps {
                echo "Building"
            }
        }
        stage("Deploy") {
            steps {
                echo "Deploying"
            }
        }
        stage("Test") {
            steps {
                bat "mvn clean test"
            }
        }
    }
}