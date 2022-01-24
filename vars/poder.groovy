#!/usr/bin/env groovy
package vars

def String pod() {
    def podTemplate = '''
apiVersion: v1
kind: Pod
spec:
  containers:
    - name: terraform
      image: hashicorp/terraform:1.0.6
      command:
        - sleep
      args:
        - "99d"
      volumeMounts:
        - name: data
          mountPath: /data
    - name: ansible
      image: ansible/ansible-runner:1.4.7
      command:
        - sleep
      args:
        - "99d"
      volumeMounts:
        - name: data
          mountPath: /data
    - name: aws
      image: amazon/aws-cli:2.4.12
      command:
        - sleep
      args:
        - "99d"
      volumeMounts:
        - name: data
          mountPath: /data
  volumes:
    - name: data
      emptyDir: {}'''
    return podTemplate
}
