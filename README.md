# G2T2-Abba

- Install Maven. Documentation on Maven installation can be found here : https://maven.apache.org/download.cgi
- Run `mvn clean install`
- Get WAMP/MAMP up and running. Installation guide : https://www.geeksforgeeks.org/how-to-install-and-set-up-a-wamp-server/. Then create a database called `abbaschedulingsystem` or whatever is your DATASOURCE_URL in `.env` file located in the root folder of the repository.
- Run `mvn spring-boot:run`
- Run the `data.sql` SQL script in MySQL to populate the tables with the data
- On Java terminal it should say the application is started on port 8080
- [AbbaApplication] [  restartedMain] c.g.g4t2project.AbbaApplication          : Started AbbaApplication in 8.209 seconds (process running for 8.805)

- pom.xml is the root folder
- classes are compiled into target folder, from src folder
- All java files can be found in src/main/java/com/g4t2project/g4t2project, from the root folder. They are categorised into entity, repository, scheduler, DTO, service, controller, config(for authentication feature), exception(for custom exceptions), scheduler for scheduled jobs, and util for more complex helper functions.

### All entities are in src/main/java/com/g4t2project/g4t2project/entity/
For front-end purposes(to access the UI of the application):
Make sure you have the following installed:

Node.js (v14 or higher)
npm (comes with Node.js)
### Open another terminal separate from the one running SpringBoot ###
In terminal, with your directory at the root folder, run
- `cd front-end`
- `npm install`
- `npm run dev`
-  Go to `localhost:3000`
-  Register with an actual email, and verify your email from your email inbox
-  Login with your username and password.
-  To access Admin features, select "Worker" as role, with the username "root". Your password does not matter.
-  To access Client features, select "Client" as role
-  To access Worker features, select "Client" as role

