## GuardianNewsApp
The structure of a news app which gives a user regularly-updated news from the Internet related to a particular location.
Project which comes under Udacity Android Basics Nanodegree Program .

## Project Overview
The goal is to create a News Feed app which gives a user regularly-updated news from the Internet related to a particular topic, person, or location. The presentation of the information as well as the topic is up to you.

## What will i learn?
In the most recent portion of the Nanodegree program, you worked to build the News app. Along the way, you learned about connecting to the Internet in Android, parsing responses from an API, updating the information in your app, and properly displaying that information. Practicing these skills is imperative in order to build apps that delight and surprise users by anticipating their needs and supplying them with relevant information.


## Requirements
. App contains a main screen which displays multiple news stories
. Each list item on the main screen displays relevant text and information about the story.
. The title of the article and the name of the section that it belongs to are required field.
. If available, author name and date published should be included. Please note not all responses will contain these pieces of data, but it is required to include them if they are present.
. Stories shown on the main screen update properly whenever new news data is fetched from the API.
. The code runs without errors.
. Clicking on a story uses an intent to open the story in the user’s browser.
. App queries the content.guardianapis.com api to fetch news stories related to the topic chosen by the student, using either the ‘test’ api key or the student’s key.
. The JSON response is parsed correctly, and relevant information is stored in the app.
. When there is no data to display, the app shows a default TextView that informs the user how to populate the list.
. The app checks whether the device is connected to the internet and responds appropriately. The result of the request is validated to account for a bad server response or lack of server response.
. Networking operations are done using a Loader rather than an AsyncTask.
. The intent of this project is to give you practice writing raw Java code using the necessary classes provided by the Android framework; therefore, the use of external libraries for the core functionality will not be permitted to complete this project.
. Code is easily readable such that a fellow programmer can understand the purpose of the app.
. All variables, methods, and resource IDs are descriptively named such that another developer reading the code can easily understand their function.
. The code is properly formatted i.e. there are no unnecessary blank lines; there are no unused variables or methods; there is no commented out code.


# Download
You can download the apk here [GuardianNewsApp](../../raw/master/app/screenshots/app-debug.apk)
