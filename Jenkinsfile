pipeline {
    agent any
    stages {
        stage("Build") {
            steps {
                echo "Building running"
            }
        }
        stage("Deploy") {
            steps {
                echo "Deploying code"
            }
        }
        stage("Test") {
            steps {
                bat "mvn clean test"
            }
            post {
                success {
                    publishHTML (target : [allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'ExtentReports',
                        reportFiles: 'ExtentReport*.html',
                        reportName: 'Orange HRM Live Report',
                        reportTitles: 'Orange HRM Live Report'])
            }
            }
        }
    }
}
