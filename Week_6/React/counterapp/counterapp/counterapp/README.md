# counterapp — React State Hands-on Lab

## Objective
Learn how to use the React `state` object in a class component by building a
mall visitor counter.

## What this app does
The `CountPeople` component tracks two numbers:
- **entrycount** — number of people who have entered the mall
- **exitcount** — number of people who have exited the mall

Both values are stored in the component's `state` object, initialized inside
the **constructor**. Two buttons update that state:

| Button  | Method it calls | Effect                        |
|---------|------------------|--------------------------------|
| Login   | `updateEntry()`  | `entrycount` increases by 1   |
| Exit    | `updateExit()`   | `exitcount` increases by 1    |

Every time `setState()` runs, React re-renders the component, so the counters
on screen update immediately after each click.

## Project structure
```
counterapp/
├── package.json
├── public/
│   └── index.html
└── src/
    ├── index.js          # App entry point
    ├── index.css         # Global styles
    ├── App.js            # Renders <CountPeople />
    ├── CountPeople.js    # The component with constructor + state
    └── CountPeople.css   # Component styling
```

## Key concepts demonstrated

### 1. Constructor + state
```js
constructor(props) {
  super(props);
  this.state = {
    entrycount: 0,
    exitcount: 0
  };
  this.updateEntry = this.updateEntry.bind(this);
  this.updateExit = this.updateExit.bind(this);
}
```
`this.state` is initialized once, inside the constructor, as required by
React class components. Methods are bound to the instance here so `this`
works correctly when the methods are used as event handlers.

### 2. Updating state
```js
updateEntry() {
  this.setState((prevState) => ({
    entrycount: prevState.entrycount + 1
  }));
}
```
State is never modified directly (`this.state.entrycount++` would be wrong).
Instead, `this.setState()` is called, which tells React to update the value
and re-render the component. The updater-function form (`prevState => ...`)
is used because the new value depends on the previous value.

### 3. Reading state in render()
```js
<span className="counter-value">{this.state.entrycount}</span>
```
`render()` reads directly from `this.state`, so the displayed numbers always
reflect the latest state.

## How to run this project

### Prerequisites
- Node.js and npm installed
- Visual Studio Code (or any editor)

### Steps
1. Copy/unzip the `counterapp` folder to your machine.
2. Open a terminal in the `counterapp` folder.
3. Install dependencies:
   ```bash
   npm install
   ```
4. Start the development server:
   ```bash
   npm start
   ```
5. Your browser will open automatically at `http://localhost:3000`, showing
   the counter app.

## Expected behavior
- On load: **People Entered = 0**, **People Exited = 0**
- Click **Login** → "People Entered" increases by 1 each click
- Click **Exit** → "People Exited" increases by 1 each click
- The two counters are completely independent of each other

## Notes / Extension ideas
- Add a "Reset" button that sets both counts back to 0 using `setState`.
- Add a derived value, `people currently inside = entrycount - exitcount`,
  computed in `render()` from state.
- Convert `CountPeople` to a function component using the `useState` Hook,
  to compare the Hook-based approach with the class + constructor approach
  used here.
