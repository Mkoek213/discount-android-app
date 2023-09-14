# discount-android-app
I created an application, displaying discounts in restaurants. The app is fully working.

What the application looks like:

1. In the application, in addition to the basic menu, in which restaurants are divided into categories(according to the type of food served), you will also find a search bar that allows you to search for restaurants by name. 
2.Next to each displayed restaurant is a button redirecting to the restaurant's address using Google Maps. 
3. There is also a settings menu where we can set the language in which the discounts will be displayed to English or Polish. In addition, a dark mode of the application is also available in the menu.  
4. The first time you open the app after downloading it, a 3-slide tutorial is displayed to explain how it works. 
5. The application continuously retrieves data on discounts from the REST API, which is created using Node.JS and MySQL, all hosted on Google Cloud. Currently, the API is not working because hosting through Google Cloud is paid. In the case of reactivating the application, it would be enough to make a simple REST API with a list of restaurants and discounts in Polish and English and then assign the URLs of these APIs to the function that retrieves data from the API in the application. There is also a refresh button in the main menu, which fetches the data from the API anew.

