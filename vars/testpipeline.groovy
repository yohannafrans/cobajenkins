#!/usr/bin/env groovy

def call(){
	def remote = [:]
	remote.name = 'target'
	remote.host = 'http://192.168.0.52:8080/'
	remote.user = 'root'
	remote.password = 'yohannafrans'
	remote.allowAnyHosts = true
	pipeline {
		agent any
		stages {
			stage('Build') {
				steps {
					sh 'mvn -B -DskipTests clean package'
				}
			}
			stage('Test') {
				steps {
					sh 'mvn test'
				}
				post {
					always {
						junit 'target/surefire-reports/*.xml'
					}
				}
			}
			stage('Deliver') {
				steps {
					sh './jenkins/scripts/deliver.sh'
				}
			}
		}
	}
}
