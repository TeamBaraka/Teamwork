# Teamwork
Teamwork in Mobile Applications for Android course @ TelerikAcademy

# My Diary

- My Diary is an application for saving everything, which is important or fun for you from your daily life - lectures, cinema, trips ..etc. 
- You can record your past or current events, but also make reminders for future arrangements. 
- Events are described by title, more details, category, date and time, you can add place from Google and take a picture to complete it all.
- The application provides option for creating, editing, deleting, searching events.
- On main page are displayed all events for today. On click you can see event details, edit, or delete event.
- On second page is the search option, with the same functionality. 
- Each event can be added to your GoogleCalendar.

##  Requirements:

### Creativity

- it is a regular application but it extends regular calendar with adding different details options

### Value to the end user

- we tried to make it clickable, with different functionalities

### Use custom Views

- 

### Use animations

- implemented loader on events search

### Device APIs

  - Camera
  - Geolocation

### Application Storage (SQLite)

- implemented table Events with get, put, post and different queries

### Remote data

- Google API - for Places and Calendar 

### Background tasks

-

### Touch friendly UI

- UI is clickable and easily navigated

### Notifications

- Used notifications to provide feedback to the user
  - Show a message when the user submits, edits, deletes data to the app
  - Used SystemService for setting alarm for pending events, even when app is closed

### Gestures

- Used Swipe

### High-quality code and reusability
- The application code is separated into 4 modules
 - Database, Helper classes, View Control and UI

### Validation and Error handling
- No extensive validation, since it's own user app and can leave an event without a title for example, only with category if they want. But some obligatory details are handled with UI, Date is set current date and handled not to crush, Category is set default.

##  Team
- Obelixx (Alexander Angelov)
- zvet80 (Tsvetanka Chipilova)

##  Deliverables

- YouTube: https://www.youtube.com/watch?v=FZQDzQREL1Y&feature=youtu.be- 
- Telerik: http://best.telerikacademy.com/projects/155/MyDiary
- GitHub repository: https://github.com/TeamBaraka

