import React from 'react';
import './CountPeople.css';

class CountPeople extends React.Component {
  constructor(props) {
    super(props);

    // State object stores the entry and exit counts
    this.state = {
      entrycount: 0,
      exitcount: 0
    };

    // Bind methods so `this` refers to the component instance
    this.updateEntry = this.updateEntry.bind(this);
    this.updateExit = this.updateExit.bind(this);
  }

  // Increments entrycount by 1 whenever someone logs in (enters the mall)
  updateEntry() {
    this.setState((prevState) => ({
      entrycount: prevState.entrycount + 1
    }));
  }

  // Increments exitcount by 1 whenever someone exits the mall
  updateExit() {
    this.setState((prevState) => ({
      exitcount: prevState.exitcount + 1
    }));
  }

  render() {
    return (
      <div className="count-people">
        <h1>Mall Visitor Counter</h1>

        <div className="counter-row">
          <div className="counter-box">
            <span className="counter-label">People Entered</span>
            <span className="counter-value">{this.state.entrycount}</span>
          </div>

          <div className="counter-box">
            <span className="counter-label">People Exited</span>
            <span className="counter-value">{this.state.exitcount}</span>
          </div>
        </div>

        <div className="button-row">
          <button className="btn login-btn" onClick={this.updateEntry}>
            Login
          </button>
          <button className="btn exit-btn" onClick={this.updateExit}>
            Exit
          </button>
        </div>
      </div>
    );
  }
}

export default CountPeople;
