pipeline {
    agent any
    stages {
        stage("Build") {
            steps {
                echo "Building test"
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
