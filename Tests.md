# Test 1: Buyer sign up

Steps:
1. User launches application.
2. User selects the email textbox on the top left of the screen.
3. User enters the email via the keyboard.
4. User selects the password textbox on the top left of the screen
5. User enters a password via the keyboard
6. Use selects “Buyer” option on the top right of the screen
7. User selects the "Sign Up" button. 

Expected result: Application verifies the user's username and password and creates a new Buyer account and loads the marketplace. 

Test Status: Passed. 

# Test 2: Buyer sign up - edge case
Steps:
1. User launches application.
2. User selects the email textbox on the top left of the screen.
3. User enters the email they already used for “Test 1”.
4. User selects the password textbox on the top left of the screen
5. User enters a password via the keyboard
6. Use selects “Buyer” option on the top right of the screen
7. User selects the "Sign up" button. 

Expected result: Application gives an error message for the user trying to create an account with an email that already exists.

Test Status: Passed. 

# Test 3: Buyer Log In
Steps:
1. User launches application.
2. User selects the email textbox on the top left of the screen.
3. User enters the email they used for Test 1 via the keyboard.
4. User selects the password textbox on the top left of the screen
5. User enters a password they used for Test 1 via the keyboard.
6. Use selects the “Buyer” option
7. User selects the "Login" button. 

Expected result: Application verifies the user's username and password and logs into an existing Buyer account and loads the marketplace. 

Test Status: Passed. 

# Test 4: Buyer Making a Purchase (Adding to cart and checking out)
Steps:
1. User launches application.
2. User logs in to account as a Buyer. (Following Test 3)
3. User clicks on any product in their main view (middle of their screen) and a pop up of product details will show up. 
4. User increments the quantity they want to add to cart to 2 using the “^” button on the right of the pop up.
5. User selects “Add to Cart”
6. User clicks “OK” on success message
7. User selects “Shopping Cart” button on the bottom left of the screen to view the shopping cart
8. User exists the Shopping Cart
9. User selects “Checkout”
10. User selects “Confirm” on the pop-up
11. User clicks “OK” on success message

Expected result: Application processes the Buyer’s request to add a book to their cart. Then, it makes a purchase and reduces the quantity of the product they purchased to reflect the new number of that book that is in stock.

Test Status: Passed. 

# Test 5: Buyer Viewing Purchase History
Steps:
1. User launches application.
2. User logs in to account as a Buyer. (Following Test 3)
3. User makes a purchase (Following Test 4)
4. User clicks on the “Purchase History” button on the bottom-left of their screen

Expected result: Application loads the Buyer’s purchase history and reflects their purchase in step 3.

Test Status: Passed. 

# Test 6: Buyer viewing Dashboard for Viewing Popular Stores
Steps:
1. User launches application.
2. User logs in to account as a Buyer. (Following Test 3)
3. User clicks on the “Dashboard” option on the top middle of their screen and a pop up for viewing the dashboard will show up.
4. User clicks on “Stats” button on the menu that pops up
5. User clicks on “By products sold” option
6. User clicks on the first store on the top left

Expected result: Application popup loads a list of stores sorted by their sales. By clicking on the most popular store, the user will see the products that store is selling and how many of each of their products are still in stock.

Test Status: Passed. 

# Test 7: Buyer viewing Dashboard for Viewing Stores they have purchased from
Steps:
1. User launches application.
2. User logs in to account as a Buyer. (Following Test 3)
3. User makes a purchase (Following Test 4)
4. User clicks on the “Dashboard” button on the top of their screen and a pop up for viewing the dashboard will show up.
5. User clicks on “Stats” button on the menu that pops up
6. User clicks on “By purchase history” option
7. User clicks on the first store on the top left

Expected result: Application popup loads a list of stores sorted by the user’s purchase history. By clicking on the top left store, the user will see details about the Store they most recently purchased from and see a list of their products and how much of each product is still in stock.

Test Status: Passed. 

# Test 8: Buyer Exporting Purchase History
Steps:
1. User launches application.
2. User logs in to account as a Buyer. (Following Test 3)
3. User makes a purchase. (Following Test 4)
4. User clicks on the “Export Purchase History” button on the bottom of their screen.
5. User enters a valid csv file name in the pop up
6. User clicks “Export to File”

Expected result: Application creates a file with the User’s purchase history at the filename they provided.

Test Status: Passed. 

# Test 9: Buyer searching for products
Steps:
1. User launches application.
2. User logs in to account as a Buyer. (Following Test 3)
3. User selects “Name” option on the top of the screen for searching criteria
4. User types in “Book” to the search bar on the top of the screen via the keyboard
5. User clicks on the “Search button”

Expected result: Application loads a screen of all of the books that match the search criteria. In the search for “Book” in the name, the resulting Search Results will load all the books that contain the word “Book” in its name.

Test Status: Passed. 

# Test 10: Buyer Sorting the marketplace
Steps:
1. User launches application.
2. User logs in to account as a Buyer. (Following Test 3)
3. User clicks on “sort buy” button on top right of the screen 
4. User selects the “Sort Buy: Price” option

Expected result: Application updates main marketplace view (middle of screen) to be sorted from cheapest to most expensive

Test Status: Passed. 

# Test 11: Seller sign up
Steps:
1. User launches application.
2. User selects the email textbox on the top left of the screen.
3. User enters the email via the keyboard.
4. User selects the password textbox on the top left of the screen
5. User enters a password via the keyboard
6. Use selects “Seller” option on the top right of the screen
7. User selects the "Sign Up" button. 

Expected result: Application verifies the user's username and password and creates a new Seller account and loads the marketplace. 

Test Status: Passed. 

# Test 12: Seller sign up - edge case
Steps:
1. User launches application.
2. User selects the email textbox on the top left of the screen.
3. User enters the email they already used for “Test 11”.
4. User selects the password textbox on the top left of the screen
5. User enters a password via the keyboard
6. Use selects “Seller” option on the top right of the screen
7. User selects the "Sign up" button. 

Expected result: Application gives an error message for the user trying to create an account with an email that already exists.

Test Status: Passed. 

# Test 13: Seller Log In
Steps:
1. User launches application.
2. User selects the email textbox on the top left of the screen.
3. User enters the email they used for Test 1 via the keyboard.
4. User selects the password textbox on the top left of the screen
5. User enters a password they used for Test 1 via the keyboard.
6. Use selects the “Seller” option
7. User selects the "Log in" button. 

Expected result: Application verifies the user's username and password and logs into an existing Seller account and loads the marketplace. 

Test Status: Passed. 

# Test 14: Seller creating a Store
Steps:
1. User launches application.
2. User logs in to account as a Seller. (Following Test 13)
3. User clicks on “Add Store” on bottom left of the screen
4. User enters a store name in the textbox that shows itself 
5. User clicks on the check next to the textbox

Expected result: Application creates a new store with the store name the seller provided and stores it on server.

Test Status: Passed. 

# Test 15: Seller adding a product
Steps:
1. User launches application.
2. User logs in to account as a Seller. (Following Test 13)
3. User creates a store (Following Test 14)
4. User clicks on “Add Product” button on bottom of the screen
5. User enters a product in the given format (Product Name,Store name,description,price,quantity) in the textbox that shows itself which has the same store name as the store they created in Step 3
6. User clicks on the check next to the textbox to confirm

Expected result: Application creates a new product in one of the seller’s stores.

Test Status: Passed. 

# Test 16: Seller editing product
Steps:
1. User launches application.
2. User logs in to account as a Seller. (Following Test 13)
3. User adds a product to one of their stores (Following Test 15)
4. User clicks on the “Edit Product” button on the bottom of the screen
5. User clicks on one of the products in the pop-up menu
6. User changes a field by entering new text into one of the textboxes via the keyboard
7. User clicks the “Edit” button

Expected result: Application edits one of the seller’s products and updates that value in the database.

Test Status: Passed. 

# Test 17: Seller viewing Dashboard for Store Sales
Steps:
1. User launches application.
2. User logs in to account as a Seller. (Following Test 13)
3. User creates a store (Following Test 14)
4. User clicks on the “Dashboard” button on the top of their screen and a pop up for viewing the dashboard will show up.
5. User clicks on “Stats” button on the menu that pops up
6. User clicks on “By products sold” option
7. Buyer clicks on one of their stores

Expected result: Application popup loads the statistics of one of the seller’s stores.

Test Status: Passed. 

# Test 18: Seller Exporting Products
Steps:
1. User launches application.
2. User logs in to account as a Seller. (Following Test 13)
3. User adds a product to one of their stores (Following Test 15)
4. User clicks on the “Export” button on the bottom of their screen.
5. User enters a valid csv file name in the pop up
6. User selects the store they want to export products from
7. User clicks “Export to File”

Expected result: Application creates a file with all the products in that Seller’s store

Test Status: Passed. 

# Test 19: Seller importing products
Steps:
1. User launches application.
2. User logs in to account as a Seller. (Following Test 13)
3. User creates a store (Following Test 14)
4. User clicks on the “Import” button on the bottom of their screen.
5. User enters a valid csv file name in the pop up (The csv must have format index,name,store,description,quantity,price)
6. User clicks “Import File”

Expected result: User adds all the products in the csv file to the seller’s stores (based on the store argument of the csv file)

Test Status: Passed. 

# Test 20: Seller views products in customer shopping cart
Steps:
1. User launches application.
2. User logs in to account as a Seller. (Following Test 13)
3. User adds a product to one of their stores (Following Test 15)
4. User clicks on the “View Stores” button on the bottom of their screen.
5. User looks at the fourth column (right-most) of the pop up

Expected result: Application loads a pop-up of all of the user’s stores and shows data for which products are in customer shopping carts

Test Status: Passed. 
