
# Scheduling System for Wossomotta University.

There are 3 level of user access:

<strong>First user level is root user:</strong> <br />
<li>They are able to approve requests of new root and admin users</li>
<li>They are able to maintain course catalogs by manually maintaining a course </li>
<li>They are able to upload course and section file to batch update course catalog </li> <br />


https://user-images.githubusercontent.com/51452855/119695086-74e8c500-be13-11eb-88ca-48170900bfca.mov


<br /><strong>Second user level is admin user:</strong><br />
<li>They are able to maintain course catalogs by manually maintaining a course</li>
<li>They are able to upload course and section file to batch update course catalog </li>


<br /><strong>Final user level is student user:</strong><br />
<li>They are able to maintain possible courses from the course catalog </li>
<li>They are able to maintain designated courses from their possible courses </li>
<li>They are able to maintain their reserved times for unavailability for classes </li>
<li>They are able to auto-generate schedules based on designated courses around their reserved times </li>

<br /><strong>To set this up locally:</strong><br />
1. Clone the repository locally<br />
2. Navigate to src/main/resources/application.sources file<br />
3. Update your file with database information and gmail authentication<br />
4. Run the application and open it at localhost:8080<br />
