
# Installation

1. Requires [Leiningen](https://leiningen.org/) & [JavaSDK](http://openjdk.java.net/)

2. Be sure to follow the [studentmgmt README instructions](https://github.com/ca-collins/studentmgmt) to get the server and database running.

3. Clone the project by running the following in your terminal: `git clone https://github.com/ca-collins/profezzerk.git`

4. Navigate to the project folder and run the following command in the terminal to start the figwheel compiler: `lein figwheel`
  - This should automatically open your webrowser to <http://localhost:3449/>


5. If you adjust the backend endpoint (per the [studentmgmt README](https://github.com/ca-collins/profezzerk)), you will have to make an equivalent change to the `student-mgmt-server` in `src\profezzerk\core`

--------------------------------------------------------------------------------

# Technologies Used

- [Reagent](https://github.com/reagent-project/reagent) (ClojureScript wrapper around React)
- [CLJS Ajax](https://github.com/JulianBirch/cljs-ajax) (requests)
- [Soda Ash](https://github.com/gadfly361/soda-ash) (interface btw Reagent & Semantic UI)

--------------------------------------------------------------------------------

# Requirements

## General

- [x] When first launched, the application should display a list of all students in the database (no paging is necessary).

- [x] A student on the list can be deleted by the end user. When the student is being deleted, the user should be presented with an "are you sure" dialog box.

- [x] A student on the list can be updated.

- [x] A new student can be added to the list.

## Architecture

- [x] This student management application should consist of three parts: a frontend, a backend, and a database.

- [x] The frontend should be written using a modern frontend library like React, Vue, etc.

- [x] The backend should expose a REST API that the frontend will call

- [x] This should be the only way that the frontend and backend communicate.
---
### REPL

The project is setup to start nREPL on port `7002` once Figwheel starts.
Once you connect to the nREPL, run `(cljs)` to switch to the ClojureScript REPL.

### Building for production

```
lein clean
lein package
```

--------------------------------------------------------------------------------

# License

Copyright © 2019 FIXME

This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at <http://www.eclipse.org/legal/epl-2.0>.

This Source Code may also be made available under the following Secondary Licenses when the conditions for such availability set forth in the Eclipse Public License, v. 2.0 are satisfied: GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version, with the GNU Classpath Exception which is available at <https://www.gnu.org/software/classpath/license.html>.