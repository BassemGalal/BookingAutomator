# Booking Automation Framework

## Overview

This is a comprehensive end-to-end automation testing framework designed for booking systems with multiple payment gateway integrations. The framework supports both API and UI testing, with specific focus on payment processing through Stripe, Paymob, and Fawry gateways. It's built using Java with Maven as the build tool, TestNG for test orchestration, and includes continuous integration through GitHub Actions.

## User Preferences

Preferred communication style: Simple, everyday language.

## Recent Changes

### July 15, 2025
- **Fixed Lombok Compilation Issue**: Resolved Java 11 module system incompatibility by excluding Lombok dependency from ExtentReports
- **Updated Maven Configuration**: Added module system exports to compiler and surefire plugins to handle Java 11 compilation
- **Build Success**: Framework now compiles and runs tests successfully (5 tests passed)
- **Implemented Complete API Framework**: Created comprehensive REST API client with endpoints for all booking operations
- **Added POJO Classes**: Implemented request/response classes for all API endpoints with proper Jackson annotations
- **Created E2E Test Suite**: Implemented complete end-to-end test cases covering all payment gateways and coupon scenarios
- **Payment Gateway Integration**: Added UI page objects for Stripe, Paymob, and Fawry payment processing
- **Configuration Management**: Implemented environment-specific property loading with token-based authentication
- **Enhanced Utilities**: Added JsonUtil for test data management, ExtentManager for reporting, and SlackIntegration for notifications
- **CI/CD Pipeline**: Created GitHub Actions workflow for automated testing across multiple environments

## System Architecture

The framework follows a layered architecture pattern with clear separation of concerns:

- **Test Layer**: Contains all test cases and test orchestration
- **Page Object Layer**: UI automation using Selenium WebDriver with Page Object Model
- **API Layer**: REST API testing using RestAssured
- **Utilities Layer**: Common utilities for configuration, reporting, and data management
- **Configuration Layer**: Environment-specific configurations and test data

## Key Components

### API Testing Framework
- **RestAssured Integration**: Uses RestAssured for HTTP API testing
- **POJO Design**: Plain Old Java Objects for request/response mapping
- **Centralized Client**: Single ApiClient.java for managing API interactions
- **Endpoint Management**: Organized API endpoints through constants or enums

### UI Testing Framework
- **Selenium WebDriver**: Browser automation for web interface testing
- **Page Object Model**: Structured approach with BasePage and specific payment gateway pages
- **Driver Management**: Centralized WebDriver instance management
- **Payment Gateway Support**: Dedicated page objects for Stripe, Paymob, and Fawry

### Configuration Management
- **Multi-Environment Support**: Separate property files for staging, alpha, and production
- **Dynamic Configuration**: Runtime environment selection capability
- **Test Data Management**: JSON-based test data storage with utility classes

### Reporting and Logging
- **ExtentReports**: Comprehensive HTML test reporting
- **Log4j2**: Structured logging configuration
- **TestNG Integration**: Built-in TestNG reporting with custom listeners

## Data Flow

1. **Test Initialization**: ConfigManager loads environment-specific properties
2. **Test Data Loading**: JsonUtil reads test data from JSON files
3. **API Testing**: ApiClient executes REST API calls with POJO serialization
4. **UI Testing**: DriverManager initializes browser, Page Objects handle interactions
5. **Payment Processing**: Specific gateway pages handle payment flows
6. **Result Capture**: TestListener captures results for ExtentReports
7. **Cleanup**: Proper resource cleanup and report generation

## External Dependencies

### Core Testing Libraries
- **TestNG**: Test framework and suite management
- **RestAssured**: HTTP API testing library
- **Selenium WebDriver**: Browser automation
- **ExtentReports**: Test reporting framework

### Payment Gateway Integrations
- **Stripe**: Test card data and payment processing flows
- **Paymob**: Payment gateway integration (implementation pending)
- **Fawry**: Payment gateway integration (implementation pending)

### Build and CI/CD
- **Maven**: Dependency management and build automation
- **GitHub Actions**: Continuous integration and automated testing

## Deployment Strategy

### Development Workflow
- **Local Development**: Maven-based build with TestNG execution
- **Version Control**: Git-based source control with GitHub
- **Continuous Integration**: GitHub Actions workflow for automated testing

### Environment Management
- **Staging Environment**: For development and integration testing
- **Alpha Environment**: For pre-production testing
- **Production Environment**: For production validation (limited scope)

### Test Execution Strategy
- **Suite-Based Execution**: TestNG XML configuration for test organization
- **Parallel Execution**: Support for concurrent test execution
- **Cross-Environment Testing**: Dynamic environment switching capability

### Reporting and Monitoring
- **HTML Reports**: ExtentReports for detailed test results
- **Log Files**: Structured logging for debugging and monitoring
- **CI/CD Integration**: Automated report generation in build pipeline

## Key Architectural Decisions

### Framework Choice Rationale
- **Java + Maven**: Chosen for enterprise compatibility and dependency management
- **TestNG over JUnit**: Selected for advanced test configuration and parallel execution
- **RestAssured**: Industry standard for API testing with excellent JSON/XML support
- **Selenium WebDriver**: De facto standard for web UI automation

### Design Pattern Implementation
- **Page Object Model**: Ensures maintainable UI test code and reduces duplication
- **Factory Pattern**: Used in DriverManager for browser instance creation
- **Builder Pattern**: Potential implementation for complex API request construction

### Test Data Strategy
- **JSON Format**: Chosen for readability and easy maintenance of test data
- **Environment Separation**: Allows safe testing across different environments
- **External Configuration**: Enables non-technical users to modify test parameters

This architecture provides a scalable, maintainable foundation for comprehensive booking system testing with robust payment gateway validation capabilities.