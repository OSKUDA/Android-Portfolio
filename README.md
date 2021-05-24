# Portfolio
Hi, I am **Oskar!**<br>
This repo holds android applications developed by me during the course of ***learning*** & ***discovering*** android development. 
## ToDoList
#### *created an app to track list of tasks*
>Android Studio (java & firebase real-time database)

 <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/splash_screen.jpg" width="180"/>  <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/username_login.jpg" width="180"/> <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/dashboard.jpg" width="180"/><br>
<img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/new_task.jpg" width="180"/>
 <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/dashboard1.jpg" width="180"/> <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/edit_task.jpg" width="180"/> <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/todolistScreenshots/firebase_data.PNG" width="300"/>
 
 
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

## ReadIt
#### *created an app to search & read news*
>Android Studio (java & Guardian API)

 <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/readitScreenshots/Screenshot_2021-02-19-19-02-38-406_io.github.oskuda.readit.jpg" width="180"/>  <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/readitScreenshots/Screenshot_2021-02-19-19-02-47-360_io.github.oskuda.readit.jpg" width="180"/> <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/readitScreenshots/Screenshot_2021-02-19-19-02-58-461_io.github.oskuda.readit.jpg" width="180"/>
<img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/readitScreenshots/Screenshot_2021-02-19-19-03-18-423_io.github.oskuda.readit.jpg" width="180"/>
 
 
### Specifications
* App is integrated with *Guardian API*
* API feeds the app with news using *custom query*
* Uses *httpUrlConnection* to request search query and fetch result
* Search query are stored locally using *sharedPreferences*
* Uses *Picasso* library for caching and downloading image files
* On startUp last known query is loaded up
* Received data is on Json format
* Uses *JSON.org* and *Jsoup* to parse json and html data
* *RecyclerItemClickListener* class is used to manage clicks with *GestureDetector*
* Displays *error message* if no news article was found
* User input is *validated* before processing it

### What I learned?
* Use *RecyclerView* with *GestureDetector* to intercept user inputs and act accordingly
* Integrating *Guardian API* and learn about *API* architecture overall
* Using *AsyncTask* multi-threading for downloading data over the internet
* *Parsing and Validating* data from API
* *Building* custom query to request the API
* Integrate *exception handling* during downloads and parsing of data
* Generate and work on *custom theme* including day/night mode
* Design *custom layout* for txtView, editText and ImageView widgets 
* Learned about *backward app compatibility* and how to deal with older versions

## AudioRecorder
#### *created an app to record & playback audio*
>Android Studio (java & sqlite3)

 <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/audiorecorderScreenshots/Screenshot_2021-05-24-21-42-34-704_io.github.oskuda.audiorecorder.jpg" width="180"/> <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/audiorecorderScreenshots/Screenshot_2021-05-24-21-43-01-108_io.github.oskuda.audiorecorder.jpg" width="180"/> <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/audiorecorderScreenshots/Screenshot_2021-05-24-21-43-52-377_io.github.oskuda.audiorecorder.jpg" width="180"/> <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/audiorecorderScreenshots/Screenshot_2021-05-24-21-44-32-858_io.github.oskuda.audiorecorder.jpg" width="180"/> <img src="https://github.com/OSKUDA/Android-Portfolio/blob/master/screenshots/audiorecorderScreenshots/Screenshot_2021-05-24-21-45-16-964_io.github.oskuda.audiorecorder.jpg" width="500"/>
 
 
### Specifications
* App Database is implemented using *sqlite3*
* It performs all the *CRUD (Create, Read, Update, Delete) operations*
* Uses *Cursor* and *SQLiteOpenHelper* class to implement CRUD
* *MediaRecorder* and *MediaPlayer* is used for record and playback of audio
* Database schema is designed to *track audio files*, store relevant *meta-data* of files, and feed data to recycler view
* Audio files are stored in '.3gp' format

### What I learned?
* 
