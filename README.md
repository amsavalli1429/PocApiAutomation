
BookStore API Automation Project

This project automates REST API testing for a BookStore application using Java, RestAssured, and TestNG. It integrates Extent Reports for test reporting, follows a CI/CD pipeline using Jenkins, and is version-controlled with Git on GitHub.



| Component         | Version                        | Purpose                                            |
|------------------|--------------------------------|----------------------------------------------------|
| Java           | 21                             | Core programming language                          |
| Maven          | Latest (compatible with Java 21) | Project build lifecycle & dependency management    |
| TestNG         | 7.9.0                         | Test execution and suite configuration             |
| RestAssured    | 5.4.0                          | Simplified API testing using Java                  |
| Extent Reports | Latest                         | Generate rich and interactive test reports         |
| Jenkins        | Latest                         | CI/CD pipeline orchestration                       |
| Git & GitHub    | -                              | Version control and remote code repository hosting |


why use Extend Reports

Extent Reports offers a clean and visually appealing dashboard with charts, summaries, and timelines that make your test results easy to   understand.
It logs every test step with clear status labels like pass, fail, or skip, along with timestamps and custom messages.
You can attach screenshots, videos, or logs to help debug failed test cases more easily.
Tests can be grouped and filtered by tags, authors, or devices, making it easier to focus on specific parts of your test suite.
With the Spark Adapter, you can track trends and historical test data to monitor long-term performance.
Extent Reports works well with CI/CD tools like Jenkins, so you can access your test reports automatically after each build.
Why TestNG is a Better Fit
Extent Reports integrates seamlessly with TestNG for generating detailed and interactive test reports.
Using TestNG with RestAssured  gives you more control over test grouping, parallel execution, and test flow management.
TestNG provides built-in support for test dependencies, retry logic, and flexible setup/teardown configurations, making it ideal for robust API test automation.

Prerequisites
Java 21 installed and added to PATH
Maven installed and added to PATH
Extent Reports library added to your project dependencies (via Maven)
Jenkins installed for CI/CD execution and publishing test reports


How to Set Up and Run BookStore API Automation
Create or Clone the Project
Create a new Maven project for automation, or
Clone the existing GitHub repo if it’s already available.

Fork and Clone the Dev Repository
Fork the development repository provided.
Clone your fork to your local machine.
Follow the setup steps mentioned in the README.md of the Dev repo to get everything ready.

API Endpoints Covered in This Automation
POST /signup
Create a new user account by registering with the BookStore.
POST /login
Authenticate an existing user and receive a token for authorized access.
POST /books
Add a brand-new book to the store’s collection.
PUT /books/{id}
Modify details of a specific book identified by its unique ID.
GET /books/{id}
Retrieve detailed information for a single book using its ID.
GET /books
Fetch the complete list of all available books.
DELETE /books/{id}
Remove a book from the store by specifying its ID.
Execute the automation suite by running the testng.xml file. This will run all the test scenarios, providing thorough test coverage and clear visibility into the results.
Once execution is complete, Extent Reports will be generated. You can find the report in the test-output/ExtentReports folder and view the detailed HTML report there.


CI/CD Integration

Jenkins installed with the following plugins (can be installed via Jenkins UI):
 GitHub

 Pipeline

 Maven

Extent Reports library added as a dependency in your project (no Jenkins plugin needed for Extent Reports)

STEPS to be Followed for CI/CD (Development & Testing Environment)
Add Jenkinsfile in Dev Repo

This Jenkinsfile will build the dev code and trigger the QA automation job.

pipeline {
    agent any
    stages {
        stage('Build Dev') {
            steps {
                echo 'Build or test dev code here'
               
  }
    }
        stage('Trigger QA Automation') {
            steps {
                build job: 'QA-Repo'
            }
        }
    }
}


Jenkinsfile in QA Repo

pipeline {
    agent any
    tools {
        maven 'maven 'Maven_3.8.5'
    }
    stages {
        stage('Checkout') {
            steps {
                git url: '<gitUrl>', branch: '<BranchName>'
            }
        }
        stage('Build and Test') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('Publish Extent Report') {
            steps {
                // Assuming Extent Reports are generated during test execution and saved in test-output/ExtentReports
                archiveArtifacts 'test-output/ExtentReports/**'
                // Optionally, you can publish HTML reports using Jenkins HTML Publisher Plugin:
                publishHTML([allowMissing: false,
                             alwaysLinkToLastBuild: true,
                             keepAll: true,
                             reportDir: 'test-output/ExtentReports',
                             reportFiles: 'ExtentReport.html',
                             reportName: 'Extent Report'])
            }
        }
    }
}


Setup Jenkins
Launch Jenkins locally (via your preferred method).
Install required plugins:
GitHub
Pipeline
Maven
HTML Publisher Plugin (for Extent Reports)
Create two Pipeline Jobs:
Dev Job (builds dev repo)
QA Job (runs automation and publishes Extent Reports)
Configure GitHub repository URLs in the respective jobs.

Workflow Summary
When a commit is pushed to the Dev repo, the Dev Jenkins job triggers.
Upon successful Dev build, it triggers the QA automation Jenkins job.
The QA job runs tests, generates Extent Reports, and archives/displays them in Jenkins.
This cycle repeats with every Dev repo commit, ensuring CI/CD automation with clear test reporting.


