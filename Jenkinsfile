pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'release/development',
                    credentialsId: 'CredCIC',
                    url: 'https://gitlab.com/devilops1-notion/flex-tooling-backend.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sshagent(credentials: ['ubuntu-ssh']) {
                        sh """
          ssh ubuntu@54.183.204.142 -o StrictHostKeyChecking=no \\
          nohup java -jar ${WORKSPACE}/target/flex-tooling-0.0.1-SNAPSHOT.jar &
        """
                    }
                }
            }
        }
    }
}
