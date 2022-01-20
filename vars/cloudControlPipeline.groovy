#!/usr/bin/env groovy

def call(String name = 'human') {
    container('terraform') {
        echo "Hello, ${name}!"
        sh("terraform -version")
        terraform -version
    }
}