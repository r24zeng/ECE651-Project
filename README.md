# ECE651-Project
[Front-end source code link](https://github.com/faushine/ECE651-Project) <br>
[Back-end source code link](https://github.com/faushine/ECE651-Project-Back-end) <br>
Tools:
+ Front-end: Android Studio + Java + JavaScript
+ Back-end: Flask + Python + SQLAlchemy + RESTful style
+ Test: Postman
+ Version control: Github

Team members:
+ Project manager: Run Zeng
+ Front-end: Yuxin Fan, Quanyu Wang
+ Back-end: Zijian Liu, Run Zeng(unit test)

### WatNews purpose and users
This is a front and back separated Android app, which gathers all news from University of Waterloo websites. Users are students, professors and other stuffs of University of Waterloo and who is care about the news within UW.

### Requiremnts 
+ Allow users to register and login(after one hour the session will expire)
+ Allow users to modify its own profile
+ Allow users to view all news without login
+ Allow users to follow news source within search page
+ Allow users to view all following news source within login session
+ Allow to add admin account in database to assist development of front-end

### Modules
+ Front-end:
  + Register
  + Login
  + Modify user's own profile
  + Search to follow news source
  + View all news source wihout login
  + View following news source within login
+ Back-end:
  + Build database to set up users(includes admin), news source and news source tables, and relevant linux commands
  + Users authorioty 
  + Support basic front-end functions using RESTful API
  + Error codes
  + News craws within University of Waterloo

### Front-end unit tests

### Back-end unit tests 
[Completed unit test link](https://github.com/faushine/ECE651-Project-Back-end/blob/master/test_newsapp.py) <br>
Statement coverage: 89% <br>
Branch coverage: 88% <br>
*Only craws related haven't been covered because it's dynamic.*

### System test screenshots
![SignUp: when a user is the first time to use WatNews app, he or she can create a new account.](https://github.com/faushine/ECE651-Project/raw/master/image/signup.jpg)
![Login: The user can use their username and password to login WatNews app.](https://github.com/faushine/ECE651-Project/raw/master/image/login.jpg)
![News: The user can view all following news source or see the newest news from the website.](https://github.com/faushine/ECE651-Project/raw/master/image/news.jpg)
![Search: The user can search their favorite categories and add to favorite](https://github.com/faushine/ECE651-Project/raw/master/image/search.jpg)
![Favorite: Show this user's favorite categories](https://github.com/faushine/ECE651-Project/raw/master/image/following.jpg)
![Account Information: show this user's account information, such as department, faculty, gender and so on.](https://github.com/faushine/ECE651-Project/raw/master/image/account.jpg)
![Edit: The user can edit his or her profile.](https://github.com/faushine/ECE651-Project/raw/master/image/edit.jpg)


