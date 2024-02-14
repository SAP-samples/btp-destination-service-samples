## Table of Contents

* [Overview](#overview)
* [Description](#description)
* [Requirements](#requirements)
    * [Local build](#local-build)
    * [Environment](#environment)
    * [Deploy](#deploy)
* [Installation and Usage](#installation-and-usage)
  * [Build and Deploy](#build-and-deploy)
  * [Postman Usage](#postman-usage)
  * [Client-based Security](#client-based-security)
  * [How to get an access token](#how-to-get-an-access-token)
      * [Trust and Roles pre-requisites](#trust-and-roles-pre-requisites)
      * [Get the access token](#get-the-access-token)
      * [Endpoints Description](#endpoints-description)
          * [GET /destinations (Read Destinations)](#get-destinations)
          * [POST /destinations/migrate (Migrate Destinations)](#post-destinationsmigrate)
          * [GET /destinations/json (Prepare config.json with NEO destination properties)](#get-destinationsjson)
* [Known Issues](#known-issues)
* [How to obtain support](#how-to-obtain-support)
* [Contributing](#contributing)

# Overview

<!-- Please include descriptive title -->

<!--- Register repository https://api.reuse.software/register, then add REUSE badge:
[![REUSE status](https://api.reuse.software/badge/github.com/SAP-samples/REPO-NAME)](https://api.reuse.software/info/github.com/SAP-samples/REPO-NAME)
-->
This is a simple example of a Java project that contains
the following functionality:

* Read Neo Destinations (via Connectivity API)
* Migration of Neo Destinations to CF Destinations

## Description

This Java project is a straightforward example that focuses on two main functionalities: reading Neo Destinations
through the Connectivity API and migrating Neo Destinations to CF Destinations. It's designed to be simple and
efficient, making it suitable for anyone looking to handle data connectivity and migration in Java.

## Requirements

#### Local build

To build the application locally, the following prerequisites must be met:

* Java 8 or higher installed
* Maven 3.8.6 or higher installed

#### Environment

To deploy the application to the Neo environment, the following prerequisites must be met:

* SAP BTP Neo account (trial or productive)
* Account User with Administrator role

#### Deploy

To deploy the application to the Neo environment, the following prerequisites must be met:

* NEO Console Client (Neo CLI) with SDK Version 4.47.11 or higher installed

## Installation and Usage

### Build and Deploy

Just build and deploy the application to the Neo environment using
the Neo CLI:

Build:

```shell
mvn clean package
```

Deploy:

```shell
neo deploy --host <neo_host> --account <neo_account> --application neodestinationdemo --source-path target/dest-1.0-SNAPSHOT.war --user <your_user>
```

### Postman Usage

The `postman` folder contains both Postman Collection and Postman Environment.
So it can be easily imported into Postman. After importing the collection, all environment variables should be set
based on the deployed application and user credentials.

Just run the `Get Token` request to get the access token, it will be automatically saved in the environment variable
so further requests will use it.

### Client-based security

The application is secured for any authenticated SAP BTP user of the account or can be accessed via OAuth Clients.

### How to get an access token

#### Trust pre-requisites

> **Note:** The following steps are require the already deployed app.

1. Go to the `SAP BTP NEO Cockpit`/`Security`/`OAuth`/`Clients` tab
2. Create a new client  (Authorization Grant is `Client Credentials`, Subscription is your application name), provide
   and remember `client secret`
   and `client id` attributes.

#### Get the access token

1. Get the `Token Endpoint` from the `Security`/`OAuth`/`Branding` tab
2. Get the `client id` and `client secret` from the `Security`/`OAuth`/`Clients` tab
3. Make an API call to the `Token Endpoint` via Postman Collection (`postman` folder) and Get Token request or via
   custom API call with the following parameters:

[POST] /<token_endpoint>

**Body** (x-www-form-urlencoded)

| Name         | Required |  Type  | Value                           |
|:-------------|:--------:|:------:|---------------------------------|
| `grant_type` | required | string | `password`                      |
| `username`   | required | string | `<username_with_assigned_role>` |
| `password`   | required | string | `<username_password>`           |

OR

**Body** (x-www-form-urlencoded)

| Name         | Required |  Type  | Value                 |
|:-------------|:--------:|:------:|-----------------------|
| `grant_type` | required | string | `client_credentials`  |
| `username`   | required | string | `<neo_client_id>`     |
| `password`   | required | string | `<neo_client_secret>` |

**Headers**

| Name                                   | Required |  Type  | Description                                                                                                  |
|:---------------------------------------|:--------:|:------:|--------------------------------------------------------------------------------------------------------------|
| `Authorization: Basic <base64encoded>` | required | string | Where the `<base64encoded>` is the Base 64 Encoded string: `<neo_oauth_client_id>:<neo_oauth_client_secret>` |

### Endpoints Description

After the deployment is completed, the resulted url of the deployed application
will look like this: `https://neodestinationdemotbxg6xlzhd.eu2.hana.ondemand.com/dest-1.0-SNAPSHOT/`

#### GET /destinations

Get all destinations from the Neo environment for the specified account name
taken in the GET query parameters.

**Parameters**

| Name          | Required |  Type  | Description                                |
|:--------------|:--------:|:------:|--------------------------------------------|
| `accountName` | required | string | The Neo account, for example: `tbxg6xlzhd` |

**Headers**

| Name                            | Required |  Type  | Description                      |
|:--------------------------------|:--------:|:------:|----------------------------------|
| `Authorization: Bearer <token>` | required | string | Previously received access token |

**Response** (example)

List of destinations with all parameters and properties fetched via Connectivity API.

```json
{
  "test_saml_bearer_destination": {
    "configurationLevel": "TENANT",
    "keyStore": null,
    "trustStore": {},
    "tokenServiceKeyStore": null,
    "allProperties": {
      "Type": "HTTP",
      "audience": "test",
      "clientKey": "test",
      "Authentication": "OAuth2SAMLBearerAssertion",
      "tokenServiceUser": "test",
      "tokenServiceURL": "http://test.com",
      "ProxyType": "Internet",
      "URL": "http://demo0474470.mockable.io",
      "Name": "test_saml_bearer",
      "tokenServicePassword": "test"
    }
  }
}
```

#### POST /destinations/migrate

**Parameters**

| Name                      | Required |  Type  | Description                                                                                                                   |
|:--------------------------|:--------:|:------:|-------------------------------------------------------------------------------------------------------------------------------|
| `accountName`             | required | string | The Neo account, for example: `tbxg6xlzhd`                                                                                    |
| `destinationUrl`          | required | string | The CF Destination Service URL, for example <br/>https://destination-configuration.cfapps.eu10.hana.ondemand.com              |
| `destinationAuthUrl`      | required | string | The CF Destination Service Auth URL, usually UAA Url, for example <br/>https://migrate.authentication.eu10.hana.ondemand.com` |                                            |
| `destinationClientId`     | required | string | The CF Destination Service Client Id                                                                                          |                                                                                         |
| `destinationClientSecret` | required | string | The CF Destination Service Client Secret                                                                                      |

**Headers**

| Name                            | Required |  Type  | Description                      |
|:--------------------------------|:--------:|:------:|----------------------------------|
| `Authorization: Bearer <token>` | required | string | Previously received access token |

> **Note:** Currently the application supports only the following authentication types to be migrated:
> * `OAuth2SAMLBearerAssertion`
> * `OAuth2ClientCredentials`
> * `BasicAuthentication`

**Response** (example)

Result messages and migration details about each destination.

```json
{
  "result": "Migration completed",
  "details": [
    {
      "destinationName": "test_saml_bearer",
      "result": "Success"
    },
    {
      "destinationName": "webide_di",
      "result": "Destination authentication type is not supported"
    },
    {
      "destinationName": "Northwind",
      "result": "Destination authentication type is not supported"
    },
    {
      "destinationName": "Mockable",
      "result": "Destination authentication type is not supported"
    },
    {
      "destinationName": "documentmanagement",
      "result": "Success"
    },
    {
      "destinationName": "test_client_credentials",
      "result": "Success"
    },
    {
      "destinationName": "ESPMSSO",
      "result": "Destination authentication type is not supported"
    }
  ]
}
```

#### GET /destinations/json

This endpoint prepares the config.json compatible structure with NEO destinations properties for further MTA deployment.

**Headers**

| Name                            | Required |  Type  | Description                                                                                                                                                                                 |
|:--------------------------------|:--------:|:------:|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Authorization: Bearer <token>` | required | string | Previously received access token                                                                                                                                                            |
| `X-Account-Name`                | required | string | Account name                                                                                                                                                                                |
| `X-Subaccount-Destinations`     | required | string | Comma-separated (space and case-sensitive) list of destinations to be added to JSON response at the **subaccount** destinations level, for example: destination_1,destination_2             |
| `X-Instance-Destinations`       | required | string | Comma-separated (space and case-sensitive) list of destinations to be added to JSON response at the **instance** destinations level, for example: destination_1,destination_3,destination_4 |

**Response** (example)

JSON configuration with NEO destination properties divided by specified destination levels.

```json
{
  "HTML5Runtime_enabled": true,
  "init_data": {
    "instance": {
      "existing_destinations_policy": "ignore",
      "destinations": [
        {
          "KeyStorePassword": "",
          "Type": "HTTP",
          "KeyStoreLocation": "mocked.p12",
          "Authentication": "ClientCertificateAuthentication",
          "ProxyType": "Internet",
          "URL": "https://test.com",
          "Name": "test_client_certificate"
        },
        {
          "Type": "HTTP",
          "audience": "test",
          "clientKey": "test",
          "Authentication": "OAuth2SAMLBearerAssertion",
          "tokenServiceUser": "test",
          "tokenServiceURL": "http://test.com",
          "ProxyType": "Internet",
          "URL": "http://mockable.io",
          "Name": "test_saml_bearer",
          "tokenServicePassword": "test"
        }
      ],
      "existing_certificates_policy": "ignore"
    },
    "subaccount": {
      "existing_destinations_policy": "ignore",
      "destinations": [
        {
          "KeyStorePassword": "",
          "Type": "HTTP",
          "KeyStoreLocation": "mocked.p12",
          "Authentication": "ClientCertificateAuthentication",
          "ProxyType": "Internet",
          "URL": "https://test.com",
          "Name": "test_client_certificate"
        }
      ],
      "existing_certificates_policy": "ignore"
    }
  }
}
```

## Known Issues

<!-- You may simply state "No known issues. -->
No known issues.

## How to obtain support

[Create an issue](https://github.com/SAP-samples/<repository-name>/issues) in this repository if you find a bug or have
questions about the content.

For additional support, [ask a question in SAP Community](https://answers.sap.com/questions/ask.html).

## Contributing

If you wish to contribute code, offer fixes or improvements, please send a pull request. Due to legal reasons,
contributors will be asked to accept a DCO when they create the first pull request to this project. This happens in an
automated fashion during the submission process. SAP
uses [the standard DCO text of the Linux Foundation](https://developercertificate.org/).

## License

Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved. This project is licensed under the Apache
Software License, version 2.0 except as noted otherwise in the [LICENSE](LICENSE) file.
[![REUSE status](https://api.reuse.software/badge/github.com/SAP-samples/btp-destination-service-samples)](https://api.reuse.software/info/github.com/SAP-samples/btp-destination-service-samples)
