# DefaultApi

All URIs are relative to *http://localhost:8080/api*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addToBlacklist**](DefaultApi.md#addToBlacklist) | **PUT** /blacklists | 
[**createProduct**](DefaultApi.md#createProduct) | **PUT** /products | 
[**createRequest**](DefaultApi.md#createRequest) | **PUT** /requests | 
[**createUser**](DefaultApi.md#createUser) | **PUT** /costumers | 
[**getBlacklisted**](DefaultApi.md#getBlacklisted) | **GET** /blacklists | 
[**getCostumerRequests**](DefaultApi.md#getCostumerRequests) | **POST** /requests | 
[**getCostumers**](DefaultApi.md#getCostumers) | **GET** /costumers | 
[**getProducts**](DefaultApi.md#getProducts) | **GET** /products | 
[**hello**](DefaultApi.md#hello) | **GET** /hello | 
[**logMe**](DefaultApi.md#logMe) | **POST** /costumers | 


<a name="addToBlacklist"></a>
# **addToBlacklist**
> Blacklist addToBlacklist(costumerUuid, myId, myPw)



Adds a costumer to the blacklist

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String costumerUuid = "costumerUuid_example"; // String | 
String myId = "myId_example"; // String | 
String myPw = "myPw_example"; // String | 
try {
    Blacklist result = apiInstance.addToBlacklist(costumerUuid, myId, myPw);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#addToBlacklist");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **costumerUuid** | **String**|  |
 **myId** | **String**|  | [optional]
 **myPw** | **String**|  | [optional]

### Return type

[**Blacklist**](Blacklist.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createProduct"></a>
# **createProduct**
> Product createProduct(name, myId, myPw, unitprice, active)



Creates a product

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String name = "name_example"; // String | 
String myId = "myId_example"; // String | 
String myPw = "myPw_example"; // String | 
Float unitprice = 3.4F; // Float | 
Boolean active = true; // Boolean | 
try {
    Product result = apiInstance.createProduct(name, myId, myPw, unitprice, active);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#createProduct");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **name** | **String**|  |
 **myId** | **String**|  | [optional]
 **myPw** | **String**|  | [optional]
 **unitprice** | **Float**|  | [optional]
 **active** | **Boolean**|  | [optional]

### Return type

[**Product**](Product.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createRequest"></a>
# **createRequest**
> Request createRequest(request)



Creates a new request for a costumer

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
RequestPut request = new RequestPut(); // RequestPut | 
try {
    Request result = apiInstance.createRequest(request);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#createRequest");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **request** | [**RequestPut**](RequestPut.md)|  |

### Return type

[**Request**](Request.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createUser"></a>
# **createUser**
> Costumer createUser(name, username, password, creditcardnumber, creditcarddate)



Creation of new user

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String name = "name_example"; // String | 
String username = "username_example"; // String | 
String password = "password_example"; // String | 
String creditcardnumber = "creditcardnumber_example"; // String | 
String creditcarddate = "creditcarddate_example"; // String | 
try {
    Costumer result = apiInstance.createUser(name, username, password, creditcardnumber, creditcarddate);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#createUser");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **name** | **String**|  |
 **username** | **String**|  |
 **password** | **String**|  |
 **creditcardnumber** | **String**|  |
 **creditcarddate** | **String**|  |

### Return type

[**Costumer**](Costumer.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getBlacklisted"></a>
# **getBlacklisted**
> Blacklists getBlacklisted()



Returns a collection

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
try {
    Blacklists result = apiInstance.getBlacklisted();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#getBlacklisted");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Blacklists**](Blacklists.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getCostumerRequests"></a>
# **getCostumerRequests**
> Consult getCostumerRequests(costumerUuid, pin)



Returns a request collection of a costumer given its uuid and pin

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String costumerUuid = "costumerUuid_example"; // String | 
String pin = "pin_example"; // String | 
try {
    Consult result = apiInstance.getCostumerRequests(costumerUuid, pin);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#getCostumerRequests");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **costumerUuid** | **String**|  |
 **pin** | **String**|  |

### Return type

[**Consult**](Consult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getCostumers"></a>
# **getCostumers**
> Costumers getCostumers()



Returns an array of users

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
try {
    Costumers result = apiInstance.getCostumers();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#getCostumers");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Costumers**](Costumers.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getProducts"></a>
# **getProducts**
> Products getProducts()



Returns an array of products

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
try {
    Products result = apiInstance.getProducts();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#getProducts");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Products**](Products.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="hello"></a>
# **hello**
> HelloWorldResponse hello(name)



Returns &#39;Hello&#39; to the caller

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String name = "name_example"; // String | The name of the person to whom to say hello
try {
    HelloWorldResponse result = apiInstance.hello(name);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#hello");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **name** | **String**| The name of the person to whom to say hello | [optional]

### Return type

[**HelloWorldResponse**](HelloWorldResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="logMe"></a>
# **logMe**
> Costumer logMe(username, password)



Login

### Example
```java
// Import classes:
//import io.swagger.client.api.DefaultApi;

DefaultApi apiInstance = new DefaultApi();
String username = "username_example"; // String | 
String password = "password_example"; // String | 
try {
    Costumer result = apiInstance.logMe(username, password);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DefaultApi#logMe");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **username** | **String**|  |
 **password** | **String**|  |

### Return type

[**Costumer**](Costumer.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

