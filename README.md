# Food Trip [![Build Status](https://travis-ci.org/Chark/food-trip.svg?branch=master)](https://travis-ci.org/Chark/food-trip) [![Coverage Status](https://coveralls.io/repos/github/Chark/food-trip/badge.svg?branch=master)](https://coveralls.io/github/Chark/food-trip?branch=master)
Spring Boot application for managing, commenting on various fast food restaurants. 

## Front-end setup
To setup front-end dependencies you'll have to install `node.js` following up with `grunt` and `bower`.

First, download and install the latest stable version of [node.js](https://nodejs.org/en/download/). After installation is complete, open command prompt, navigate to project `root` and run these commands in the following order (this will install `grunt` and `bower`):

```bash
npm install bower -g  # install bower globally
npm install grunt -g  # install grunt globally
```

After the installation is complete, you will have to initialize the project and download all the required dependencies. To do so once again navigate to project `root` via the command prompt and type in the following commands:

```bash
npm install
bower install
```

Thats it, the front-end part of the project should now have all the required files ready for development.

## Front-end developing (unfinished)
After the initial setup is complete, you will have to run some cpmmands in order to concat the javascript files you'll be creating, to do so open command prompt, navigate to project `root` and first initialize the project:

```bash
grunt init    # build project directory structure
```

Once initialization if complete, you can start watching for file changes and start developing.
```bash
grunt watch   # watch for file changes
```
