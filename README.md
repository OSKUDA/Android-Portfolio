# Portfolio
Hi, I am **Oskar!**<br>
This repo holds android applications developed by me during the course of ***learning*** & ***discovering*** android development. 
## ToDoList
#### *created an app to track list of tasks*
>Android Studio (java & firebase real-time database)

 <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/splash_screen.jpg" width="250"/>  <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/username_login.jpg" width="250"/> <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/dashboard.jpg" width="250"/>
<img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/new_task.jpg" width="250"/>
 <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/dashboard1.jpg" width="250"/> <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/edit_task.jpg" width="250"/> <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/firebase_data.PNG" width="500"/>
 
 
### Specifications
* App has *multiple* activity (Splash screen, username registration, dashboard, add task, edit task)
* Username screen is only shown for first time user
* Registered user's name is stored locally using *sharedPreferences*
* *Crosschecks* are made with database such that only unique username will be registered
* Dashboard activity implements recycler view for task list and a button to add task
* Dashboard activity refreshes task list on every changes made on database
* Add new task activity collects required details and post data to database
* Edit task activity retrieves current task and updates any changes made to database
* Delete function completely remove selected task from database
* Every input is *validated* before posting it to real-time database
### What I learned?
* store and retrieve data on local memory using ***SharedPreferences*** 
* ***validate*** input from user
* work with ***real-time firebase*** database
* enhance ***UI/UX*** of app 
* work with material.io library
