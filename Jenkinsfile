pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from GitLab repository
                git branch: 'release/development',
                    credentialsId: 'CredCIC', // Credentials ID for GitLab access
                    url: 'https://gitlab.com/devilops1-notion/flex-tooling-backend.git' // GitLab repository URL
            }
        }

        stage('Build') {
            steps {
                // Build the application using Maven
                sh 'mvn clean install' // Clean and compile the project
            }
        }

        stage('Test') {
            steps {
                // Run unit tests using Maven
                sh 'mvn test' // Execute the tests defined in the project
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Deploy the application to the remote server using SSH
                    sshagent(credentials: ['ubuntu-ssh']) { // SSH credentials for the remote server
                        sh """
                            ssh ubuntu@54.183.204.142 -o StrictHostKeyChecking=no \\
                            nohup java -jar ${WORKSPACE}/target/flex-tooling-0.0.1-SNAPSHOT.jar & // Run the JAR file in the background,StrictHostKeyChecking=no ( used to bypass SSH key verification for the server.)
                        """
                    }
                }
            }
        }
    }
}
