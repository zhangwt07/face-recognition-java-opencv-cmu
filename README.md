# face-recognition-java-opencv-cmu
*This is an academic project @cmu.*  
## Overview and features    
  The face recognition project implemented the OpenCV libraries to realize the features of:
  1. Login page  
  As this is for school administration for collecting data of students' visits and queries, it is of importance that the application is protected by login process and only the desired people could have access to it.
  ![Login page](https://github.com/zhangwt07/face-recognition-java-opencv-cmu/blob/master/nbproject/Picture1.png)
  
  2. Multiple faces recognition with information dashboard  
  The application could detect multiple faces after starting the camera and would show the information of the last detected person of his/her name, gender, current program if student or position if staff, age and last visit information. The last visit information is composed of the last visit time and the last or most frequent visit reason. The profile photo will also be shown at the top of the dashboard. In the camera, the names would appear above the persons for a clear view.
  ![Face recognition](https://github.com/zhangwt07/face-recognition-java-opencv-cmu/blob/master/nbproject/recognition.png)
  
  3. Add record   
  This feature is designed with embedded JavaDB. If clicked the "Add Record" button, there would be a prompted window to input information. The andrew ID (CMU unique student ID) is entered with the person's information as in the dashboard. If this was not the desired person (e.g. there are more than one faces detected) the user may edit the ID manuually. The application will check for the person's ID in the database. If the person is not currently in the database, the application will change into the process of registration automatically.
  
  4. Registration  
 If there is a new account to be registrated, the process will ask for basic information like Andrew ID, name, DOB, program etc. After checking if this person exists in the database, the process will move to profile taking and recognition. There is a grid for instructions for positions in taking the profile photo. The recogniction requires to take 10 photos for successful recognition.
 ![Registration profile photo](https://github.com/zhangwt07/face-recognition-java-opencv-cmu/blob/master/nbproject/profile-photo.png)
 
  5. Analytics  
  As this is for the administration use, the analytics of students' visit data and reasons will be analyzed and reports will be generated. The report could have a desired range of time period and user could choose which analytic report to be generated. Reports will be downloaded to designed path and auto opened after generation.
  ![Analytics reports](https://github.com/zhangwt07/face-recognition-java-opencv-cmu/blob/master/nbproject/analytics.png)
