#!/usr/bin/env groovy
package vars

def call() {
    [containerTemplate(name: 'terraform', image: 'hashicorp/terraform:1.0.6', command: 'sleep', args: '99d'),
    containerTemplate(name: 'aws', image: 'amazon/aws-cli:2.4.12', command: 'sleep', args: '99d')]
}
