import React, { Component } from 'react';
import Post from './Post';

class Posts extends Component {

  // Step 5: Initialize component state with an empty list of posts.
  constructor(props) {
    super(props);
    this.state = {
      posts: [],
      hasError: false
    };
  }

  // Step 6: loadPosts() fetches data from the API and stores it in state.
  loadPosts = () => {
    fetch('https://jsonplaceholder.typicode.com/posts')
      .then((response) => response.json())
      .then((data) => {
        this.setState({ posts: data });
      })
      .catch((error) => {
        // Network/fetch errors are handled here directly, since fetch()
        // rejections are not caught by componentDidCatch (that hook only
        // catches errors thrown during rendering/lifecycle methods).
        console.error('Error fetching posts:', error);
        alert('Error fetching posts: ' + error.message);
      });
  };

  // Step 7: componentDidMount runs once, right after the component is
  // first rendered to the DOM — the correct place to kick off data fetching.
  componentDidMount() {
    this.loadPosts();
  }

  // Step 9: componentDidCatch catches JavaScript errors thrown anywhere
  // in this component's render tree (during rendering, in lifecycle
  // methods, or in constructors of child components) and lets us respond
  // instead of letting the whole app crash to a blank screen.
  componentDidCatch(error, info) {
    this.setState({ hasError: true });
    alert('Something went wrong while rendering posts: ' + error.message);
    console.error('componentDidCatch:', error, info);
  }

  // Step 8: render() displays each post's title (heading) and body
  // (paragraph) by delegating to the Post component.
  render() {
    if (this.state.hasError) {
      return <p>Unable to display posts right now.</p>;
    }

    return (
      <div className="posts">
        <h1>Blog Posts</h1>
        {this.state.posts.map((post) => (
          <Post key={post.id} title={post.title} body={post.body} />
        ))}
      </div>
    );
  }
}

export default Posts;
