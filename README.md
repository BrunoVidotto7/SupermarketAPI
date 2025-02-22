# Supermarket API

&nbsp;
[![Continuos Integration with Github](https://github.com/BrunoVidotto7/SupermarketAPI/actions/workflows/docker-publish.yml/badge.svg?branch=main)](https://github.com/BrunoVidotto7/SupermarketAPI/actions/workflows/docker-publish.yml)
[![codecov](https://codecov.io/gh/BrunoVidotto7/SupermarketAPI/branch/main/graph/badge.svg?token=3UB2CRFBWV)](https://codecov.io/gh/BrunoVidotto7/SupermarketAPI)

---

<ul>
  <li>Java - 17</li>
  <li>Spring boot - 2.7.5</li>
  <li>H2 in memory database</li>
  <li>Running in port 8080</li>
</ul>

---

## Endpoints


```/api/baskets``` - **GET** - list all baskets\
```/api/baskets/create``` - **POST** - create a new basket (required a form as body)\
```/api/baskets/add/{id}``` - **PATCH** - add items into a already existent basket (required a form as body)\
```/api/baskets/checkout/{id}``` - **PATCH** - change basket status to CHECKOUT\
```/api/baskets/pay/{id}``` - **PATCH** - change basket status to PAID\
```/api/baskets/{id}``` - **GET** - get basket using id<br>

  ```/api/products``` - **GET** - list all products\
  ```/api/products/{productId}``` - **GET** - get product using id<br>

---
## Example
Start the running the class *SupermarketApplication.java*.\
Create a basket with 4 Amazing pizzas, 2 Amazing burger and 1 Amazing salad sending a **POST** request to http://localhost:8080/api/baskets/create passing the following JSON as body parameter:

```json
{
  "productBaskets": [
    {
      "productId": "Dwt5F7KAhi",
      "quantity": 4
    },
    {
      "productId": "PWWe3w1SDU",
      "quantity": 2
    },
    {
      "productId": "C8GDyLrHJb",
      "quantity": 1
    }
  ]
} 
```
Add 5 boring fries to this existent basket sending a **PATCH** request to http://localhost:8080/api/baskets/add/1 passing the following JSON as body parameter:
```json
{
  "productBaskets": [
    {
      "productId": "4MB7UfpTQs",
      "quantity": 5
    }
  ]
} 
```
Checkout the basket sending a **PATCH** request to http://localhost:8080/api/baskets/checkout/1

Check the basket informations sending a **GET** request to  http://localhost:8080/api/baskets/1<br>
See the following response:
```json
{
    "basketProducts": [
        {
            "productName": "Amazing Pizza!",
            "quantity": 4
        },
        {
            "productName": "Amazing Burger!",
            "quantity": 2
        },
        {
            "productName": "Amazing Salad!",
            "quantity": 1
        },
        {
            "productName": "Boring Fries!",
            "quantity": 5
        }
    ],
    "id": 1,
    "status": "CHECKOUT",
    "expectedTotals": {
        "rawTotal": 78.88,
        "totalPromos": 18.47,
        "totalPayable": 60.41
    }
}
```







