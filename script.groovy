def pipeline {
    agent {
        kubernetes {
            yamlFile 'pod.yaml'
        }
    }
    options {
        ansiColor('xterm')
    }
    stages {
        stage('Create Terraform infrastructure') {
            steps {
                container('terraform') {
                    sh "ls -l"
                    sh "cat config.txt"
                    sh "terraform --version"
                }
            }
        }
    }
}
