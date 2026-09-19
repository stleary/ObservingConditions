# Observing Conditions: Introduction

Over six sessions, this mini-course builds a Java service end to end: from an empty repository to a running API. The goals are to deepen your Java practice, and to give you a sense of what it's like to work in a software development team.

Along the way you'll:

* Write unit tests, and write them *before* the code they test  
* Use a build tool and version control the way a team would  
* Shape raw data into types that model the problem  
* Refactor working code and trust your tests to catch you  
* Read another developer's documentation and build on their library  
* Decide how your API should behave when a caller gets it wrong  
* Call a remote service, and handle what happens when it's unavailable  
* Return to code you wrote weeks earlier and pick it back up  
* Have your work reviewed, and respond to the comments  
* Release new versions of your application

---

## The project

A small Spring Boot service that answers **"What will the sky be like on a given night at a given place?"**

- Feature one: moon phase and illumination for a date, calculated locally.  
- Feature two: the same data from a remote API, used as a second source and a cross-check.

### Why this project

Astronomy has **k**nown new-moon dates, which means the tests can assert against real answers. 

### Deliberately out of scope

Not included at this time:  
	Database  
Authentication  
Frontend  
Docker  
Availability  
Reactive elements

