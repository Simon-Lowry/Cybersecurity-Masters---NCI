import React from "react";
import { Navigate } from 'react-router-dom';

export default class ForgotPassword extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            question1: '',
            question2: '',
            password: '',
            passwordRepeated: '',
            signUpError: [],
            userProfile:''
          };
        
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleResponse = this.handleResponse.bind(this);

     }

     componentDidMount() { 
        localStorage.clear();
        
     }

    handleInputChange() {

    }

    handleSubmit() {

    }

    handleResponse() {

    }

    render() {
        return(
            <div className="Signup-form-container">
                {this.state.userProfile &&(
                    <div>
                        <Navigate to="/userProfile" replace={true} />
                    </div>
                )}
            <form className="Signup-form" onSubmit={this.handleSubmit}>
              <div className="Signup-form-content">
                <h3 className="Signup-form-title">Signup</h3> 
                {this.state.ForgotPasswordError &&(
                   <div>
                    <ul style={{ color: "red" }}>
                        {this.state.signUpError.map((item, i) => {
                            return <li key={i}>{item}</li>
                        })}
                    </ul>
                   </div>
                )}
                <div className="form-group mt-3">
                  <label>Question 1: What was your first pet's name?</label>
                  <input
                    type="input"
                    className="form-control mt-1"
                    placeholder="e.g rolo polo"
                    name="name"
                    onChange={this.handleInputChange} 
                    value={this.state.question1}
                  />
                </div>
                <div className="form-group mt-3">
                  <label>Question 2: What is your mother's maiden name?</label>
                  <input
                    type="input"
                    className="form-control mt-1"
                    placeholder="Enter maidne name here."
                    name="question2"
                    onChange={this.handleInputChange} 
                    value={this.state.question2}
                  />
                </div>
                <div className="form-group mt-3">
                  <label>Password</label>
                  <input
                    type="password"
                    className="form-control mt-1"
                    placeholder="Password"
                    name="password"
                    onChange={this.handleInputChange} 
                    value={this.state.password}
                  />
                </div>
                <div className="form-group mt-3">
                  <label>Password Repeated</label>
                  <input
                    type="password"
                    className="form-control mt-1"
                    placeholder="Password"
                    name="passwordRepeated"
                    onChange={this.handleInputChange} 
                    value={this.state.passwordRepeated}
                  />
                </div>

                <div className="d-grid gap-2 mt-3">
                  <button type="submit" className="btn btn-primary">
                    Submit
                  </button>
                </div>
              </div>
            </form>
          </div>
        )
      }

}