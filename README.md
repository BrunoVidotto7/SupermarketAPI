# Supermarket API

<ul>
  <li>Java - 17</li>
  <li>Spring boot - 2.7.5</li>
  <li>H2 in memory database</li>
  <li>Running in port 8080</li>
</ul>

---

## Endpoints

<ul>
  <li>/api/baskets - GET - list all baskets</li>
  <li>/api/baskets/create - POST - create a new basket (required a form as body)</li>
  <li>/api/baskets/add/{id} - PUT - add items into a already existent basket (required a form as body)</li>
  <li>/api/baskets/checkout/{id} - PUT - change basket status to CHECKOUT</li>
  <li>/api/baskets/pay/{id} - PUT - change basket status to PAID</li>
  <li>/api/baskets/{id} - GET - get basket using id</li><br>

  <li>/api/products - GET - list all products</li>
  <li>/api/products/{productId} - GET - get product using id</li><br>
</ul>

---
## Example
Start the running the class *SupermarketApplication.java*.\
Create a basket with 4 Amazing pizzas, 2 Amazing burger and 1 Amazing salad.\
Send a POST request to http://localhost:8080/api/baskets/create passing the following JSON as body parameter:

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
Add 5 boring fries to this existent basket sending a PUT request to http://localhost:8080/api/baskets/add/1 passing the following JSON as body parameter:
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
Checkout the basket sending a PUT request to http://localhost:8080/api/baskets/checkout/1

Check the basket informations sending a GET request to  http://localhost:8080/api/baskets/1<br>
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




