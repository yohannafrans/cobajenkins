#!/usr/bin/env groovy

def call(Map param){
	def deliverAgent = libraryResource 'deliver.sh'
	pipeline {
		agent {
			label "agent01"
		}
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
					withEnv(['IPADD='+param.ip])
					sh "sh jenkins/scripts/deliver.sh $param.ip"
				}
			}
		}
	}
}
