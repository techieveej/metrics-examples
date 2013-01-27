metrics-examples
================

Example projects using Yammer's Metrics library.

To run the examples.

1. Install JBoss 7.1.1
2. Deploy the external HTTP service stub
3. Deploy the metrics demo app
4. POST a customer to:

  http://localhost:8080/metrics-java-demo/api/customer

Content-Type header: application/json

{
    "id": "1",
    "firstName": "John",
    "lastName": "Doe"
}

5. GET http://localhost:8080/metrics-java-demo/api/customer/1
6. Visit the Metrics Admin page:
  
  http://localhost:8080/metrics-java-demo/operations
