#URSS-Website

This repository contains the source code of the web part of the Epitech's RSS agregator project.
The goal of the project is to design and craft a RSS Reader collection of application.
This project use AngularJS and bootstrap 3.

To see other parts follow the following links :
   - serveur :
   - android :
   - java desktop application :

##Launch website localy

To use the website localy you must use the following command :

**npm**

```
npm install
```

```
npm start
```

Then go to http://localhost:8000

##Generate documentation

Type in the root project directory

```
node_modules/jsdoc/jsdoc.js --configure node_modules/angular-jsdoc/common/conf.json --template node_modules/angular-jsdoc/angular-template  --destination build/docs  --readme README.md  --recurse app/controller
```