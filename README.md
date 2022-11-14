# Online Book Store

This application can be used by buyers to search for a variety of books in an online market and make purchases from
there. Sellers can use this application to set up stores where their books are sold on the market.

### Authors

CS 18000, section 001
* Visv Shah
* Katya Teodorovich
* Megan Long
* Jayden Deason
* Griffin Chittenden

## Executing program

* How to run the program
* Compile all the files

```
javac "filenamehere"
```

* Run the main method

```
java main
```

## Submissions

* Visv Shah -- Submitted report on Brightspace
* Katya Teodorovich -- Submitted code on Vocareum

## Description

* [User.java](src/User.java)
    * The User class contains fields for storing login information for a user. Buyer and Seller are subclasses of User
      because they require the same basic information, but Buyer and Seller differentiate themselves in almost every
      other regard. The User class also contains getters and setters for the username and password, and it has the
      ability to print information to a user's dashboard as well as save to a file.
* [Buyer.java](src/Buyer.java)
    * The Buyer class is a subclass of User. In addition to a username and a password, Buyer has a shopping cart, which
      stores items that a buyer intends to purchase but has not done so yet. (Include more here once more is added)
* [Seller.java](src/Seller.java)
    * The Seller class is a subclass of User. In addition to a username and a password, Seller has a list of Stores that
      it owns. A seller can export the products in their stores to a file, write all of their stores to a file, and
      printout a dashboard of statistics including the purchase history of their customers.
* [Store.java](src/Store.java)
    * The Store class is utilized by both the Seller and the Buyer class. Stores have an index in the Store.csv file, a
      name, the name of the seller that owns the store, total sales the store has done, total revenue the store has
      made, and a list of products that the store sells. A seller can create as many stores as they would like, and they
      can add, delete, or modify products that the store sells. Buyers make purchases from stores in the market.
    * The testing for this class is done in the main method testing.
* [Product.java](src/Product.java)
    * The Product class stores all the information related to the products being sold and purchased: name, the name of
      the store it is sold in, a description, a quantity, a price, and it's index in the Products.csv file. Products do
      not have much functionality outside of getters and setters and a toString method. Products and their attributes
      are used by almost every other class.
* [Market.java](src/Market.java)
    * The Market class is the general container which includes the lists of all Products, Users, and Stores in the
      entire marketplace. It has functionality for updating all the CSV files and filtering all the lists of products
      when interacting with the user. The testing of Market largely overlaps with the testing of Main, as it is
      basically the container for the objects that are actually referred to in the Main method. 
* [Main.java](src/Main.java)
    * The Main class is what is actually ran when the application is started. It contains a menu that a user can go
      through to complete actions such as logging in, buying items, creating stores, editing products, etc. If the
      program is ever ended prematurely the data that was changed persists through csv files.

