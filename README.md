## Cyber Security Base: Course Project 1
>Project base cloned from [here](https://github.com/cybersecuritybase/cybersecuritybase-project)

It is made on top of the starter code, so testing it is simple. Clone and run on your IDE.
There is a test account ted / ted. I suggest you to create (at least) one extra account for testing purposes, and run them side by side (with different browsers/incognito)
The application itself is pretty raw, and ugly. Sorry for that.


This project is for testing purposes only.


## 1.	A2:2017 Broken Authentication

First of all the site does allow brute force password fuzzing. You can test this with Owasp Zap.
The Size Response Header size will be smaller with the right password.
There is no validation of any kind on the password or usernames, so password could be even left as empty. (Feel free to try)

Fixing it: Adding multi-factor authentication, preventing weak-passwords with validation and adding limits to failed logins would prevent these. Logging failed attempts and attacks would also be desirable. 

## 2.	A3:2017 Sensitive Data Exposure

As you can see when in the index page, all the email and credit card data is visible there for anyone to see. The whole application runs on http, and not in https, so everything can be stolen, and when it’s not encrypted. Even if the credit card info would be encrypted, it could be possibly be retrieved with an SQL injection. 

Fixing it: Some of the info should be hidden and encrypted, or maybe discarded after use. Identify the sensitive data and store it properly. Don’t store it if it is not necessary.  There are strong hashing functions to use, just make sure all is up to date. Encrypt all transferring data and use secure protocols. 

## 3.	A5:2017 Broken Access Control

When logged in with ted and with another user you have created, you can see that ted has the required Authority level of 2 to sign up for the event. After you click on the form, you can see the url http://localhost:8080/superSecret123form. Now, use that url with the account you created, which has Authority level of 0. You are now able to sign up for the event, even when you shouldn’t be allowed. The authority check is clumsy, as it only hides the button linking to this form. The same mistakes could happen on admin panel, as the h2-panel, which should be visible by admins only.

Fixing it: There are 2 ways to fix this. First, a bit clumsy way, would be to add the same authority check on the form. This wouldn’t prevent http requests on the page though = still vulnerable.
So adding 
.antMatchers("/superSecret123form").hasAuthority("ALLOWED") to the security configuration file will allow ted to see the form, as he has the required authority, but other newly created accounts doesn’t have it yet, so it will be visible to ted only.


## 4.	A6 Security Misconfiguration / A9 Using Components with Known Vulnerabilities
Site has improperly configured permissions, as for example anyone can access the h2-console. The password change from url is really bad feature and can be exploited so easily. Every authenticated user (logged in) can post and see all the data Spring has security features disabled (http.csrf().disable();)  and it is using an old version of spring (1.4.2, and the newest is 2.1.1)

Fixing it: Access control for admins is something to start with. Removing unnecessary features, such as the password change, changing permissions on http requests and updating software. 

## 5.	A7:2017- Cross-Site Scripting (XSS)

When logged in as Ted, you can see the Signup for the event button, which takes you to the signup page. Ted has already signed for the event, but you can sign again. 
The form is not done properly, and you can insert code there.
For example, you can try with “name”: “  <span>TestName<script>alert(123);</script></span>“ and address: “TestAddress 123” .  
Another fun to test is this one (<script>window.location.replace("/password/123");</script>).
As the site has currently really bad way to change current user’s password: simply by sending a POST request to /password/{newpassword}.
(This will end in an endless loop, as the password change redirects back to index and the script redirects to the password change. Click the clear all button to end it :D)
After going back to index, you will see the alert window. This can be exploited in many ways. As these signups are visible to every user, you can log in with another account and the same script will run. The message system is also exploitable, and is done as poorly.
This vulnerability can be tested with Owasp ZAP as well. If you want to do so; after opening the app, start attack on the POST request of http://localhost:8080/superSecret123form.
After the scan, you can see under the Alerts tab that “Cross Site Scripting” is listed there.

Fixing it:
Fixing it here is quite simple. With correct validation of the inputs (these kind of scripts wouldn’t be possible to save at all), and with the index.html listing the signups lists the signups with th:utext. Change it to th:text, and the script wont run.  
