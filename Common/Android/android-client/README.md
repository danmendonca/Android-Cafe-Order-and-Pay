# swagger-android-client

## Requirements

Building the API client library requires [Maven](https://maven.apache.org/) to be installed.

## Installation

To install the API client library to your local Maven repository, simply execute:

```shell
mvn install
```

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn deploy
```

Refer to the [official documentation](https://maven.apache.org/plugins/maven-deploy-plugin/usage.html) for more information.

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-android-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your project's build file:

```groovy
compile "io.swagger:swagger-android-client:1.0.0"
```

### Others

At first generate the JAR by executing:

    mvn package

Then manually install the following JARs:

* target/swagger-android-client-1.0.0.jar
* target/lib/*.jar

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java

import io.swagger.client.api.DefaultApi;

public class DefaultApiExample {

    public static void main(String[] args) {
        DefaultApi apiInstance = new DefaultApi();
        BlacklistParam blacklist = new BlacklistParam(); // BlacklistParam | 
        try {
            Blacklist result = apiInstance.addToBlacklist(blacklist);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling DefaultApi#addToBlacklist");
            e.printStackTrace();
        }
    }
}

```

## Documentation for API Endpoints

All URIs are relative to *http://localhost:8080/api*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*DefaultApi* | [**addToBlacklist**](docs/DefaultApi.md#addToBlacklist) | **PUT** /blacklists | 
*DefaultApi* | [**createProduct**](docs/DefaultApi.md#createProduct) | **PUT** /products | 
*DefaultApi* | [**createRequest**](docs/DefaultApi.md#createRequest) | **PUT** /requests | 
*DefaultApi* | [**createUser**](docs/DefaultApi.md#createUser) | **PUT** /costumers | 
*DefaultApi* | [**getBlacklisted**](docs/DefaultApi.md#getBlacklisted) | **GET** /blacklists | 
*DefaultApi* | [**getCostumerRequests**](docs/DefaultApi.md#getCostumerRequests) | **POST** /requests | 
*DefaultApi* | [**getCostumers**](docs/DefaultApi.md#getCostumers) | **GET** /costumers | 
*DefaultApi* | [**getProducts**](docs/DefaultApi.md#getProducts) | **GET** /products | 
*DefaultApi* | [**hello**](docs/DefaultApi.md#hello) | **GET** /hello | 
*DefaultApi* | [**logMe**](docs/DefaultApi.md#logMe) | **POST** /costumers | 


## Documentation for Models

 - [Blacklist](docs/Blacklist.md)
 - [BlacklistParam](docs/BlacklistParam.md)
 - [Blacklists](docs/Blacklists.md)
 - [Consult](docs/Consult.md)
 - [Costumer](docs/Costumer.md)
 - [Costumers](docs/Costumers.md)
 - [ErrorResponse](docs/ErrorResponse.md)
 - [HelloWorldResponse](docs/HelloWorldResponse.md)
 - [LoginParam](docs/LoginParam.md)
 - [PinLoginParam](docs/PinLoginParam.md)
 - [Product](docs/Product.md)
 - [ProductParam](docs/ProductParam.md)
 - [Products](docs/Products.md)
 - [RegisterParam](docs/RegisterParam.md)
 - [Request](docs/Request.md)
 - [RequestParam](docs/RequestParam.md)
 - [Requestline](docs/Requestline.md)
 - [RequestlineParam](docs/RequestlineParam.md)
 - [Voucher](docs/Voucher.md)
 - [VoucherParam](docs/VoucherParam.md)


## Documentation for Authorization

All endpoints do not require authorization.
Authentication schemes defined for the API:

## Recommendation

It's recommended to create an instance of `ApiClient` per thread in a multithreaded environment to avoid any potential issue.

## Author



