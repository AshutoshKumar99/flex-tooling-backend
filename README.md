This Jenkins pipeline automates a typical CI/CD process for a Java-based application.
The pipeline consists of four main stages: Checkout, Build, Test, and Deploy.
In the Checkout stage, it retrieves code from the 'release/development' branch of the 'flex-tooling-backend' Github repository.
The Build stage uses Maven commands to build the application.
The Test stage executes unit tests to ensure quality.
Finally, the Deploy stage leverages SSH to deploy the application to a server at IP address 54.183.204.142, running it in the background using nohup.
